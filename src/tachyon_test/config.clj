(ns tachyon-test.config
  (:require [me.raynes.fs :as fs]))

(defn require-env
  "Verifies that the environment variable is set and returns the current value"
  [name]
  (let [v (java.lang.System/getenv name)]
    (if (nil? v)
      (throw (java.lang.AssertionError. (str "Required " name " environment variable")))
      v)))

(defmulti find-file (fn [base regex] (class regex)))
(defmethod find-file java.util.regex.Pattern [base regex]
  (first (fs/find-files base regex)))
(defmethod find-file java.lang.String [base regex]
  (find-file base (java.util.regex.Pattern/compile regex)))

(def config {
  :master-address "tachyon://localhost:19998"
  :tachyon-home (require-env "TACHYON_HOME")
  :tachyon-client-jar (.getAbsolutePath (find-file (require-env "TACHYON_HOME") "tachyon-client-.*-jar-with-dependencies.jar"))
  :hadoop-home (require-env "HADOOP_HOME")
  :mapred-home (require-env "HADOOP_MAPRED_HOME")
  :hadoop-examples-jar (.getAbsolutePath (find-file (require-env "HADOOP_MAPRED_HOME") "hadoop-mapreduce-examples-.*.jar"))
  :base-path "/tachyon-test"
  :default-data-size 512
})

