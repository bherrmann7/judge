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

  (testing "Validate visual information checks"
    (let [[total-score summary] (judge.score/compute-final-summary [
                                                                     ["Overall Effort" 1]

                                                                     ["Visual Title, Name, School, Grade" 1.0]
                                                                     ["Visual 2 Resources" 1]
                                                                     ["Visual Topic / questions explanation" 1]
                                                                     ["Visual Pictures / models / collections" 1]
                                                                     ["Visual Conclusion" 1]
                                                                     ] true)]
      (is (= 20.0 total-score))))

  (testing "Validate oral information checks"
    (let [[total-score summary] (judge.score/compute-final-summary [
                                                                     ["Overall Effort" 1]

                                                                     ["Oral Written information explanation" 1]
                                                                     ["Oral Pictures / models / collections" 1]
                                                                     ["Oral Conclusion" 1]

                                                                     ] true)]
      (is (= 20.0 total-score))))

  (testing "nothing * 1.5 = 0"
    (let [[total-score summary] (judge.score/compute-final-summary [["Overall Effort" 1.5]] true)]
      (is (= 0.0 total-score))))

  (testing "Perfect Informational Project"
    (let [[total-score summary]
          (judge.score/compute-final-summary [["Visual Title, Name, School, Grade" 1.0]
                                              ["Visual 2 Resources" 1.0]
                                              ["Visual Topic / questions explanation" 1.0]
                                              ["Visual Pictures / models / collections" 1.0]
                                              ["Visual Conclusion" 1.0]
                                              ["Visual Appearance" 20.0]
                                              ["Oral Written information explanation" 1.0]
                                              ["Oral Pictures / models / collections" 1.0]
                                              ["Oral Conclusion" 1.0]
                                              ["Oral Clarity" 20.0]
                                              ["Scientific Subject" 20.0]
                                              ["Overall Effort" 1.5]] true)]
      (is (= 150.0 total-score))))

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
                                              ["Visual Appearance" 20.0]
                                              ["Oral Purpose" 1.0]
                                              ["Oral Hypothesis / prediction" 1.0]
                                              ["Oral Test setup" 1.0]
                                              ["Oral Test procedure" 1.0]
                                              ["Oral Variables" 1.0]
                                              ["Oral Test data" 1.0]
                                              ["Oral Test results in graph/chart" 1.0]
                                              ["Oral Conclusion" 1.0]
                                              ["Oral Clarity" 20.0]
                                              ["Scientific Subject" 20.0]
                                              ["Overall Effort" 1.5]] false)
          highest-possible (* 1.5 ; Overall Effort
                              (+ 20 ; Visual Checks
                                 20 ; Visual Appearence
                                 20 ; Oral Checks
                                 20 ; Oral Clarity
                                 20; Scientific Subject
))]      

      (is (= highest-possible total-score))
      (is (= 150.0 highest-possible))))


  (testing "Informational Project -  Ding Resources, and flat effort."
    (let [[total-score summary]
          (judge.score/compute-final-summary [["Visual Title, Name, School, Grade" 1.0]
                                             ;  ["Visual 2 Resources" 1.0]
                                              ["Visual Topic / questions explanation" 1.0]
                                              ["Visual Pictures / models / collections" 1.0]
                                              ["Visual Conclusion" 1.0]
                                              ["Visual Appearance" 20.0]
                                              ["Oral Written information explanation" 1.0]
                                              ["Oral Pictures / models / collections" 1.0]
                                              ["Oral Conclusion" 1.0]
                                              ["Oral Clarity" 20.0]
                                              ["Scientific Subject" 20.0]
                                              ["Overall Effort" 1.0]] true)]
      (is (= (- 100.0 4) total-score)))))
