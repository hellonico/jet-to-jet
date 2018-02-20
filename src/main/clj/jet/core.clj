(ns jet.core
    (:require [camel-snake-kebab.core :refer [->camelCase]])
    (:import
      [com.hazelcast.jet Sources Sinks]
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

(defn new-pipeline []
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

(defn source [ type id ]
  (let [m (.getDeclaredMethod Sources (name type) (into-array Class [String]))]
      (.invoke m Sources (into-array String [id]))))

(defn sink [ type id]
  (let [m (.getDeclaredMethod Sinks (name type) (into-array Class [String]))]
      (.invoke m Sinks (into-array String [id]))))

(defmacro pipeline [ elements ]
 `(let [ p# (jet.core/new-pipeline) ]
   (-> p#
   ~@(map #(concat (list '. (->camelCase (read-string (name (first %))))) (rest %) ) elements)
   (.getPipeline)
   (jet.core/new-job)
   (.join))))
