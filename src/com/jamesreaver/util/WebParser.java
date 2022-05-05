package com.jamesreaver.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.nodes.Document;

import static com.jamesreaver.util.Constants.ANCHOR_TAG;
import static com.jamesreaver.util.Constants.HREF_ATTR;

public class WebParser {

    /**
     * Matches filenames whose extentions may not be crawled.
     */
    private static final Pattern fileExtensions = Pattern.compile(
            ".{0,}(\\.pdf|\\.png|\\.jpg|\\.jpeg|\\.mp3|\\.m4a|\\.css|\\.js)$"
    );

    /**
     * Extract and validate links from the given document.
     * Links are only retrieved from the href attribute of anchor tags,
     * with the segment and trailing slash removed and only where
     * both the protocol and the host match that of the root url.
     *
     * @param  doc      the web document from which to extract links
     * @param  rootUrl  the url representing the root domain
     * @return          a set of all valid URLs found on the document
     */
    public static Set<URL> extractLinks(Document doc, URL rootUrl) {
        Stream<String> rawLinks = doc.select(ANCHOR_TAG).stream()
                .map(element -> element.absUrl(HREF_ATTR));
        return rawLinks
                .map(WebParser::toUrl)
                .filter(url -> validate(url, rootUrl))
                .collect(Collectors.toSet());
    }

    private static URL toUrl(String rawLink) {
        try {
            URL url = new URL(rawLink);
            return removeSegment(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static boolean validate(URL url, URL rootURL) {
        return url != null
                && url.getHost().equals(rootURL.getHost())
                && url.getProtocol().equals(rootURL.getProtocol())
                && !fileExtensions.matcher(url.getFile()).matches();
    }

    private static URL removeSegment(URL url) throws MalformedURLException {
        return new URL(url.getProtocol(), url.getHost(), removeTrailingSlash(url.getPath()));
    }

    private static String removeTrailingSlash(String path) {
        return path.endsWith("/") ? path.substring(0, path.length()-1) : path;
    }
}
