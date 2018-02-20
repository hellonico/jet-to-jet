(ns pipes.pipe00
    "Making Pipes using a tentative api"
  (:require
    [chazel.core :refer :all]
    [jet.core :as jet]))

; start jet
(jet/new-instance)

; get a list and add some elements
(-> (hz-list "from")
    (.addAll (map #(str "hello" %) (range 1000000))))

; create and run a pipeline
; using the existing jet instance
(jet/pipeline [
  [:draw-from   (jet/source :list "from")]
  [:map         (jet/new-fn 'pipes.fn.reverse)]
  [:drain-to    (jet/sink :list "to")]
  ])

; display count of target list
; should be 1000000
(count (hz-list "to"))
