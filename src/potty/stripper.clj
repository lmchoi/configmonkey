(ns potty.stripper
  (:refer-clojure :exclude [load])
  (:require [yaml.core :as yaml]
            [clojure.java.io :as io]))

(declare select-attributes)

(defn- read-yaml
  [filename]
  (->> filename
       (io/resource)
       (yaml/from-file)
       (into {})))

(defn- find-nested-attribute
  [data [top-level-fieldname required-children]]
  (let [[fieldname nested-data] (find data top-level-fieldname)]
    (when nested-data
      [fieldname (select-attributes (into {} nested-data) required-children)])))

(defn- find-attribute
  [data fieldname]
  (if (coll? fieldname)
    (find-nested-attribute data (first fieldname)) ; should only have one entry!
    (find data fieldname)))

(defn- select-attributes
  [data required-attributes]
  (loop [ret {}
         attributes (seq required-attributes)]
    (if attributes
      (let [attr (first attributes)
            entry (find-attribute data attr)]
        (recur
         (if entry
           (conj ret entry)
           ret)
         (next attributes)))
      (with-meta ret (meta data)))))

(defn strip
  [required-attributes config-file]
  (-> config-file
      (read-yaml)
      (select-attributes required-attributes)
      (clojure.walk/keywordize-keys)))
