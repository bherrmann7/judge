(ns judge.admin
  (:require clojure.pprint
            [judge.layout :as layout]
            )
  )

(defn admin-page []
  (layout/render "/admin/home.html" {:headers ["Name" "Total count" "Kindergarden" "First" "Second" "Third" "Fourth"]
                               :rows    (concat
                                              (judge.db/judge-summary judge.db/db-spec)
                                              (judge.db/student-summary judge.db/db-spec)
                                              (judge.db/judgement-summary judge.db/db-spec)
                                              )}))

(defn judges-page []
  (prn (judge.db/all-judges judge.db/db-spec))
  (layout/render "/admin/judges.html"  {
                                                       :headers ["Name" "Grade" "Currently Judging"]
                                                       :rows   (judge.db/all-judges judge.db/db-spec)
                                                       }))

(defn students-page []
  (layout/render "/admin/students.html" {
                                         :headers [ "Name" "Table Assignment" "Grade" "Position" "Being Judged By"]
                                         :rows (judge.db/all-students judge.db/db-spec)
                                         }
  ))

(defn judgements-page []
  (layout/render "/admin/judgements.html"
                 {:headers ["Student" "Judged Count" "Judged By" "Currently Judging"]
                  :rows    (judge.db/all-summary judge.db/db-spec)
                  }))


(defn awards-page []
  (layout/render "/admin/awards.html" )
  )

