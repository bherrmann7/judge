(ns judge.score-test
  (:use clojure.test
        ring.mock.request
        judge.handler)
  (:require clojure.pprint
            judge.score))

;(deftest test-app
;  (testing "computing score"
;    (let [[total-score summary] (judge.score/compute-final-summary [["Scientific Subject" 4.0]] true)]
;      (is (= 4.0 total-score))
;      )))


(deftest test-app

  (testing "computing score for empty informational.  Is 2 because there are only 3 checks in Oral part"
    (let [[total-score summary] (judge.score/compute-final-summary [["Overall Effort" 1]] true)]
      (is (= 2.0 total-score))))

  (testing "multiplier 2 * 1.5 = 3t"
    (let [[total-score summary] (judge.score/compute-final-summary [["Overall Effort" 1.5]] true)]
      (is (= 3.0 total-score))))

  (testing "Perfect Informational Project"
    (let [[total-score summary]
          (judge.score/compute-final-summary [["Visual Title, Name, School, Grade" 1.0]
                                              ["Visual 2 Resources" 1.0]
                                              ["Visual Topic / questions explanation" 1.0]
                                              ["Visual Pictures / models / collections" 1.0]
                                              ["Visual Conclusion" 1.0]
                                              ["Visual Appearance" 5.0]
                                              ["Oral Written information explanation" 1.0]
                                              ["Oral Pictures / models / collections" 1.0]
                                              ["Oral Conclusion" 1.0]
                                              ["Oral Clarity" 5.0]
                                              ["Scientific Subject" 5.0]
                                              ["Overall Effort" 1.5]] true)]
      (is (= 37.5 total-score))))

  (testing "Perfect Experimental Project"
    (let [[total-score summary]
          (judge.score/compute-final-summary [["Visual Title, Name, School, Grade" 1]
                                              ["Visual 2 Resources" 1.0]
                                              ["Visual Purpose" 1.0]
                                              ["Visual Hypothesis / prediction" 1.0]
                                              ["Visual Test setup" 1.0]
                                              ["Visual Test procedure" 1.0]
                                              ["Visual Variables" 1.0]
                                              ["Visual Test data" 1.0]
                                              ["Visual Test results in graph/chart" 1.0]
                                              ["Visual Conclusion" 1.0]
                                              ["Visual Appearance" 5.0]
                                              ["Oral Purpose" 1.0]
                                              ["Oral Hypothesis / prediction" 1.0]
                                              ["Oral Test setup" 1.0]
                                              ["Oral Test procedure" 1.0]
                                              ["Oral Variables" 1.0]
                                              ["Oral Test data" 1.0]
                                              ["Oral Test results in graph/chart" 1.0]
                                              ["Oral Conclusion" 1.0]
                                              ["Oral Clarity" 5.0]
                                              ["Scientific Subject" 5.0]
                                              ["Overall Effort" 1.5]] false)
          highest-possible (* 1.5 ; Overall Effort
                              (+ 5 ; Visual Checks
                                 5 ; Visual Appearence
                                 5 ; Oral Checks
                                 5 ; Oral Clarity
                                 5; Scientific Subject
))]      

      (is (= highest-possible total-score))
      (is (= 37.5 highest-possible))))


  (testing "Informational Project -  Ding Resources, and flat effort."
    (let [[total-score summary]
          (judge.score/compute-final-summary [["Visual Title, Name, School, Grade" 1.0]
                                             ;  ["Visual 2 Resources" 1.0]
                                              ["Visual Topic / questions explanation" 1.0]
                                              ["Visual Pictures / models / collections" 1.0]
                                              ["Visual Conclusion" 1.0]
                                              ["Visual Appearance" 5.0]
                                              ["Oral Written information explanation" 1.0]
                                              ["Oral Pictures / models / collections" 1.0]
                                              ["Oral Conclusion" 1.0]
                                              ["Oral Clarity" 5.0]
                                              ["Scientific Subject" 5.0]
                                              ["Overall Effort" 1.0]] true)]
      (is (= 24.0 total-score)))))
