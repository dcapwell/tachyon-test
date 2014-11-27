(ns tachyon-test.mapreduce-test
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :as log]
            [tachyon-test.config :refer [config]]
            [tachyon-test.core :refer :all]
            [tachyon-test.tachyon :refer :all]
            [tachyon-test.mapreduce :refer :all]))


(deftest randomtextwriter
  (testing "Runs the randomtextwriter MapReduce job against tachyon")
    (let [path (str (:master-address config) (uniq-path "/randomtextwriter/"))
          result (hadoop-example "randomtextwriter" ["-libjars" (:tachyon-client-jar config)
                                                     "-Dmapreduce.randomtextwriter.totalbytes=1024" 
                                                     "-Dmapreduce.randomtextwriter.bytespermap=1024" 
                                                    path]
                                 :env {"HADOOP_CLASSPATH" (:tachyon-client-jar config)})]
      (log/info "Job completed:" result)
      (is (= (:exit result) 0))
      (with-open [fs (create-filesystem)]
        (->> (.getFile fs path)
             (is-dir)
             (is-not-file)
             (is-complete))
        (->> (.getFile fs (str path "/_SUCCESS"))
             (is-file)
             (is-not-dir)
             (is-complete))
        (->> (.getFile fs (str path "/part-m-00000"))
             (is-file)
             (is-not-dir)
             (is-complete)))))
