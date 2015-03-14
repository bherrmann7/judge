(ns judge.routes.home
  (:require [compojure.core :refer :all]
            [noir.session]
            [noir.response]
            [judge.db]
            [judge.admin]
            [judge.layout :as layout]
            [judge.util :as util]
            [judge.score :as score]
            [compojure.route :as route]))

(defn home-page []
  (println "get home called.")
  (layout/render
    "home.html" {:message (noir.session/flash-get :message) :judges (judge.db/all-judges judge.db/db-spec)}))

(defn login-page [judge-name password]
  (println "Login called with " judge-name " " password)
  (if (= "sf" password)
    (do
      (noir.session/assoc-in! [:user] judge-name)
      (noir.response/redirect "/begin"))
    (do
      (noir.session/flash-put! :message "Bad password")
      (noir.response/redirect "/"))))

(defn about-page []
  (layout/render "about.html"))

(defn begin-stats [user]
  {:user user
   :you-judged (:judged (first (judge.db/you-judged judge.db/db-spec user)))
   :could-judge (count (judge.db/who-can-i-judge judge.db/db-spec user))
   :stats (first (judge.db/judge-stats-summary judge.db/db-spec user))
   })

(defn begin-page []
  (let [user (noir.session/get :user)]
    (if (nil? user)
      (noir.response/redirect "/")
      (layout/render "begin.html" (begin-stats user)))))


(defn logout []
  (noir.session/clear!)
  (noir.response/redirect "/"))

(defroutes home-routes
  (GET "/logout" [] (logout))
  (GET "/score-by-name" [name type] (score/scoring-page-by-name name type))
  (GET "/score" [] (score/scoring-page))
  (POST "/score-approved" [& args] (score/score-approved args))
  (GET "/begin" [] (begin-page))
  (POST "/scored" [& args]
    (score/score-post args))
  (GET "/" [] (home-page))
  (POST "/" [judge-name password] (login-page judge-name password))
  (GET "/a" [] (judge.admin/admin-page))
  (GET "/a/judges" [] (judge.admin/judges-page))
  (GET "/a/students" [] (judge.admin/students-page))
  (GET "/a/judgements" [] (judge.admin/judgements-page))
  (GET "/a/awards" [] (judge.admin/awards-page))
  (GET "/about" [] (about-page))
  (GET "/cancel" [& args] (judge.score/cancel args))
  (route/not-found "<h1>I'm verry sorry, but page not found</h1>"))