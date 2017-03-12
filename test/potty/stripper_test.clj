(ns potty.stripper-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [potty.stripper :refer :all]))

(facts "about stripping values"
       (fact "from file"
             (let [required-attributes ["port" "host" "doesnotexist"]]
               (strip required-attributes "resources/simple-config.yml")
               => {:host "localhost"
                   :port 1337}))

       (fact "from file with nested values"
             (let [required-attributes ["invoice" {"bill-to" ["given" "family"]}]]
               (strip required-attributes "resources/sample-invoice.yml")
               => {:invoice 34843
                   :bill-to {:given "Chris"
                             :family "Dumars"}}))

       (fact "from file with multi level nested values"
             (let [required-attributes ["invoice" {"bill-to" ["given"
                                                              {"address" ["city" "postal"]}
                                                              "family"]}]]
               (strip required-attributes "resources/sample-invoice.yml")
               => {:invoice 34843
                   :bill-to {:given "Chris"
                             :family "Dumars"
                             :address {:city "Royal Oak"
                                       :postal 48046}}})))

