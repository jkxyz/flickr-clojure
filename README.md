Flickr API for Clojure
======================

A Clojure interface for the Flickr API.

Example
-------

```clj
(require '[flickr.core :as flickr])

(def api-info (flickr/api-info "YOUR_API_KEY"))

(require '[flickr.user :as flickr-user]
         '[flickr.photoset :as photoset])

(def u (flickr-user/find-by-username api-info "someusername"))
(def photosets (photoset/get-by-user api-info u))
```

To Do
-----

- [ ] Full API coverage
- [ ] OAuth
- [ ] Caching
