(ns tachyon-test.core
  (:require [tachyon-test.config :refer [config]]))

(defn uniq-id
  "Returns a new and unique object. This object has a valid string form"
  []
  (java.util.UUID/randomUUID))

(defn uniq-path
  "Creates a unique path string from the given sub-path. :base-path is used as the prefix for all 
  paths and a unique postfix is appended at the end"
  [path & {:keys [pre post] :or {pre (config :base-path) post (uniq-id)}}]
  (str pre path post))

(defn rnd-bytes
  "Produces random byte array"
  ([] (rnd-bytes (config :default-data-size)))
  ([n] (byte-array n)))

(defn bounded
  "Runs the function in the background. If the function takes longer than timeout then a timeout 
  exception is thrown"
  [fn & {:keys [timeout time-unit] :or {timeout 30 time-unit java.util.concurrent.TimeUnit/SECONDS}}]
  (.get (future-call fn) timeout time-unit))
