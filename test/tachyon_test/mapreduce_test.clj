(ns tachyon-test.mapreduce-test
  (:use [tachyon-test.config :as config])
  (:require [clojure.test :refer :all]
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
      (print result)
      (is (= (:exit result) 0))
      (with-open [fs (create-filesystem)]
        (let [root (.getFile fs path)]
           (is (.isDirectory root))
           (is (.isComplete root)))
        (let [suc (.getFile fs (str path "/_SUCCESS"))]
           (is (.isFile suc))
           (is (.isComplete suc)))
        (let [part (.getFile fs (str path "/part-m-00000"))]
           (is (.isFile part))
           (is (.isComplete part)))
        )))
