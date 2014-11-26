(defproject tachyon-test "0.6.0-SNAPSHOT"
  :description "Automation tests for testing a Tachyon cluster"
  :url "http://github.com/dcapwell/tachyon-test"
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :jvm-opts ["-Xmx1g"]
  :plugins [[lein-test-out "0.3.1"]]
  :dependencies [
    [org.clojure/clojure "1.5.1"]
    [org.tachyonproject/tachyon "0.6.0-SNAPSHOT"]])
