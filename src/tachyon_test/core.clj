(ns tachyon-test.core
  (:use [tachyon-test.config :as config])
  (:import 
    [tachyon.client TachyonFS]
    [java.io IOException]))

(defn create-filesystem 
  "Creates a new TachyonFS based off the given address"
  [ & {:keys [address] :or {address (config :master-address)}}]
  (TachyonFS/get address))

(defn create-file
  "Attempts to create a file at the given path. If the path already exists or any other exception happens, a string error message is returned"
  [fs path]
  (let [fid (.createFile fs path)]
    (.getFile fs fid)))

(defn uniq-id
  "Returns a new and unique object. This object has a valid string form"
  []
  (java.util.UUID/randomUUID))

(defn uniq-path
  "Creates a unique path string from the given sub-path. :base-path is used as the prefix for all paths and a unique postfix is appended at the end"
  [path & {:keys [pre post] :or {pre (config :base-path) post (uniq-id)}}]
  (str pre path post))

(defn rnd-bytes
  "Produces random byte array"
  ([] (rnd-bytes (config :default-data-size)))
  ([n] (byte-array n)))
