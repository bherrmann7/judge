(ns judge.db)

(require '[yesql.core :refer [defqueries]])

(def db-spec {; :classname "org.postgresql.Driver"
              :subprotocol "mysql"
              :subname     "//localhost/judge"
              :user        (.trim (slurp "/judge-data/judge.db.user"))
              :password    (.trim (slurp "/judge-data/judge.db.pass"))})


(defqueries "judge.sql")


