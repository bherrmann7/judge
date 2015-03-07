(defproject judge "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [lib-noir "0.9.4"]
                 [ring-server "0.3.1"]
                 [selmer "0.7.9"]
                 [com.taoensso/timbre "3.3.1"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.58"
                  :exclusions [com.keminglabs/cljx]]
                 [environ "1.0.0"]
                 [im.chit/cronj "1.4.3"]
                 [noir-exception "0.2.3"]
                 [prone "0.6.0"]
                 [yesql "0.4.0"]
                 [mysql/mysql-connector-java "5.1.32"]
                 ]

  :repl-options {:init-ns judge.repl}
  :jvm-opts ["-server"]
  :plugins [[lein-ring "0.8.13"]
            [lein-environ "1.0.0"]
           ; [lein-ancient "0.5.5"]
            [lein-cljfmt "0.1.4"]
            ]
  :ring {:handler judge.handler/app
         :init    judge.handler/init
         :destroy judge.handler/destroy}
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             :aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.1"]
                        [pjstadig/humane-test-output "0.6.0"]
                        [ring-serve "0.1.2"]
                        ]
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}}
  :min-lein-version "2.0.0")