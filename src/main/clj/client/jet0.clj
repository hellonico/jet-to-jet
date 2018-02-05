(ns cient.jet0)

(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])
(def config (JobConfig.))
(def jet (Jet/newJetInstance))

(->  jet
  (.getCluster)
  (.getMembers))

(-> jet (.getJobs))
