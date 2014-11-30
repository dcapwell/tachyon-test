(ns tachyon-test.tachyon-check
 (:require [clojure.test.check :as tc]
           [clojure.test.check.generators :as gen]
           [clojure.test.check.properties :as prop]
           [clojure.test.check.clojure-test :refer (defspec)]
           [clojure.string :as string]))

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

(defn path-gen [] (gen/vector gen/string))

(defspec path-string
  100
  (prop/for-all [v (path-gen)]
    (= (path v) (path (path v)))))
