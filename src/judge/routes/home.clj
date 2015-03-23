(ns judge.routes.home
  (:require [compojure.core :refer :all]
            [noir.session]
            [noir.response]
            [judge.db]
            [judge.admin :as ad]
            [judge.layout :as layout]
            [judge.util :as util]
            [judge.score :as score]
            judge.load
            [compojure.route :as route]))

(defn home-page []
  (layout/render
   "home.html" {:message (noir.session/flash-get :message) :judges (judge.db/all-judges judge.db/db-spec)}))

(defn login-page [judge-name password]
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
  {:user        user
   :you-judged  (:judged (first (judge.db/you-judged judge.db/db-spec user)))
   :could-judge (count (judge.db/who-can-i-judge judge.db/db-spec user))
   :stats       (first (judge.db/judge-stats-summary judge.db/db-spec user))})

(defn begin-page []
  (let [user (noir.session/get :user)
        not-used (judge.db/unassign-judge-from-any-students! judge.db/db-spec user)]
    (if (nil? user)
      (noir.response/redirect "/")
      (layout/render "begin.html" (begin-stats user)))))


(defn logout [where]
  (noir.session/clear!)
  (noir.response/redirect where))

(defroutes home-routes
  (GET "/logout" [] (logout "/"))
  (GET "/score-by-name" [name type] (score/scoring-page-by-name name type))
  (GET "/score" [] (score/scoring-page))
  (POST "/score-approved" [& args] (score/score-approved args))
  (GET "/begin" [] (begin-page))
  (POST "/scored" [& args] (score/score-post args))
  (GET "/" [] (home-page))
  (POST "/" [judge-name password] (login-page judge-name password))
  (GET "/about" [] (about-page))
  (GET "/cancel" [& args] (judge.score/cancel args))

  (GET "/a/login.html" req (ad/login req))
  (POST "/a" req (ad/login-post req))
  (GET "/a/logout" [] (logout "/a"))

  ; This checking really shoud be done in the middle where on uri /a and /a/*
  (GET "/a" req (ad/admin-check ad/admin-page req))
  (GET "/a/judges" req (ad/admin-check ad/judges-page req))
  (GET "/a/students" req (ad/admin-check ad/students-page req))
  (GET "/a/judgements" req (ad/admin-check ad/judgements-page req))
  (GET "/a/awards" req (ad/admin-check judge.admin/awards-page req))
  (GET "/a/reload" req (ad/admin-check judge.load/confirm req))
  (POST "/a/reload" req (ad/admin-check judge.load/reload-judges-students req))

  (route/not-found "<h1>I'm verry sorry, but page not found</h1>"))
