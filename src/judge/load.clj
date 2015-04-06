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
        students-simpler (map #(vector (nth % 0) (.toUpperCase(nth % 3)) (nth % 1) (str (first (nth % 4))) (str (second (nth % 4))) ) students)]
    (doseq [[name grade partner table_assignment position ] (group-students-with-partners students-simpler)]
      (prn  "name=" name "table=" table_assignment "grade= " grade "position=" position "partern=" partner  )
      (db/insert-student! db/db-spec name table_assignment grade position partner  )))

  (let [judges (rest (with-open [in-file (io/reader "/judge-data/judges.csv")] (doall (csv/read-csv in-file))))]
    (doseq [row judges]
      (if (not (empty? (.trim (second row))))
        (db/insert-judge! db/db-spec (second row) (nth row 6) ))))

  (noir.response/redirect "/"))

