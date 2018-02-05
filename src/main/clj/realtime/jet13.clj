; Distributed Jet Test
; Simple Aggregation Function, counting written in Clojure
;
(ns realtime.jet13)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])
; used by piped processors
(import '[com.hazelcast.jet.core WindowDefinition])
(import '[com.hazelcast.jet.core.processor Processors])
(import '[com.hazelcast.jet.aggregate AggregateOperations])
(compile 'realtime.p1)
(compile 'realtime.p3)
(compile 'realtime.f01)
(compile 'realtime.f03)

(def tumbling
  (WindowDefinition/tumblingWindowDef 10000))

(def cc
  (Processors/accumulateByFrameP
    (new realtime.f01)
    (new realtime.f03)
    com.hazelcast.jet.core.TimestampKind/EVENT
    tumbling
    (AggregateOperations/counting)))

(defn dag-13 []
 (let [ dag (DAG.)
        myp3      (.newVertex dag "p3" (OneSourceSupplier. "p3" realtime.p3))
        myp1      (.newVertex dag "p1"  (OneSourceSupplier. "p1" realtime.p1))
        ; counting  (.localParallelism (.newVertex dag "counting" cc) 2)
        counting  (.localParallelism (.newVertex dag "counting" cc) 1)
        ]
        ; (->> counting  (Edge/between myp3) (.distributed) (.edge dag))
        ; (->> counting  (Edge/between myp3) (.allToOne) (.edge dag))
        (->> counting  (Edge/between myp3) (.edge dag))
        (->> myp1      (Edge/between counting) (.edge dag))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job (-> jet (.newJob (dag-13) config)) )

; total should be 240 (p3 currently sends 2 messages each time)

(comment
 (.cancel job)
 ; cancel all
 (doseq [j (.getJobs jet)] (.cancel j))
 (.shutdown jet)

)
