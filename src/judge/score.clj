(ns judge.score
  (:require clojure.pprint
            judge.criteria
            [judge.layout :as layout]))

(defn scoring-page []
  (let [user (noir.session/get :user)
        student-record (first (shuffle (judge.db/who-can-i-judge judge.db/db-spec user)))]
    ;; if in K 1 2, they have a choice of project
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
    (. Integer parseInt
      (if (instance? String score-raw)
        score-raw
        (first score-raw))))


(defn score-post [args]
  (let [student-record (first (judge.db/get-student-by-name judge.db/db-spec (:name args)))
        scores (dissoc args :name :project-type)
        score-list (into [] (map #(vector (name (first %)) (unpack-score (second %)) ) scores))
;        score-list (into [] scores)
        just-scores (map #(second %) score-list)
        total-score (reduce + just-scores)
        ]

    (layout/render "scoring-summary.html" (conj {:criteria score-list :total-score total-score} student-record)))
  )


(defn score-approved [args]
  {
    :headers { "Content-Type" "text/html; charset=utf-8" }
    :body (str "Boo hoo " args)
    }

  )