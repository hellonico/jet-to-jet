(ns pipes.pipe00
    "Apply a function to all elements in a list"
  (:require
    [chazel.core :refer :all]
    [jet.core :as jet]))

; start jet
(jet/new-instance)

; get a list and add some elements
(add-all!
  (hz-list "text")
  ["hello world hello hello world i want more hello"])

; create and run a pipeline
; using the existing jet instance
(jet/pipeline [
  [:draw-from   (jet/source :list "text")]
  [:flat-map         (jet/new-fn 'pipes.fn.traverse)]
  [:drain-to    (jet/sink :list "mapped")]
  ])

; display target list

(clojure.pprint/pprint
  (hz-list "mapped"))
