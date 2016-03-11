(ns judge.layout
  (:require [selmer.parser :as parser]
            [clojure.string :as s]
            [ring.util.response :refer [content-type response]]
            [compojure.response :refer [Renderable]]
            [environ.core :refer [env]]))

(def template-path "templates/")

(deftype RenderableTemplate [template params]
  Renderable
  (render [this request]
    (content-type
     (->> (assoc params
                 :hyper-active (keyword (s/replace template #".html" "-selected"))
                 (keyword (s/replace template #".html" "-selected")) "active"
                 :dev (env :dev)
                 :servlet-context
                 (if-let [context (:servlet-context request)]
                    ;; If we're not inside a serlvet environment (for
                    ;; example when using mock requests), then
                    ;; .getContextPath might not exist
                   (try (.getContextPath context)
                        (catch IllegalArgumentException _ context))))
          (parser/render-file (str template-path template))
          response)
     "text/html; charset=utf-8")))

(defn render [template & [params]]
  (RenderableTemplate. template params))

(defn render-style [html-file attr-map]
  (let [chosen-style (noir.session/get :style)]
    (if (and (not (= html-file "home.html")) (= "plain" chosen-style))
      (render (str "plain/" html-file) (assoc attr-map :style "plain"))
    (if (and (not (= html-file "home.html")) (= "bootstrap" chosen-style))
      (render (str "bootstrap/" html-file) (assoc attr-map :style "bootstrap"))
      (render html-file attr-map)
      ))
    ))
