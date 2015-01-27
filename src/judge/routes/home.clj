(ns judge.routes.home
  (:require [compojure.core :refer :all]
            [noir.session]
            [noir.response]
            [judge.db ]
            [judge.layout :as layout]
            [judge.util :as util]))

(defn home-page []
  (println "get home called.")
  (layout/render
    "home.html" {:message (noir.session/flash-get :message) :judges (judge.db/judges-query judge.db/db-spec)}))

(defn login-page [judge-name password]
  (println "Login called with " judge-name " " password)
  (if (= "sf" password)
    (do
      (noir.session/assoc-in! [:user ] judge-name)
      (noir.response/redirect "/begin"))
    (do
      (noir.session/flash-put! :message "Bad password")
      (noir.response/redirect "/"))
    ))

(defn about-page []
  (layout/render "about.html"))


(defn begin-stats [user]
   {
    :user user
    :you-judged (:judged (first (judge.db/you-judged judge.db/db-spec  user )))
    :could-judge (count (judge.db/who-can-i-judge judge.db/db-spec user ))
    }
  )
    ;:judged-once 30
    ;:judged-twice 19
    ;:judged-fully 5 })

(defn begin-page []
  (let [user (noir.session/get :user)]
    (if (nil? user)
      (noir.response/redirect "/")
      (layout/render "begin.html" (begin-stats user)  ))
  ))

(defn judge-page []
  (let [
        user (noir.session/get :user)
        student-record  (first (shuffle (judge.db/who-can-i-judge judge.db/db-spec user) ))
        ]
    (layout/render "judge.html" student-record )))

(defn admin-page []
  (layout/render "admin.html" {:judge_stats [{:count 7 :total 17 }]} ))


(defn judge-post [student problem research hypothesis experiment observations]
  (judge.db/insert-judgements! judge.db/db-spec student (noir.session/get :user) problem research hypothesis experiment observations   )
  (noir.response/redirect "/begin")
  )

(defn logout []
  (noir.session/clear! )
  (noir.response/redirect "/")
  )

(defroutes home-routes
           (GET "/logout" [] (logout))
           (GET "/judge" [] (judge-page))
           (GET "/begin" [] (begin-page))
           (POST "/judge" [student judge problem research hypothesis experiment observations]
                 (judge-post student problem research hypothesis experiment observations))
           (GET "/" [] (home-page))
           (POST "/" [judge-name password] (login-page judge-name password))
           (GET "/a" [] (admin-page ))
           (GET "/about" [] (about-page) ))