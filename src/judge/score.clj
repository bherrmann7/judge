(ns judge.score
  (:require clojure.pprint
            judge.criteria
            judge.db
            clojure.string
            noir.session
            noir.response
            [judge.layout :as layout]))

(defn scoring-page []
  (let [user (noir.session/get :user)
        student-record (first (shuffle (judge.db/who-can-i-judge judge.db/db-spec user)))]
    ;; if in K 1 2, they have a choice of project
    (judge.db/assign-judge-to-student! judge.db/db-spec user (:name student-record))
    (if (some #(= (:grade student-record) %) ["K" "1" "2"])
      (layout/render "unknown-project-type.html" student-record)
      (noir.response/redirect (str "/score-by-name?name=" (java.net.URLEncoder/encode (:name student-record)) "&type=e")))))


(defn scoring-page-by-name [name type]
  (let [; user (noir.session/get :user)
        student-record (first (judge.db/get-student-by-name judge.db/db-spec name))
        project-type (if (= "i" type) :informational :experimental)
        criteria (judge.criteria/make-criteria (= "i" type))]
    (clojure.pprint/pprint criteria)
    (layout/render "scoring.html" (conj {:project-type project-type :criteria criteria} student-record))))


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

; Checks are strange.  The total checks for a section can only add up to 5 points.
; so each missing check counts against you, but you cant go negative.  So if out of 10 checked criteria
; you are missing two things, you get a score of 3.    If you are missing 7 things, you get a 0.
(defn extract-checks [key criteria scores]
  (let [check-criteria (filter #(= key (first %)) criteria)
        check-names (map #(str (clojure.string/capitalize (name key)) " " (second %)) check-criteria)
        scores (map #(extract-score % scores) check-names)
        scores-missing (count (filter #(= 0.0 %) scores))
        scores-raw (- 5 scores-missing)
        score (if (< 0 scores-raw)  scores-raw 0)]
    ;(prn "scores" key (count scores) scores scores-missing scores-raw score)
    score))

(defn compute-final-summary [scores is-informational]
  (let [criteria (judge.criteria/make-criteria is-informational)
        visual-checks (extract-checks :visual criteria scores)
        visual-appearance (extract-score "Visual Appearance" scores)
        oral-checks (extract-checks :oral criteria scores)
        oral-clarity (extract-score "Oral Clarity" scores)
        scientific-subject (extract-score "Scientific Subject" scores)
        overall-effort (extract-score "Overall Effort" scores)

        total-score (* overall-effort (+ visual-checks visual-appearance oral-checks oral-clarity scientific-subject))
        scores [["Visual Checks" visual-checks]
                ["Visual Appearance" visual-appearance]
                ["Oral Checks" oral-checks]
                ["Oral Clarity" oral-clarity]
                ["Scientific Subject" scientific-subject]
                ["Overall Effort" overall-effort]]]

    ;(println "total-score" total-score "from scores" scores)
    [total-score scores]))

(defn score-post [args]
  (let [student-record (first (judge.db/get-student-by-name judge.db/db-spec (:name args)))
        scores (dissoc args :name :project-type)
        scores-unpacked (into [] (map #(vector (name (first %)) (unpack-score (second %))) scores))
        [total-score massaged-scores] (compute-final-summary scores-unpacked (= ":informational" (:project-type args)))
        formatted-total (format "%.3f" total-score)]

    (layout/render "scoring-summary.html" (conj {:project-type (:project-type scores) :criteria massaged-scores :total-score formatted-total} student-record))))


(defn score-approved [args]
  (let [judge (noir.session/get :user)
        scores (dissoc args :name :project-type)
        score-list (into [] (map #(vector (name (first %)) (unpack-score (second %))) scores))
        just-scores (map #(second %) score-list)
        total-score (reduce + just-scores)]
    (judge.db/insert-scores! (:name args) judge score-list total-score)
    (noir.response/redirect "/begin")))

(defn cancel [args]
  (judge.db/assign-judge-to-student! judge.db/db-spec nil (:n args))
  (noir.response/redirect "/begin"))