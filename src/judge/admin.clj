(ns judge.admin
  (:require clojure.pprint
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
  (layout/render "/admin/home.html" {:headers ["Name" "Total count" "Kindergarden" "First" "Second" "Third" "Fourth"]
                                     :rows    (concat
                                               (judge.db/judge-summary judge.db/db-spec)
                                               (judge.db/student-summary judge.db/db-spec)
                                               (judge.db/judgement-summary judge.db/db-spec))}))
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


(defn awards-page [req]
  (layout/render "/admin/awards.html"))