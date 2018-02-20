(ns jet.client
  "Simply Start a new Jet Instance. This does not do anything but start a jet instance"
  (:require [jet.core :as jet])
  (:gen-class))

; eventually  all your code dependencies are in and so you can start a new
; jet instance to join the show by calling this namespace.

(defn -main [& args]
  (jet/new-instance))
