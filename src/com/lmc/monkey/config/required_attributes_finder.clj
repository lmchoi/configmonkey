(ns com.lmc.monkey.config.required-attributes-finder
  (:require [com.lmc.monkey.config.yaml-reader :as reader]))

(declare select-attributes)

(defn- find-nested-keys
  [[top-level-fieldname children] criteria]
  (let [blah (select-attributes (into {} children) criteria)]
    (when (not (empty? blah))
      [top-level-fieldname blah])))

(defn- find-keys
  [entry criteria]
  (let [value (val entry)]
    (cond
      (and (string? value)
           (criteria value))
      entry

      (coll? value)
      (find-nested-keys entry criteria))))

(defn- select-attributes
  [data criteria]
  (loop [ret {}
         entries data]
    (if entries
      (let [entry (first entries)
            k (find-keys entry criteria)]
        (recur
         (if k
           (conj ret k)
           ret)
         (next entries)))
      ret)))

(defn select-entries-by-criteria
  [all-entries criteria]
  (select-attributes all-entries criteria))
