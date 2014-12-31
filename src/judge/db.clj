(ns judge.db)

(require '[yesql.core :refer [defqueries]])

(def home-dir (System/getProperty "user.home"))

(def db-spec {; :classname "org.postgresql.Driver"
              :subprotocol "mysql"
              :subname     "//localhost/judge"
              :user        "judge"
              :password    (.trim (slurp (str home-dir "/bin/judge.db.pass")))})


(defqueries "judge.sql")


