package com.jamesreaver.api;

import java.net.URL;
import java.util.Map;
import java.util.Set;

public interface WebCrawler {

    /**
     * Given a root url, return a map of all urls found on that page
     * and all subpages recur.
     *
     * @param  rootUrl  the url representing the domain to be crawled
     * @return          the map of the crawled domain as described above
     */
    Map<URL, Set<URL>> execute(URL rootUrl);

}
