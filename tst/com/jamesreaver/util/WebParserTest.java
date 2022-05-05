package com.jamesreaver.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebParserTest {

    @Test
    public void extractLinks() throws MalformedURLException {
        // given
        URL rootUrl = new URL("https://www.example.com");
        URL subUrl = new URL("https://www.example.com/test");
        Document doc = Document.createShell(rootUrl.toString());
        doc.body().append(createAnchor(subUrl));

        // when
        Set<URL> urls = WebParser.extractLinks(doc, rootUrl);

        // then
        assertEquals(1, urls.size());
        assertTrue(urls.contains(subUrl));
    }

    @Test
    public void extractLinksRelativeUrl() throws MalformedURLException {
        // given
        URL rootUrl = new URL("https://www.example.com");
        URL subUrl = new URL("https://www.example.com/test");
        Document doc = Document.createShell(rootUrl.toString());
        doc.body().append(createAnchor("/test"));

        // when
        Set<URL> urls = WebParser.extractLinks(doc, rootUrl);

        // then
        assertEquals(1, urls.size());
        assertTrue(urls.contains(subUrl));
    }

    @Test
    public void extractLinksRemoveSegmentAndTrailingSlash() throws MalformedURLException {
        // given
        URL rootUrl = new URL("https://www.example.com");
        URL subUrl = new URL("https://www.example.com/#segment");
        Document doc = Document.createShell(rootUrl.toString());
        doc.body().append(createAnchor(subUrl));

        // when
        Set<URL> urls = WebParser.extractLinks(doc, rootUrl);

        // then
        assertEquals(1, urls.size());
        assertTrue(urls.contains(rootUrl));
    }

    @Test
    public void extractLinksDifferentHost() throws MalformedURLException {
        // given
        URL rootUrl = new URL("https://www.example.com");
        URL subUrl = new URL("https://other.example.com");
        Document doc = Document.createShell(rootUrl.toString());
        doc.body().append(createAnchor(subUrl));

        // when
        Set<URL> urls = WebParser.extractLinks(doc, rootUrl);

        // then
        assertTrue(urls.isEmpty());
    }

    @Test
    public void extractLinksDifferentProtocol() throws MalformedURLException {
        // given
        URL rootUrl = new URL("https://www.example.com");
        URL subUrl = new URL("http://www.example.com");
        Document doc = Document.createShell(rootUrl.toString());
        doc.body().append(createAnchor(subUrl));

        // when
        Set<URL> urls = WebParser.extractLinks(doc, rootUrl);

        // then
        assertTrue(urls.isEmpty());
    }

    @Test
    public void extractLinksIgnoreAssets() throws MalformedURLException {
        // given
        URL rootUrl = new URL("https://www.example.com");
        URL subUrl = new URL("https://www.example.com/image.png");
        Document doc = Document.createShell(rootUrl.toString());
        doc.body().append(createAnchor(subUrl));

        // when
        Set<URL> urls = WebParser.extractLinks(doc, rootUrl);

        // then
        assertTrue(urls.isEmpty());
    }

    @Test
    public void extractLinksInvalidUrl() throws MalformedURLException {
        // given
        URL rootUrl = new URL("https://www.example.com");
        Document doc = Document.createShell(rootUrl.toString());
        doc.body().append(createAnchor("other://www.example.com"));

        // when
        Set<URL> urls = WebParser.extractLinks(doc, rootUrl);

        // then
        assertTrue(urls.isEmpty());
    }

    private static String createAnchor(URL url) {
        return "<a href=\"" + url + "\"></a>";
    }

    private static String createAnchor(String url) {
        return "<a href=\"" + url + "\"></a>";
    }

}
