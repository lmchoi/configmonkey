(ns potty.stripper
  (:gen-class))

(defn strip
  [attribute line]
  (let [attribute-pattern (re-pattern (str attribute ":\\s*(.+)$"))]
    (some->> line
             (re-matcher attribute-pattern)
             (re-find)
             (second)
             (read-string))))
