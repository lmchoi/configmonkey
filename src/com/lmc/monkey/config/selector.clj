(ns com.lmc.monkey.config.selector)

(declare select-attributes)

(defn- find-nested-attributes
  [[top-level-fieldname children] criteria]
  (let [children-attributes (select-attributes
                             (into {} children)
                             (get criteria top-level-fieldname))]
    (when (not (empty? children-attributes))
      [top-level-fieldname children-attributes])))

(defn- find-attributes
  [[fieldname value :as entry] criteria]
  (cond
    (coll? value)
    (find-nested-attributes entry criteria)

    (get criteria fieldname)
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

(defn select-entries
  [all-entries criteria]
  (select-attributes all-entries criteria))
