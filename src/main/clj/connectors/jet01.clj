(ns connectors.jet01
"Simple DAG with Read write files using Java Based Sinks and Source processor
See:
https://github.com/hazelcast/hazelcast-jet/blob/master/hazelcast-jet-core/src/main/java/com/hazelcast/jet/core/processor/SinkProcessors.java")

(import [com.hazelcast.jet.core DAG Edge])
(import [com.hazelcast.jet.core.processor DiagnosticProcessors])
(import [com.hazelcast.jet Jet])
(import [com.hazelcast.jet.config JobConfig])

(import [java.nio.charset StandardCharsets])
(import [com.hazelcast.jet.core.processor SinkProcessors])
(import [com.hazelcast.jet.core.processor SourceProcessors])

(def dag (DAG.))

(def source-folder "in")


(def reader
  (.localParallelism
    (.newVertex dag "reader"
  (SourceProcessors/readFilesP
    source-folder
    StandardCharsets/UTF_8
    "*.txt" )) 1 ))

(def writer
  (.newVertex dag "writer"
    (SinkProcessors/writeFileP "out")))

(.edge dag (Edge/between reader writer))

(def jet (Jet/newJetInstance))
(def config (JobConfig.))
(def job (.newJob jet dag config))

(comment
  (.cancel job)

  (spit (str "in/hello" (System/currentTimeMillis) ".txt") (str "bonjour" (rand) "\n"))
  )
