(ns flickr.api.people.find-by-username
  (:require [clojure.spec.alpha :as s]
            [flickr.api.core :as api]))

(s/def ::id string?)
(s/def ::username string?)

(s/def ::user (s/keys :req-un [::id
                               ::username]))

(s/def ::response (s/merge ::api/response
                           (s/keys :req-un [::user])))

(def ^:private method "flickr.people.findByUsername")

(defn request [api-info username]
  (let [response (api/request api-info method {"username" username})
        parsed (s/conform ::response response)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid response" (s/explain-data ::response response)))
      parsed)))
