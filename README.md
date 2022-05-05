# Concurrent Web Crawler
A command-line web crawler which employs multiple threads to scrape a given domain name.

---
## The problem

This challenge is to create a web crawler which, for a given URL, will visit every URL it finds on the same domain and output each URL visited together with a list of the links found that the corresponding page.

---
## How to run

The calendar comes with its dependencies (JUnit and JSoup) inside the `lib` folder. The following instructions have been tested on MacOS.

To build it, run:
```
make
```

To run it:
```
make run
```

To run the test suite:
```
make test
```

To clean build artifacts:
```
make clean
```

---
## How it works

The `WebCrawler` is an interface providing a single `execute()` method which, given a starting URL, returns a map of all the URLs crawled from the root URL to a set with the distinct links contained in each page. Its `ConcurrentWebCrawler` implementation takes an `ExecutorService` as a parameter which will be used to perform multiple crawling tasks concurrently. An `AtomicInteger` is used to keep track of the tasks left to complete before the call to `execute()` can be terminated and the results returned to the users.

More specifically, when the `crawl()` method is called, first we check if we have already visited the given URL. The use of `putIfAbsent()`, rather than `containsKey()`, followed by a `put()` allows us to ensure no two threads can crawl the same URL twice. In fact, we only go ahead with the crawling if there is no entry in the map for the given URL. If so, we immediately put an empty set to notify other threads that this URL should not be crawled again. At this point, we increment the atomic taks counter and submit the crawl task to the executor service.

When each crawl task is picked, we retrieve the page document for that URL and update the previously empty set in the map with the actual links found on that page. Finally, we call `crawl()` on each of the links found. It's important that the task counter gets decreased __even__ in the event of an error, otherwise the program will not terminate.

The `WebFetcher` is a utility class to retrieve the HTML document for the given URL. It relies on functionality provided by JSoup, however creating its own class allows me to easily modify this functionality in the future. The `WebParser` is another utility class for extracting links and filtering them to ensure they are valid, belong to the same domain as the root URL and do not represent an asset.

The `Main` class provides the command line interface, prompting the user to enter a URL to crawl or an empty line to terminate the program and release the resources.

---
## Highlights

Here are some of the features which make this implementation of a web crawler efficient and cool:

* __Concurrent crawl__. Multiple threads are employed to speed up the crawling process, with the guarantee that a given URL is not crawled twice by any thread.

* __Reuse of resources__. The crawler takes an executor service as a parameter and reuses its threads for as long as it is running, regardless of the number of crawling jobs that are issued in a sequence.

* __Support for relative URLs__. Relative urls found on a webpage are turned into absolute ones and crawled like the rest.

* __URLs left to crawl__. The crawler uses an atomic integer to inform the user about the status of the job and the number of URLs left to crawl. This also allows the implementation to know when the executor service has completed all crawling tasks so that it may output the results to the user.

* __Standard classes at API level__. The `WebCrawler` interface does not use any 3rd party classes (Jsoup) in its API contract.

---
## Tradeoffs

As I was writing the implementation I had to make certain choices and consider their tradeoffs:

* __Output results at the end__. Rather than printing results as the crawling jobs progresses, this implementation outputs it all at the end. This is to avoid print statements mixing across threads and/or with error messages, and to enable reuse of results rather thanks consuming them straight away.

* __Recursive calls for input__. When the user enters an incorrect root URL, a recursive call to `getURL` is make to prompt the user to try again. The reasonable assumption is that the number of retries will be limited.

* __Empty set to mark as visited__. I use an empty set of URLs as the map value for a given URL before its links are actually retrieved. This is a simple and effective way to immediately notify other threads not to crawl that URL. 

---
## Known limitations

These are the limitations of the current implementation of the crawler:

* __Invalid URLs previously crawled__. The implementation does not remember if visiting a given URL previously resulted in a persistent error (e.g. URL not found), and will retry if it finds it on another page.

* __Protocol and host specific__. The crawler only visits links where both the protocol and the host match that of the starting URL. Inclusing or excluding `www.`, for example, will affect results.

* __E2E testing dependencies__. Some of the tests make actual network calls, which makes them reliant on an internet connection. While I tried to limit the test's reliance on the content of external webpages, they do rely on such websites being up and running.

---
## Further improvements

The current implementation could be further enhanced in the following ways:

* __Allow command line arguments__. The crawler could be further modified to support command line arguments such as the number of threads to use, a timeout for particularly long jobs and whether to output errors (verbose).

* __Improved asset filtering__. This implementation performs some basic filtering of asset resources (PDFs, images, media, etc) based on a limited list of file extensions. This can be improved.

* __Flexible crawl of starting URL__. Current crawling is limited to an exact match of protocol and host with the starting URL. An improvement would be to support flexible crawling so that `http://example.com`, `http://www.example.com` and `https://example.com` are all visited.

---
> Developed by James Reaver in May 2022.