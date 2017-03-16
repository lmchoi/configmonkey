(ns potty.stripper-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [potty.stripper :refer :all]))

(facts "about stripping values from file"
       (fact "top-level values"
             (let [required-attributes {"port" "<%= some-port %>"
                                        "host" "<%= some-host %>"}]
               (extract-values-deprecated required-attributes "resources/simple-config.yml")
               => {:host "localhost"
                   :port 1337}))

       (fact "nested values"
             (let [required-attributes {"invoice" anything
                                        "bill-to" {"given"  anything
                                                   "family" anything}}]
               (extract-values-deprecated required-attributes "resources/sample-invoice.yml")
               => {:invoice 34843
                   :bill-to {:given  "Chris"
                             :family "Dumars"}}))

       (fact "multi level nested values"
             (let [required-attributes {"invoice" anything
                                        "bill-to" {"given"   anything
                                                   "address" {"city"   anything
                                                              "postal" anything}
                                                   "family"  anything}}]
               (extract-values-deprecated required-attributes "resources/sample-invoice.yml")
               => {:invoice 34843
                   :bill-to {:given   "Chris"
                             :family  "Dumars"
                             :address {:city   "Royal Oak"
                                       :postal 48046}}})))

