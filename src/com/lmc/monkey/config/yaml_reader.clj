(ns com.lmc.monkey.config.yaml-reader
  (:require [yaml.core :as yaml]))

(defn read-file
  [input-file]
  (->> input-file
       (yaml/from-file)
       (into {})))

