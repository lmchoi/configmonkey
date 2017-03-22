(ns com.lmc.monkey.config.chef-recipe-generator-test
  (:use midje.sweet)
  (:require [com.lmc.monkey.config.chef-recipe-generator :as chef]))

(use '[midje.util :only [testable-privates]])

(facts "about parsing placeholders"
       (testable-privates com.lmc.monkey.config.chef-recipe-generator parse-placeholders)
       (fact "empty string"
             (parse-placeholders "")
             => [])
       (fact "incomplete placeholders are treated as a string"
             (parse-placeholders "<%= blah")
             => ["<%= blah"])
       (fact "string containing no placeholders are treated as a single string"
             (parse-placeholders "contains-no-placeholders")
             => ["contains-no-placeholders"])
       (fact "contains one singler-level placeholder by itself"
             (parse-placeholders "<%= node['placeholder'] %>")
             => ["<%= node['placeholder'] %>"])
       (fact "contains one multi-level placeholder by itself"
             (parse-placeholders "<%= node['placeholder']['sub1']['sub2'] %>")
             => ["<%= node['placeholder']['sub1']['sub2'] %>"])
       (fact "placeholder with a string before it"
             (parse-placeholders "some-string <%= node['placeholder'] %>")
             => ["some-string " "<%= node['placeholder'] %>"])
       (fact "placeholder with a string after it"
             (parse-placeholders "<%= node['placeholder'] %> some-string")
             => ["<%= node['placeholder'] %>" " some-string"])
       (fact "placeholder with a string after it"
             (parse-placeholders "http://<%= node['host'] %>:<%= node['port'] %>/some-path")
             => ["http://" "<%= node['host'] %>" ":" "<%= node['port'] %>" "/some-path"]))

#_(facts "about finding the value of placeholders"
       (fact "single value"
             (chef/find-values 1337 ["<%= node['port'] %>"])
             => [["<%= node['port'] %>" 1337]])

       ; log error?
       (fact "ignore if it is not a placeholder"
             (chef/find-values 1337 ["not-a-placeholder"])
             => [])

       (fact ""
             (chef/find-values
              "http://localhost:8080/some-path"
              ["http://" "<%= node['host'] %>" ":" "<%= node['port'] %>" "/some-path"])
             => [["<%= node['host'] %>" "localhost"]
                 ["<%= node['port'] %>" 8080]]))
#_(facts "about generating attributes"
       (fact "no matching fields"
             (chef/generate-attributes
              {:template {:erb {:host "<%= node['local']['host'] %>",
                                :port "<%= node['local']['port'] %>"}}
               :values   {:dev {:host "munchy.dev"
                                :port 1337}
                          :qa  {:host "munchy.qa"
                                :port 9001}}}
              ))
       (fact "single attribute fields"
             (chef/generate-attributes
              {:template {:erb {:host "<%= node['local']['host'] %>",
                                :port "<%= node['local']['port'] %>"}}
               :values   {:dev {:host "munchy.dev"
                                :port 1337}
                          :qa  {:host "munchy.qa"
                                :port 9001}}})
             => {:dev {"override['local']['host']" "munchy.dev"
                       "override['local']['port']" 1337}
                 :qa  {"override['local']['host']" "munchy.qa"
                       "override['local']['port']" 9001}})

       (fact "field composed of multiple attributes"
             (chef/generate-attributes
              {:template {:erb {:url "http://<%= node['local']['host'] %>:<%= node['local']['port'] %>"}}
               :values   {:dev {:url "http://munchy.dev:1337"}
                          :qa  {:url "http://munchy.qa:9001"}}})
             => {:dev {"override['local']['host']" "munchy.dev"
                       "override['local']['port']" 1337}
                 :qa  {"override['local']['host']" "munchy.qa"
                       "override['local']['port']" 9001}}))


