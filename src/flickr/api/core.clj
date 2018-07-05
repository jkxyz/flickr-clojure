(ns flickr.api.core
  "Request and response parsing for the Flickr API."
  (:require [clojure.spec.alpha :as s]
            [clojure.walk :refer [postwalk]]
            [clojure.string :as string]
            [clojure.test :refer [deftest is]]
            [cheshire.core :as json]
            [clj-http.client :as http]
            [flickr.core :as flickr]))
            
(s/def ::stat (s/and string? #{"ok" "fail"}))
(s/def ::response (s/keys :req-un [::stat]))

(defn- flatten-content-keys [m]
  (postwalk #(if (map? %) (if-let [c (get % "_content")] c %) %) m))

(defn- keywordize [s] (keyword (string/replace s #"_" "-")))
(defn- keywordize-keys [m]
  (let [f (fn [[k v]] (if (string? k) [(keywordize k) v] [k v]))]
    (postwalk #(if (map? %) (into {} (map f %)) %) m)))

(defn- extract-json-string [response]
  (-> (:body response)
      (string/replace #"^jsonFlickrApi\(" "")
      (string/replace #"\)$" "")))

(defn- extract-json [response]
  (json/parse-string (extract-json-string response)))

(defn- process-response [response]
  (-> (extract-json response)
      flatten-content-keys
      keywordize-keys))

(deftest process-response-test
  (let [response {:body "jsonFlickrApi({\"user_test\":{\"username_test\": {\"_content\": \"__username__\"}}})"}]
    (is (= (process-response response)
           {:user-test {:username-test "__username__"}}))))

(def ^:private api-url "https://api.flickr.com/services/rest")

(s/def ::method string?)
(s/def ::params (s/map-of (s/or :string string? :keyword keyword?) string?))

(defn- base-params [{:keys [::flickr/api-key]} method]
  {"api_key" api-key
   "method" method
   "format" "json"})

(defn request 
  "Calls method on the Flickr API with params."
  [api-info method params]
  {:pre [(s/valid? ::flickr/api-info api-info)
         (s/valid? ::method method)
         (s/valid? ::params params)]}
  (let [query-params (merge (base-params api-info method) params)
        response (http/get api-url {:query-params query-params})]
    (process-response response))) 

(s/fdef request
  :args (s/cat :api-info ::flickr/api-info
               :method ::method
               :params ::params)
  :ret ::response)
