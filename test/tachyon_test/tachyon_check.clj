(ns tachyon-test.tachyon-check
  (:require [clojure.string :as string]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer (defspec)]
            [tachyon-test.config :refer [config]]
            [tachyon-test.core :as core]
            [tachyon-test.tachyon :as tachyon]))

; (def iterations 500)
(def iterations 100)

(defspec path-string
  iterations
  (prop/for-all [v (tachyon/path-gen)]
    (=
      (tachyon/path v)
      (tachyon/path (tachyon/path v)))))

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
                                :complete? false
                                :directory? false
                                :file? true
                                :in-memory? true})])))
