(ns flickr.api.photos.get-info
  (:require [clojure.spec.alpha :as s]
            [flickr.api.core :as api]))

(s/def ::single-url string?)
(s/def ::url (s/coll-of ::single-url))
(s/def ::urls (s/keys :req-un [::url]))

(s/def ::photo (s/keys :req-un [::id
                                ::title
                                ::description
                                ::urls]))

(s/def ::response (s/merge ::api/response
                           (s/keys :req-un [::photo])))

(def ^:private method "flickr.photos.getInfo")

(defn request [api-info photo-id]
  (let [response (api/request api-info method {"photo_id" photo-id})
        parsed (s/conform ::response response)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid response" (s/explain-data ::response response)))
      parsed)))
