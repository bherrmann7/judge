(ns judge.db)

(require '[yesql.core :refer [defqueries]])

(def db-spec {; :classname "org.postgresql.Driver"
              :subprotocol "mysql"
              :subname     "//localhost/judge"
              :user        (.trim (slurp "/judge-data/judge.db.user"))
              :password    (.trim (slurp "/judge-data/judge.db.pass"))})

(defqueries "judge.sql")

(defn insert-scores! [student judge scores total]
  (insert-summary!  db-spec student judge total)
  (doseq [[criteria_name score] scores]
    (insert-score!  db-spec student judge criteria_name score))
  (assign-judge-to-student! db-spec nil student))

