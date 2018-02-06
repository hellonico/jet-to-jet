(ns connectors.jet04
 "Streaming DAG on a hazelcast map"
  (:import
    [com.hazelcast.jet.core DAG Edge]
    [com.hazelcast.jet.core.processor SourceProcessors DiagnosticProcessors SinkProcessors]
    [com.hazelcast.jet Jet]
    [com.hazelcast.jet.config JobConfig]
    [com.hazelcast.jet.core WindowDefinition]
    [com.hazelcast.jet.core WatermarkPolicies WatermarkEmissionPolicy WatermarkGenerationParams]
    [java.nio.charset StandardCharsets]
    [connectors f01]))

; read and execute once
;(def map-stream
;  (SourceProcessors/readMapP
;      "appl"))

(def tumbling (WindowDefinition/tumblingWindowDef 10000))
(def params
  (WatermarkGenerationParams/wmGenParams
   (new realtime.f03)
   (WatermarkPolicies/withFixedLag 500)
   (WatermarkEmissionPolicy/emitByFrame tumbling)
   6000))

(import [com.hazelcast.jet JournalInitialPosition])
(def dag (DAG.))
(def map-stream
  (SourceProcessors/streamMapP
    "appl2"
    JournalInitialPosition/START_FROM_OLDEST
    params))

(def reader
  (-> dag
   (.newVertex "reader" map-stream)
   (.localParallelism 1)))

(def logger
  (.newVertex dag "logger"
  (DiagnosticProcessors/peekInputP
   (com.hazelcast.jet.core.processor.DiagnosticProcessors/writeLoggerP
     (new connectors.f01)))))

(.edge dag (Edge/between reader logger))

(def jet (Jet/newJetInstance))
(def hzl-config
  (-> jet (.getHazelcastInstance) (.getConfig)))
(import [com.hazelcast.config EventJournalConfig])
(def event-config (EventJournalConfig.))
(.setMapName event-config "appl2")
(-> hzl-config (.addEventJournalConfig event-config ))

(def config (JobConfig.))
(def job (.newJob jet dag config))

(comment
  (.cancel job)
  (doseq [j (.getJobs jet)] (.cancel j))


  )
