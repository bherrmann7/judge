(ns judge.admin
  (:require clojure.pprint
            [judge.layout :as layout]
            )
  )


(defn admin-page []
  (layout/render "admin.html" {:headers ["Name"  "Total count" "Kindergarden" "First" "Second" "Third" "Fourth"]
                               :rows (judge.db/judge-summary judge.db/db-spec)}))
