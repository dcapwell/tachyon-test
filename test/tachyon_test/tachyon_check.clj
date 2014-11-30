(ns tachyon-test.tachyon-check
 (:require [clojure.test.check :as tc]
           [clojure.test.check.generators :as gen]
           [clojure.test.check.properties :as prop]
           [clojure.test.check.clojure-test :refer (defspec)]
           [clojure.string :as string]
           [tachyon-test.config :refer [config]]
           [tachyon-test.core :as core]
           [tachyon-test.tachyon :as tachyon]))

(defn prefix
  [p rest]
  (if (.startsWith rest p)
    rest
    (str p rest)))

(defmulti path
  (fn [parts] (class parts)))
(defmethod path java.lang.String [parts] 
  (prefix "/" parts))
(defmethod path clojure.lang.PersistentVector [parts]
  (prefix "/" (string/join "/" parts)))

(defmulti perform-action (fn [action] [(:type action) (:via action)]))
(defmethod perform-action [::create-file ::java] 
  [action]
  (with-open [fs (tachyon/create-filesystem)]
    (try
      (let [file (.createFile fs (:path action))]
        ; no exception was thrown, so action was ok
        true)
      (catch java.io.IOException e
        (case (class (.getCause e))
          #=tachyon.thrift.InvalidPathException true
          #=tachyon.thrift.FileAlreadyExistException true
          false)))))

(defn path-gen 
  [] 
  (->> (gen/not-empty gen/string)
       (gen/such-that #(not (.contains % "/")))
       (gen/vector)
       (gen/not-empty)
       (gen/fmap path)))

(defspec path-string
  500
  (prop/for-all [v (path-gen)]
    (= (path v) (path (path v)))))

(defspec create-file
  500
  (prop/for-all [v (path-gen)]
    (perform-action {:type ::create-file 
                     :via ::java 
                     :path v})))
