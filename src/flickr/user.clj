(ns flickr.user
  (:require [clojure.spec.alpha :as s]
            [flickr.api.people.find-by-username :as find-by-username]))

(defn- make-user [{:keys [user]}]
  {::id (:id user)
   ::username (:username user)})

(defn find-by-username [api-info username]
  (let [response (find-by-username/request api-info username)]
    (make-user response)))
