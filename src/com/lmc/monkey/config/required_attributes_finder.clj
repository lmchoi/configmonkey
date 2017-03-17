(ns com.lmc.monkey.config.required-attributes-finder)

(declare select-attributes)

(defn- find-nested-attributes
  [[top-level-fieldname children] criteria]
  (let [children-attributes (select-attributes
                             (into {} children)
                             criteria)]
    (when (not (empty? children-attributes))
      [top-level-fieldname children-attributes])))

(defn- find-attributes
  [[fieldname value :as entry] criteria]
  (cond
    (coll? value)
    (find-nested-attributes entry criteria)

    (criteria entry)
    entry))

(defn- select-attributes
  [data criteria]
  (loop [ret {}
         entries data]
    (if entries
      (let [entry (first entries)
            criteria-met (find-attributes entry criteria)]
        (recur
         (if criteria-met
           (conj ret criteria-met)
           ret)
         (next entries)))
      (with-meta ret (meta data)))))

(defn select-entries-by-criteria
  [all-entries criteria]
  (select-attributes all-entries criteria))
