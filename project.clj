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
    [org.clojure/tools.logging "0.3.1"]
    [org.clojure/test.check "0.5.9"]
    [clj-ssh "0.5.11"]
    [me.raynes/fs "1.4.6"]
    [ch.qos.logback/logback-classic "1.1.2"]
    [org.slf4j/jcl-over-slf4j "1.7.7"]
    [org.tachyonproject/tachyon-client "0.6.0-SNAPSHOT"
                                        :exclusions [[commons-logging/commons-logging]
                                                     [log4j/log4j]]]]
  :profiles {:experiment {:source-paths ["experiment"]
                          :test-paths ["experiment"]}})
