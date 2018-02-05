(ns util.convertbw
  (:import
    [javax.imageio ImageIO]
    [java.awt.color ColorSpace]
    [java.awt.image ColorConvertOp]))

(def op
  (ColorConvertOp. (ColorSpace/getInstance ColorSpace/CS_GRAY) nil))

(defn to-blackwhite [img]
    (.filter op img img)
  )
(defn to-blackwhitefile [img path]
  (ImageIO/write
    (to-blackwhite img)
    "jpg"
    (clojure.java.io/as-file path)))

(comment
 (def grayScale
   (ImageIO/read (clojure.java.io/as-file "src/main/resources/emilie5.jpg")))
)
