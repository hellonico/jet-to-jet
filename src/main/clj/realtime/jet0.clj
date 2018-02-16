(ns realtime.jet0
    "Almost Empty DAG. 1 vertex, does not nothing but create a folder")

(import [com.hazelcast.jet.core DAG])
(import [com.hazelcast.jet Jet])
(import [com.hazelcast.jet.config JobConfig])

; prepare DAG
(def dag (DAG.))
(def writer
  (->> "output"
  (com.hazelcast.jet.core.processor.SinkProcessors/writeFileP)
  (.newVertex dag "writer")))

; start JET
(def jet (Jet/newJetInstance))

; deploy job
(def config (JobConfig.))
(def job (.newJob jet dag config))
