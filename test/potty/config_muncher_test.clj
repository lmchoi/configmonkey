(ns potty.config-muncher-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [potty.stripper :as extractor]
            [potty.config-muncher :refer :all]))

(facts ""
  (fact ""
        (munch ["some-config-dev.yml"
                "some-config-qa.yml"]) => {:dev ..dev-config-values..
                                           :qa ..qa-config-values..}
        (provided
         (extractor/extract-values "some-config-dev.yml") => ..dev-config-values..,
         (extractor/extract-values "some-config-qa.yml") => ..qa-config-values..)))
