(defproject leiningen-semantic-release-test-clojars "1.0.2"
  :description "A hello world project to test the leiningen-semantic-release plugin"
  :url "https://github.com/NoxHarmonium/leiningen-semantic-release-test-clojars"
  :scm {:name "git"
        :url "https://github.com/NoxHarmonium/leiningen-semantic-release-test-clojars.git"}
  :license {:name "Apache License"
            :url  "http://www.apache.org/licenses/LICENSE-2.0"}
  :deploy-branches ["master"]
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :repl-options {:init-ns leiningen-semantic-release-test-clojars.core}
  ; Not strictly neccessary but helps to ensure another random key is not picked up
  ; or if the default key is nor set
  ; You can also use the key uid
  :signing {:gpg-key "95DD278234D7B5F9FC0F3A4BFE2561D1B1685404"}
  ; Although this example uses clojars, you shoul be able to change the URL to maven central or a private repo and it will still work.
  ; The :env symbol delegates the value to an environment variable.
  ; In this case the username, password and GPG private key passphrase will come from LEIN_USERNAME, LEIN_PASSWORD and LEIN_PASSPHRASE respectively
  :deploy-repositories [["releases" {:url "https://repo.clojars.org" :username :env :password :env}]
                        ["snapshots" {:url "https://repo.clojars.org" :username :env :password :env}]])
