(ns tachyon-test.tachyon-check
  (:require [clojure.string :as string]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer (defspec)]
            [tachyon-test.config :refer [config]]
            [tachyon-test.core :as core]
            [tachyon-test.tachyon :as tachyon])
  (:import [tachyon TachyonURI]))

(def iterations 500)

(defspec path-string
  iterations
  (prop/for-all [v (tachyon/path-gen)]
    (=
      (tachyon/path v)
      (tachyon/path (tachyon/path v)))))

(defspec tachyon-uri-path
  iterations
  (prop/for-all [v (tachyon/path-gen)]
    (try
      (TachyonURI. v)
      true
      (catch java.net.URISyntaxException e false))))

(defspec create-file
  iterations
  (prop/for-all [v (tachyon/path-gen)]
    (every?
      identity
      [(tachyon/perform-action {:type ::tachyon/create-file
                                :via ::tachyon/java
                                :path v})
       (tachyon/perform-action {:type ::tachyon/fetch-file
                                :via ::tachyon/java
                                :path v
                                :test (fn [file]
                                        (and
                                          (= (.isComplete file) false)
                                          (= (.isDirectory file) false)
                                          (= (.isFile file) true)
                                          (= (.isInMemory file) true)))})])))
