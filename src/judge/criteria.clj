(ns judge.criteria
  (:require clojure.pprint))

(def informational-checklists {:visual ["Title, Name, School, Grade"
                                        "Resources (2 minimum)"
                                        "Topic/Question"
                                        "Pictures / Models / Collections"
                                        "Conclusion"]
                               :oral ["Topic/Question"
                                      "Written Information Explanation"
                                      "Pictures / Models / Collections"
                                      "Conclusion"]})

(def experimental-checklists {:visual ["Name, School, Grade"
                                       "Resources (2 minimum)"
                                       "Title and Purpose"
                                       "Hypothesis / Prediction"
                                       "Test Setup"
                                       "Test Procedure"
                                       "Test Variables"
                                       "Test Data"
                                       "Test Results in Graph/Chart"
                                       "Conclusion"]
                              :oral ["Title and Purpose"
                                     "Hypothesis / Prediction"
                                     "Test Setup"
                                     "Test Procedure"
                                     "Test Variables"
                                     "Test Data"
                                     "Test Results in Graph/Chart"
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
    (de-scrunk [(make-checks :visual checklists)
                [:score0-20 "Visual Score" "Appearence (neat, organized, visually appealing, legible, etc...)"]
                (make-checks :oral checklists)
                [:score0-20 "Oral Score" "Clarity (eye contact, clear vocals, follows logical path, incorporates visual presentation)"]
                [:score0-20 "Scientific Subject Score" "Was scientific purpose clear?"]
                [:score1-1.5 "Overall Effort Multiplier" "how much effort across the project lifecycle?"]])))

