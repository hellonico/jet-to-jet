(ns jet.core
    (:require [camel-snake-kebab.core :refer [->camelCase]])
    (:import
      [com.hazelcast.jet Sources Sinks]
      [com.hazelcast.jet.config JobConfig]
      [com.hazelcast.jet.core.processor SinkProcessors SourceProcessors]
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

(defn source-processor [ type & params]
  (println params)
  (println (count params))
  (println (into-array Class (map class params)))
  (let [m (.getDeclaredMethod SourceProcessors (name type) (into-array Class (map class params)))]
  (.invoke m SourceProcessors (into-array Object params))))

(defn sink-processor [ type params]
  (let [m (.getDeclaredMethod SinkProcessors (name type) (into-array Class [String]))]
  (.invoke m SinkProcessors (into-array Object [params]))))

(defmacro pipeline [ elements ]
 `(let [ p# (jet.core/new-pipeline) ]
   (-> p#
   ~@(map #(concat (list '. (->camelCase (read-string (name (first %))))) (rest %) ) elements)
   (.getPipeline)
   (jet.core/new-job)
   (.join))))

(defmacro dag [ vertices ]
`(let [ d# (jet.core/new-dag) ]
 (-> d#
 ~@(map #(concat (list '. 'newVertex (->camelCase (name (first %)))) (rest %) ) vertices)
 )
 d#))
