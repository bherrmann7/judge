(ns judge.admin
  (:require clojure.pprint
            [selmer.parser]
            [judge.db :as db]
            [judge.layout :as layout]))

(def site-pass (.trim (slurp "/judge-data/adminpass.txt")))

(defn login [more]
  (layout/render "/admin/login.html" (conj {:dont-show-logout-button true} more)))


(defn admin-check [fn req]
  (if (noir.session/get-in [:admin])
    (fn req)
    (login nil)))

(defn admin-page [req]
  (let [checked-in-student (noir.session/get-in [:flash-student])]
    (noir.session/assoc-in! [:flash-student] nil)
    (layout/render "/admin/home.html" {:headers                 ["Name" "Total count" "Kindergarden" "First" "Second" "Third" "Fourth"]
                                       :rows                    (concat
                                                                  (judge.db/judge-summary judge.db/db-spec)
                                                                  (judge.db/student-summary judge.db/db-spec)
                                                                  (judge.db/judgement-summary judge.db/db-spec))
                                       :students-not-checked-in (judge.db/students-not-checked-in judge.db/db-spec)
                                       :checked-in-student      checked-in-student
                                       }))
  )
(defn login-post [req]
  (let [password (str (:password (:params req)))]
    (if (= password site-pass)
      (do
        (noir.session/assoc-in! [:admin] true)
        (admin-page req))
      (login {:error "Bad Password"}))))

(defn judges-page [req]
  (layout/render "/admin/judges.html" {:headers ["Name" "Grade" "Currently Judging"]
                                       :rows    (judge.db/all-judges judge.db/db-spec)}))

(defn students-page [req]
  (layout/render "/admin/students.html" {:headers ["Name" "Name" "Table Assignment" "Grade" "Position" "Checked In" "Being Judged By"]
                                         :rows    (judge.db/all-students judge.db/db-spec)}))

(defn judgements-page [req]
  (layout/render "/admin/judgements.html"
                 {:headers ["Student" "Judged Count" "Judged By" "Currently Judging"]
                  :rows    (judge.db/all-summary judge.db/db-spec)}))



(selmer.parser/add-tag! :random-color
                        (fn [args context-map]
                          (first (shuffle ["white" "lightpink" "lightblue" "lightgreen"]))))

(selmer.parser/add-tag! :random-student
                        (fn [args context-map]
                          (first (shuffle ["TM" "LH/MK" "TR" "DY" "ER" "AF"]))))

(selmer.parser/add-tag! :random-judge
                        (fn [args context-map]
                          (first (shuffle ["" "" "" "" "" "" "" "" "" "" "" "" "" ""
                                           "BK" "JS" "RK"]))))

(defn floor [req]
  (layout/render "/admin/floor.html" {:tables      {:row1
                                                    [["X" "W"] ["V" "U"] ["T" "S"] ["R" "Q"] ["P" "O"] ["N" "M"]]
                                                    :row2
                                                    [["L" "K"] ["J" "I"] ["H" "G"] ["F" "E"] ["D" "C"] ["B" "A"]]}
                                      :four-to-one (reverse (range 1 5))}))

(defn student-checkin [req]
  (let [
        student-name (:student-name (:params req))
        checked-in-student (first (judge.db/get-student-by-name judge.db/db-spec student-name))]
    (judge.db/checkin-student! judge.db/db-spec student-name)
    (noir.session/assoc-in! [:flash-student] checked-in-student)
    (noir.response/redirect "/a")))

(defn awards-page [req]
  (let [highest-scores (judge.db/get-hightest judge.db/db-spec)]
    (layout/render "/admin/awards.html" {
                                         :awards

                                         })))

