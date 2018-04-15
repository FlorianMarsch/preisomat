var CACHE_NAME = '2018.4.15-11:17';
var urlsToCache = [
	'/',
	'/app.js',
	'/index.html',
	'/pwa.manifest',
	'/sw-register.js',
  '/template/404.html',
  '/template/add.html',
  '/template/admin.html',
  '/template/bill.html',
  '/template/cost.html',
  '/template/create-cost-centre.html',
  '/template/error.html',
  '/template/guv.html',
  '/template/overview.html',
  '/template/person.html',
  '/template/settings.html',
  '/template/show.html'
  
];

self.addEventListener('install', function(event) {
  // Perform install steps
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(function(cache) {
        return cache.addAll(urlsToCache);
      })
  );
});

self.addEventListener('fetch', function(event) {
	  event.respondWith(
	    caches.match(event.request)
	      .then(function(response) {
	        // Cache hit - return response
	        if (response) {
	          return response;
	        }

	        // IMPORTANT: Clone the request. A request is a stream and
	        // can only be consumed once. Since we are consuming this
	        // once by cache and once by the browser for fetch, we need
	        // to clone the response.
	        var fetchRequest = event.request.clone();

	        return fetch(fetchRequest).then(
	          function(response) {
	            // Check if we received a valid response
	            if(!response || response.status !== 200 || response.type !== 'basic') {
	              return response;
	            }
	            if(event.request.url.includes("api") || event.request.url.includes("sw.js")){
	            		return response;
	            }
	            // IMPORTANT: Clone the response. A response is a stream
	            // and because we want the browser to consume the response
	            // as well as the cache consuming the response, we need
	            // to clone it so we have two streams.
	            var responseToCache = response.clone();

	            caches.open(CACHE_NAME)
	              .then(function(cache) {
	                cache.put(event.request, responseToCache);
	              });

	            return response;
	          }
	        );
	      })
	    );
	});