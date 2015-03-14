(ns judge.score
  (:require clojure.pprint
            judge.criteria
            judge.db
            [judge.layout :as layout]))

(defn scoring-page []
  (let [user (noir.session/get :user)
        student-record (first (shuffle (judge.db/who-can-i-judge judge.db/db-spec user)))]
    ;; if in K 1 2, they have a choice of project
    (judge.db/assign-judge-to-student! judge.db/db-spec user (:name student-record) )
    (if (some #(= (:grade student-record) %) ["K" "1" "2"])
      (layout/render "unknown-project-type.html" student-record)
      (noir.response/redirect (str "/score-by-name?name=" (java.net.URLEncoder/encode (:name student-record)) "&type=e")))))

(defn scoring-page-by-name [name type]
  (let [
         ; user (noir.session/get :user)
         student-record (first (judge.db/get-student-by-name judge.db/db-spec name))
         project-type (if (= "i" type) :informational :experimental)
         criteria (judge.criteria/make-criteria (= "i" type))
         ]
    (clojure.pprint/pprint criteria)
    (layout/render "scoring.html" (conj {:project-type project-type :criteria criteria} student-record))))


(defn unpack-score [score-raw]
    (. Float parseFloat
      (if (instance? String score-raw)
        score-raw
        (first score-raw))))

(defn score-entry [score-name scores]
  (let [matching-entry (first (filter #(= score-name (first %)) scores ) )]
  [ score-name (if matching-entry (second matching-entry)  0 ) ]
  ))

(defn translate-front [[x y]]
  (str (case x
    :visual "Visual "
    :oral "Oral "
    ""
  ) y ))

(defn scores-add-in-missing [scores is-informational]
  (let [criteria (judge.criteria/make-criteria is-informational)
        criteria-names (map #(translate-front %) criteria)]
    (println "scores-add-in-missing informatioal " is-informational)
    (map #(score-entry % scores) criteria-names )
    )
  )

(defn score-post [args]
  (let [student-record (first (judge.db/get-student-by-name judge.db/db-spec (:name args)))
        scores (dissoc args :name :project-type)
        score-list-missing-zeros (into [] (map #(vector (name (first %)) (unpack-score (second %)) ) scores))
        score-list (scores-add-in-missing score-list-missing-zeros (= ":informational" (:project-type args)) )
        just-scores (map #(second %) score-list)
        total-score (reduce + just-scores)
        ]
    (println "project-type"  (:project-type args)  (= ":informational" (:project-type args)) )
    (layout/render "scoring-summary.html" (conj {:project-type (:project-type scores) :criteria score-list :total-score total-score} student-record)))
  )


(defn score-approved [args]
  (let [judge (noir.session/get :user)
        scores (dissoc args :name :project-type)
        score-list (into [] (map #(vector (name (first %)) (unpack-score (second %)) ) scores))
        just-scores (map #(second %) score-list)
        total-score (reduce + just-scores)]
        (judge.db/insert-scores! (:name args) judge score-list total-score)
    (noir.response/redirect "/begin")

  ))

(defn cancel [args]
  (judge.db/assign-judge-to-student! judge.db/db-spec nil (:n args) )
  (noir.response/redirect "/begin")
  )