(ns judge.load
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [judge.db :as db]))

(defn confirm [req]
  {:status  200
   :headers {"content-type" "text/html"}
   :body    "<h1>Confirm Reload<h1><form method=POST><input type=submit value=CONFIRM></form>"})


(defn student-in-list? [student-name list]
  (if (empty? list)
    false
    (let [[name grade partner] (first list)]
      (if (= name student-name)
        true
        (recur student-name (rest list))))))

(defn group-students [to-consider finished-list]
  (if (empty? to-consider)
    finished-list
    (let [[name grade partner :as student] (first to-consider)]
      (if (empty? partner)
        (recur (rest to-consider) (conj finished-list student))
        (if (student-in-list? partner finished-list)
          (recur (rest to-consider) finished-list)
          (recur (rest to-consider) (conj finished-list student)))))))

(defn group-students-with-partners [students]
  ; Verify partners match
  (doseq [[name grade partner] students]
    (if (not (empty? partner))
      (if (not (student-in-list? partner students))
        (throw (RuntimeException. (str name " has partner " partner ", but not the reverse."))))))
  (group-students students []))

(defn reload-judges-students [req]

  (db/delete-from-students! db/db-spec)
  (db/delete-from-judges! db/db-spec)
  (db/delete-from-summary! db/db-spec)
  (db/delete-from-judgements! db/db-spec)

  (let [students (rest (with-open [in-file (io/reader "/judge-data/students.csv")] (doall (csv/read-csv in-file))))
        students-simpler (map #(vector (nth % 0) (nth % 2) (nth % 4)) students)]
    (doseq [[name grade partner] (group-students-with-partners students-simpler)]
      (db/insert-student! db/db-spec name (.toUpperCase grade) partner)))

  (let [judges (rest (with-open [in-file (io/reader "/judge-data/judges.csv")] (doall (csv/read-csv in-file))))]
    (doseq [row judges]
      (if (not (empty? (.trim (second row))))
        (db/insert-judge! db/db-spec (second row) (first (shuffle ["K" "1" "2" "3" "4"]))))))


  (db/insert-judge! db/db-spec "Bob Herrmann" "2")
  (db/insert-judge! db/db-spec "Carl Prestia" "3")
  (db/insert-judge! db/db-spec "Deb June" "4")

  (noir.response/redirect "/"))

