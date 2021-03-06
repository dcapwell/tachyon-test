(ns tachyon-test.client
  (:require [clojure.test :refer :all]
            [tachyon-test.config :refer [config]]
            [tachyon-test.core :refer :all]
            [tachyon-test.tachyon :refer :all])
  (:import [tachyon TachyonURI]
           [tachyon.client WriteType ReadType]
           [com.google.common.io ByteStreams]))

(deftest missing-file
  (testing "Verifies that missing files return null"
    (with-open [fs (create-filesystem)]
      (let [file (.getFile fs "/path/to/non/existing/file")]
        (is (nil? file) "Files that don't exist yet are expected to return null to caller")))))

(deftest add-file-recursive
  (testing "Verifies that adding a new non-existing file where the parent path does not exist will create the file")
    (with-open [fs (create-filesystem)]
      (let [path (uniq-path "/add-file-recursive/")]
        (->> (create-file fs path)
          (is-not-complete)
          (is-not-dir)
          (is-file))
        (.delete fs path false))))

(deftest write-file
  (testing "Creates a new file and writes content to it"
    (with-open [fs (create-filesystem)]
      (let [path (uniq-path "/write-file")
            file (create-file fs path)
            data (rnd-bytes)
            write-type (WriteType/MUST_CACHE)
            read-type (ReadType/NO_CACHE)]
        (with-open [os (.getOutStream file write-type)]
            (.write os data))
        (with-open [input (.getInStream file read-type)]
          (let [read-data (ByteStreams/toByteArray input)]
            (is (= (seq data) (seq read-data)))))))))

(defn uri [n] (TachyonURI. n))

(defn walk 
  ([fs] (walk fs (uri "/")))
  ([fs p]
   (let [files (.listStatus fs p)]
     (doseq [f (map #(.getPath %) files)]
       walk))))

(deftest walk-file-system
  (testing "Verifies that each file is reachable"
    (with-open [fs (create-filesystem)]
      (walk fs))))

