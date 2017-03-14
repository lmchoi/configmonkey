(ns potty.yaml-reader
  (:require [yaml.core :as yaml]
            [clojure.java.io :as io]))

(defn read-file
  [filename]
  (->> filename
       (io/resource)
       (yaml/from-file)
       (into {})))
