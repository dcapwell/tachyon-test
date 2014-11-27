(ns tachyon-test.mapreduce
  (:use [clojure.java.shell :only [sh]]
        [tachyon-test.config :as config]
        [tachyon-test.shell :as shell])
  (:import [java.lang System]))

(def hadoop-bin (str (:hadoop-home config) "/bin/hadoop"))
(def hadoop-example-jar (:hadoop-examples-jar config))

(defn hadoop-example
  "Runs a hadoop-example MapReduce job."
  [prog args & {:keys [env inheritIO timeout time-unit] :or {env (System/getenv) timeout 10 time-unit java.util.concurrent.TimeUnit/MINUTES inheritIO true}}]
  (wait-for
    (shell/fork (concat [hadoop-bin "jar" hadoop-example-jar prog] args) :env env :inheritIO inheritIO)
    :timeout timeout :time-unit time-unit))

