(ns potty.required-attributes-finder-test
  (:use midje.sweet)
  (:require  [potty.required-attributes-finder :refer :all]))

(facts "about finding required attributes based on erb subs"
       (fact "top-level values"
             (find-attributes "resources/simple-config.yml.erb")
             => ["host", "port"])

       (fact "nested values"
             (find-attributes "resources/sample-invoice.yml.erb")
             => ["invoice", {"bill-to" ["given"]}, {"ship-to" ["given"]}]))
