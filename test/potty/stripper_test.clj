(ns potty.stripper-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [potty.stripper :refer :all]))

(facts "about stripping values from line"
       (fact "value is a string"
             (let [attribute "host"
                   line "host: \"localhost\""]
               (strip attribute line) => "localhost"))
       (fact "value is an int"
             (let [attribute "port"
                   line "port: 1337"]
               (strip attribute line) => 1337))
       (fact "value does not exist"
             (let [attribute "host"
                   line "not-this-line: not-this-value"]
               (strip attribute line) => nil)))
