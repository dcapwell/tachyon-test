(ns tachyon-test.tachyon
  (:use [tachyon-test.config :as config])
  (:import [tachyon.client TachyonFS]))

(defn create-filesystem 
  "Creates a new TachyonFS based off the given address"
  [ & {:keys [address] :or {address (config :master-address)}}]
  (TachyonFS/get address))

(defn create-file
  "Attempts to create a file at the given path. If the path already exists or any other exception happens, a string error message is returned"
  [fs path]
  (let [fid (.createFile fs path)]
    (.getFile fs fid)))
