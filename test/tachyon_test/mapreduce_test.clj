(ns tachyon-test.mapreduce-test
  (:use [tachyon-test.config :as config])
  (:require [clojure.test :refer :all]
            [tachyon-test.core :refer :all]
            [tachyon-test.mapreduce :refer :all]))

(deftest randomtextwriter
  (testing "Runs the randomtextwriter MapReduce job against tachyon")
    (let [result (hadoop-example "randomtextwriter" ["-libjars" (:tachyon-client-jar config)
                                                     "-Dmapreduce.randomtextwriter.totalbytes=1024" 
                                                     "-Dmapreduce.randomtextwriter.bytespermap=1024" 
                                                     (str (config :master-address) (uniq-path "/randomtextwriter/"))]
                                 :env {"HADOOP_CLASSPATH" (:tachyon-client-jar config)})]
      (print result)
      (is (= (:exit result) 0))))
