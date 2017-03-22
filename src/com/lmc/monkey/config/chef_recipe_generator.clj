(ns com.lmc.monkey.config.chef-recipe-generator
  (:require [clojure.string :as str]))

(defn- parse-placeholders
  [placeholders]
  (let [placeholder-matches (re-seq #"(.*?)(<%=\s*node\[.+?\]\s*%>)|(.+)?" placeholders)]
    (mapcat
     #(filter not-empty (rest %)) placeholder-matches)
    #_(when ph
      (clojure.string/replace ph #"node" "override"))))

(defn- is-placeholder?
  [str]
  (re-find #"<%=\s*node\[.+?\]\s*%>" str))

(defn- per-attribute
  [values [fieldname placeholders]]
  {(parse-placeholders placeholders) (get values fieldname)})

(defn- per-env
  [[env values] required-attributes]
  {env (into {} (map #(per-attribute values %) required-attributes))})

(defn generate-attributes [{:keys [template values]}]
  (into {} (map #(per-env % (:erb template)) values)))

(defn find-values
  [templated-value placeholders]
  ; TODO flag a warning if there is more than one placeholders in a str
  (loop [ret []
         str-values templated-value
         p-holders placeholders]
    (if p-holders
      (let [placeholder (first p-holders)]
        (cond
          (is-placeholder? placeholder)
          (recur (conj ret [placeholder str-values])
                 (str/replace-first str-values placeholder "")
                 (next p-holders))
          (str/starts-with? str-values placeholder)
          (recur ret
                 (str/replace-first str-values placeholder "")
                 (next p-holders))
          :default
          (recur ret
                 str-values
                 (next p-holders))))
      ret)))
