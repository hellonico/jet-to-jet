; Distributed Jet Test
; Simple Aggregation Function, aggregating written in Clojure
;
(ns realtime.jet14)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])
(import '[com.hazelcast.jet.aggregate AggregateOperations])

(compile 'realtime.p1)
(compile 'realtime.p14)
(compile 'realtime.f03)
(compile 'realtime.f01)
(compile 'realtime.f14)

; used by piped processors
(import '[com.hazelcast.jet.core WindowDefinition])
(import '[com.hazelcast.jet.core.processor Processors])
(def tumbling (WindowDefinition/tumblingWindowDef 10000))

(def cc
  (Processors/accumulateByFrameP
    (new realtime.f01) ; returns aggregation key
    (new realtime.f03) ; timestamp function
    com.hazelcast.jet.core.TimestampKind/EVENT
    tumbling
    (AggregateOperations/summingLong (new realtime.f14))))

(defn dag-14 []
 (let [ dag (DAG.)
        myp3
         (.newVertex dag "p14" (OneSourceSupplier. "p14" realtime.p14))
        myp1
         (.newVertex dag "p1"  (OneSourceSupplier. "p1" realtime.p1))
        aggregating (.newVertex dag "aggregating" cc)
        ]
        (->> aggregating (Edge/between myp3) (.allToOne) (.edge dag))
        (->> myp1    (Edge/between aggregating) (.edge dag))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job (-> jet (.newJob (dag-14) config)) )

(comment
 (.cancel job)
 ; cancel all
 (doseq [j (.getJobs jet)] (.cancel j))
 (.shutdown jet)

)
