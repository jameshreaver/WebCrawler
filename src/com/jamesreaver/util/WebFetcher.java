package com.jamesreaver.util;

import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebFetcher {

    /**
     * Extract and validate links from the given document.
     * Links are only retrieved from the href attribute of anchor tags,
     * with the segment and trailing slash removed and only where
     * both the protocol and the host match that of the root url.
     *
     * @param  url         the url to retrieve
     * @return             the document at the given url
     * @throws IOException when the request fails
     */
    public static Document fetch(URL url) throws IOException {
        return Jsoup.connect(url.toString()).get();
    }
}
