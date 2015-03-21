(ns judge.admin
  (:require clojure.pprint
            [judge.layout :as layout]))

(def site-pass (.trim (slurp "/judge-data/adminpass.txt")))

(defn login [req & more]
    (layout/render "/admin/login.html" more )
)

(defn login-post [req password]
  (if (and (not (clojure.string/blank? password))
           (= password (.trim (slurp "/judge-data/adminpass.txt"))))
    (noir.session/assoc-in! [:admin] true))
  (login

(defn admin []
  (if (noir.session/get-in [:admin])
    (layout/render "/admin/students.html" {:students (db/get-students)})
    (layout/render "/admin/login.html")))

(defn adults-get []
  (if-not (noir.session/get-in [:admin])
    (layout/render "/admin/login.html")
    (let [adults (db/get-adults)
          adults-with-link (map #(assoc % :email-link (util/make-email-link (:email %) )) adults )
          ]
      (layout/render "/admin/adults.html" {:adults adults-with-link}))))


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

