(ns pipes.j1
    "Making Pipes using a tentative api"
  (:import
    [com.hazelcast.jet Sources Sinks])
  (:require
    [chazel.core :refer :all]
    [jet.core :as jet]))

; start jet
(jet/new-instance)

; get a list and add some elements
(-> (hz-list "from")
  (.addAll (map #(str "hello" %) (range 1000000))))


; create and run a pipeline using the existing jet instance
(let [p (jet/hz-pipeline) ]
  (-> p
     (.drawFrom   (Sources/list "from"))
     (.map        (jet/new-fn 'pipes.reverse))
     (.drainTo    (Sinks/list "to")))
  (jet/new-job p))

; display target list content
(count (hz-list "to"))

(comment
 (.clear (hz-list "to"))
 (.clear (hz-list "from")))
