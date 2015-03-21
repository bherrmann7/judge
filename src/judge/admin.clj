(ns judge.admin
  (:require clojure.pprint
            [judge.layout :as layout]))

(def site-pass (.trim (slurp "/judge-data/judge.db.pass")))

(defn login [req]
    (layout/render "/admin/login.html")
)

(defn login-post [req]
;    (if (= (:password req) site-pass))
)

(defn admin-page [req]
  (if (get-in req [:session :admin])
    (layout/render "/admin/home.html" {:headers ["Name" "Total count" "Kindergarden" "First" "Second" "Third" "Fourth"]
                                     :rows    (concat
                                               (judge.db/judge-summary judge.db/db-spec)
                                               (judge.db/student-summary judge.db/db-spec)
                                               (judge.db/judgement-summary judge.db/db-spec))}))
   (layout/render "/admin/login.html"))

(defn judges-page []
  (prn (judge.db/all-judges judge.db/db-spec))
  (layout/render "/admin/judges.html"  {:headers ["Name" "Grade" "Currently Judging"]
                                        :rows   (judge.db/all-judges judge.db/db-spec)}))

(defn students-page []
  (layout/render "/admin/students.html" {:headers ["Name" "Name" "Table Assignment" "Grade" "Position" "Checked In" "Being Judged By"]
                                         :rows (judge.db/all-students judge.db/db-spec)}))

(defn judgements-page []
  (layout/render "/admin/judgements.html"
                 {:headers ["Student" "Judged Count" "Judged By" "Currently Judging"]
                  :rows    (judge.db/all-summary judge.db/db-spec)}))


(defn awards-page []
  (layout/render "/admin/awards.html"))

