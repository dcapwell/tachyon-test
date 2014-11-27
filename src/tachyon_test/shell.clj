(ns tachyon-test.shell
  (:import [java.lang System ProcessBuilder Process]))

(defn- copy
  "Copy java's InputStream into a array of bytes"
  [^java.io.InputStream in]
  (com.google.common.io.ByteStreams/toByteArray in))

(defn- copy-str
  "Copy java's InputStream into a string"
  [^java.io.InputStream in]
  (java.lang.String. (copy in)))

(defn fork
  "Forks off a command, returning a java Process. If :inheritIO is true, :out and :err will be empty."
  [cmd & {:keys [env inheritIO] :or {env (System/getenv) inheritIO true}}]
  (do
    (println "Running command: " cmd)
    (let [p (ProcessBuilder. cmd)]
      (.putAll (.environment p) env)
      (if inheritIO (.inheritIO p))
      (.start p))))

(defn wait-for
  "Waits for a given Process to complete. If it doesn't complete within the given timeout period, then :status will be :timeout and process is killed"
  [^Process p & {:keys [timeout time-unit] :or {timeout 30 time-unit java.util.concurrent.TimeUnit/SECONDS}}]
  (try
    (let [rc (.get (future-call #(.waitFor p)) timeout time-unit)]
      {:status :ok :exit rc :out (copy-str (.getInputStream p)) :err (copy-str (.getErrorStream p))})
    (catch java.util.concurrent.TimeoutException e
      (do
        (.destroy p)
        {:status :timeout :exit (.waitFor p) :out (copy-str (.getInputStream p)) :err (copy-str (.getErrorStream p))}))))

