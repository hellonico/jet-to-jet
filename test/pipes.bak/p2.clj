(ns pipes.p2
  "Build on previous example.
  Using pipelines with Jet via Clojure")

(compile 'pipes.tolowercase)
(compile 'pipes.touppercase)
(import [com.hazelcast.jet Pipeline Sources Sinks Jet])
(import [com.hazelcast.jet.config JobConfig])
(require '[chazel.core :refer :all])

(defn populate-list [ list-name ]
  (let [ my-list (hz-list list-name)]
  (doseq [s ["one" "two" "three" "four"]]
    (.add my-list s))))

(defn empty-list [ list-name]
  (-> list-name
    (hz-list)
    (.clear)))

(defn show-list [ list-name]
  (-> list-name
    (hz-list)
    (clojure.pprint/pprint)))

(defn apply-tolist [ jet from to fn ]
  (let [
     p (Pipeline/create)
     config (JobConfig.)
     results (hz-list to)]

  (-> p
    (.drawFrom (Sources/list from))
    (.map (.newInstance fn))
    (.drainTo (Sinks/list to)))

  (.newJob jet p config)

  (show-list to)))

(defn -main [ & args]
  (let [jet (Jet/newJetInstance) ]
  (empty-list "hello")
  (empty-list "results")

  (populate-list "hello")
  (apply-tolist jet "hello" "results" pipes.touppercase)
  ; (show-list "results")

  (apply-tolist jet "hello" "hello" pipes.tolowercase)
  (Thread/sleep 5000)
  (.shutdown jet)
 ))
