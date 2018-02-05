; Distributed Jet Test
; All messages sent to participants randomly!!!
;
(ns realtime.jet11)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])
(import '[com.hazelcast.jet.core ProcessorSupplier])

(compile 'realtime.p1)
(compile 'realtime.p3)
(compile 'realtime.f02)

(defn dag-11 []
 (let [ dag (DAG.)
        myp3
         (.newVertex dag "p3" (OneSourceSupplier. "p3" realtime.p3))
        myp1
         (.newVertex dag "p1"  (ProcessorSupplier/of (new realtime.f02)))]
        (->> myp1
         (Edge/between myp3)
         (.distributed)
         (.edge dag ))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job (-> jet (.newJob (dag-11) config)) )

; just start a few more jet instances with
; lein run -m client.simple

; note that if you want to get the new processor definition on other nodes
; you need to do *per node*
; (compile 'realtime.p1)
; other the old class file is still loaded in memory

(comment
 (.cancel job)
 (.shutdown jet)

)
