(ns flickr.api.photos.get-sizes
  (:require [clojure.spec.alpha :as s]
            [flickr.api.core :as api]))

(s/def ::label string?)
(s/def ::source string?)
(s/def ::single-size (s/keys :req-un [::label
                                      ::source]))

(s/def ::size (s/coll-of ::single-size))

(s/def ::sizes (s/keys :req-un [::size]))

(s/def ::response (s/keys :req-un [::sizes]))

(def ^:private method "flickr.photos.getSizes")

(defn request [api-info photo-id]
  (let [response (api/request api-info method {"photo_id" photo-id})
        parsed (s/conform ::response response)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid response" (s/explain-data ::response response)))
      parsed)))
