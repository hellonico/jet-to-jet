(ns util.webcamtest)
;
; boofcv Webcam test
;
(import '[boofcv.io.webcamcapture UtilWebcamCapture])

(def webcam (UtilWebcamCapture/openDefault 640 480))
(def image (.getImage webcam))
(println image)
(.close webcam)
