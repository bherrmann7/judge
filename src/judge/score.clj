(ns judge.score
  (:require clojure.pprint
            judge.criteria
            judge.db
            clojure.string
            clojure.pprint
            noir.session
            noir.response
            [judge.layout :as layout]))

(defn scoring-page []
  (let [user (noir.session/get :user)
        not-used (judge.db/unassign-judge-from-any-students! judge.db/db-spec user)
        student-record (first (shuffle (judge.db/who-can-i-judge judge.db/db-spec user)))]
    (judge.db/assign-judge-to-student! judge.db/db-spec user (:name student-record))
    ;; if in K 1 2, they have a choice of project
    (if (some #(= (:grade student-record) %) ["K" "1" "2"])
      (layout/render-style "unknown-project-type.html" student-record)
      (layout/render-style "scoring.html" (conj {:project-type :experimental :criteria (judge.criteria/make-criteria false)} student-record)))))

;      (noir.response/redirect (str "/score-by-name?name=" (java.net.URLEncoder/encode (:name student-record)) "&type=e")))))


(defn scoring-page-by-name [name type]
  (let [; user (noir.session/get :user)
        student-record (first (judge.db/get-student-by-name judge.db/db-spec name))
        project-type (if (= "i" type) :informational :experimental)
        criteria (judge.criteria/make-criteria (= "i" type))]
    ;  (clojure.pprint/pprint criteria)
    (layout/render-style "scoring.html" (conj {:project-type project-type :criteria criteria} student-record))))


(defn unpack-score [score-raw]
  (. Float parseFloat
     (if (instance? String score-raw)
       score-raw
       (first score-raw))))

(defn score-entry [score-name scores]
  (let [matching-entry (first (filter #(= score-name (first %)) scores))]
    [score-name (if matching-entry (second matching-entry) 0)]))

;(defn translate-front [[x y]]
;  (str (case x
;    :visual "Visual "
;    :oral "Oral "
;    ""
;  ) y ))

;(defn scores-add-in-missing [scores is-informational]
;  (let [criteria (judge.criteria/make-criteria is-informational)
;        criteria-names (map #(translate-front %) criteria)]
;    (println "scores-add-in-missing informatioal " is-informational)
;    (map #(score-entry % scores) criteria-names )
;    )
;  )


;score-list (scores-add-in-missing score-list-missing-zeros (= ":informational" (:project-type args)) )
;just-scores (map #(second %) score-list)
;total-score (format "%.3f" (reduce + just-scores))
;]


(defn extract-score [key scores]
  (let [found-score (second (first (filter #(= key (first %)) scores)))]
    (if (nil? found-score)
      0.0
      found-score)))

; Checks are strange.  The total checks for a section add up to 20 points.
(defn extract-checks [key criteria scores]
  (let [check-criteria (filter #(= key (first %)) criteria)
        check-names (map #(str (clojure.string/capitalize (name key)) " " (second %)) check-criteria)
        scores-extracted (map #(extract-score % scores) check-names)
        score (* 20 (/ (reduce + scores-extracted) (count scores-extracted)))]
    score))

(defn compute-final-summary [scores is-informational]
  (let [criteria (judge.criteria/make-criteria is-informational)
        visual-checks (extract-checks :visual criteria scores)
        visual-appearance (extract-score "Visual Score" scores)
        oral-checks (extract-checks :oral criteria scores)
        oral-clarity (extract-score "Oral Score" scores)
        scientific-subject (extract-score "Scientific Subject Score" scores)
        overall-effort (extract-score "Overall Effort Multiplier" scores)

        total-score (* overall-effort (+ visual-checks visual-appearance oral-checks oral-clarity scientific-subject))
        scores [["Visual Checks" visual-checks]
                ["Visual Score" visual-appearance]
                ["Oral Checks" oral-checks]
                ["Oral Score" oral-clarity]
                ["Scientific Subject Score" scientific-subject]
                ["Overall Effort Multiplier" overall-effort]]]
    ;(println "total-score" total-score "from scores" scores)
    [total-score scores]))

(defn score-post [args]
  (let [student-record (first (judge.db/get-student-by-name judge.db/db-spec (:name args)))
        scores (dissoc args :name :project-type)
        scores-unpacked (into [] (map #(vector (name (first %)) (unpack-score (second %))) scores))
        [total-score massaged-scores] (compute-final-summary scores-unpacked (= ":informational" (:project-type args)))
        formatted-total (format "%.3f" total-score)]

    (layout/render-style "scoring-summary.html" (conj {:project-type (:project-type scores) :criteria massaged-scores :total-score formatted-total} student-record))))


(defn score-approved [args]
  (let [judge (noir.session/get :user)
        scores (dissoc args :name :project-type)
        score-list (into [] (map #(vector (name (first %)) (unpack-score (second %))) scores))
        just-scores (map #(second %) score-list)
        total-score (reduce + just-scores)
        student-record (first (judge.db/get-student-by-name judge.db/db-spec (:name args)))]
    (if (= judge (:being_judged_by student-record))
      (doall
       (judge.db/insert-scores! (:name args) judge score-list total-score)
       (noir.response/redirect "/begin"))
      (layout/render-style "scoring-error.html" {} ))))


(defn cancel [args]
  (judge.db/assign-judge-to-student! judge.db/db-spec nil (:n args))
  (noir.response/redirect "/begin"))