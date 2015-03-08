(ns judge.criteria
  (:require clojure.pprint))

(def informational-checklists {:visual
                               ["Title, Name, School, Grade"
                                "2 Resources"
                                "Topic / questions explanation"
                                "Pictures / models / collections"
                                "Conclusion"]
                               :oral
                               ["Written information explanation"
                                "Pictures / models / collections"
                                "Conclusion"]})

(def experimental-checklists {:visual
                              ["Title, Name, School, Grade"
                               "2 Resources"
                               "Purpose"
                               "Hypothesis / prediction"
                               "Test setup"
                               "Test procedure"
                               "Variables"
                               "Test data"
                               "Test reulsts in graph/chart"
                               "Conclusion"]
                              :oral
                              ["Purpose"
                               "Hypothesis / prediction"
                               "Test setup"
                               "Test procedure"
                               "Variables"
                               "Test data"
                               "Test reulsts in graph/chart"
                               "Conclusion"]})

(defn make-checks [check-type check-list]
  (map #(vector check-type %) (check-type check-list)))

(defn de-scrunk
  ([items] (de-scrunk items []))
  ([items flat]
  (let [f (first items)]
    (if (nil? f)
      flat
    (if (seq? f) (de-scrunk (rest items) (into flat f))
        (de-scrunk (rest items) (conj flat f)))))))


(defn make-criteria [is-informational]
  (let [checklists (if is-informational informational-checklists experimental-checklists)]
    (de-scrunk [(make-checks :visual checklists )
     [:score0-5 "Visual Appearance" "Appearence (neat, organized, visually appealing, legible, etc...)"]
     (make-checks :oral checklists)
     [:score0-5 "Oral Clarity" "Clarity (eye contact, clear vocals, follows logical path, incorporates visual presentation)"]
     [:score0-5 "Scientific Subject" "Was scientific purpose clear?"]
     [:score0-5 "Overall Effort" "how much effort across the project lifecycle?"]])))

