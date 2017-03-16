(ns potty.yaml-reader
  (:require [yaml.core :as yaml]
            [clojure.java.io :as io]))

(defn read-file-deprecated
  [filename]
  (->> filename
       (io/resource)
       (yaml/from-file)
       (into {})))

(defn read-file
  [input-file]
  (->> input-file
       (yaml/from-file)
       (into {})))

