(ns jet.core
    (:import
      [com.hazelcast.jet.config JobConfig]
      [com.hazelcast.jet Pipeline Jet]
      [com.hazelcast.jet.core DAG]
      [com.hazelcast.config EventJournalConfig]))

(def current
  (atom nil))

(defn new-instance []
    (let [jet (Jet/newJetInstance)]
        (reset! current jet)
        jet))

(defn hz-config
  ([] (hz-config @current))
  ([jet]
  (-> jet (.getHazelcastInstance) (.getConfig))))

(defn add-journal-for-map
  ([ map-name ] (add-journal-for-map @current map-name))
  ([jet map-name]
    (let [event-config (EventJournalConfig.)]
      (.setMapName event-config map-name)
      (-> jet (hz-config) (.addEventJournalConfig event-config)))))

(defn hz-pipeline []
  (Pipeline/create))

(defn job-config []
  (JobConfig.))

(defn new-fn [klass]
  ; do a class.forName here
  (compile klass)
  (.newInstance (eval klass)))

(defn new-job
  ([ pipe-or-dag ] (new-job @current pipe-or-dag))
  ([ jet pipe-or-dag ]
  (.newJob jet pipe-or-dag (job-config))))


(defn new-dag []
  (DAG.))
