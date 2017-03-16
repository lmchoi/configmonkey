(ns potty.required-attributes-finder
  (:require [potty.yaml-reader :as reader]))

(declare select-keys)

(defn- find-nested-keys
  [[top-level-fieldname children] criteria]
  (let [blah (select-keys (into {} children) criteria)]
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

(defn- select-keys
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

(defn find-attributes-deprecated
  [filename]
  (let [criteria #(re-matches #".*<%=.*%>.*" %)]
    (-> filename
        (reader/read-file-deprecated)
        (select-keys criteria))))

(defn find-attributes
  [input-file]
  (let [criteria #(re-matches #".*<%=.*%>.*" %)]
    (-> input-file
        (reader/read-file)
        (select-keys criteria))))

