package com.jamesreaver.impl;

import com.jamesreaver.api.WebCrawler;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConcurrentWebCrawlerTest {

    private static final ExecutorService executor = Executors.newFixedThreadPool(2);

    @AfterClass
    public static void tearDown() {
        executor.shutdown();
    }

    @Test
    public void crawl() throws MalformedURLException {
        // given
        URL rootUrl = new URL("http://klee.doc.ic.ac.uk");
        WebCrawler crawler = new ConcurrentWebCrawler(executor, false);

        // when
        Map<URL, Set<URL>> urls = crawler.execute(rootUrl);

        // then
        assertFalse(urls.isEmpty());
        assertTrue(urls.containsKey(rootUrl));
        urls.get(rootUrl).forEach(url -> assertSameDomain(url, rootUrl));
    }

    @Test
    public void crawlInvalidUrl() throws MalformedURLException {
        // given
        PrintStream errorStream = System.err;
        URL rootUrl = new URL("https://www.example.com:80");
        WebCrawler crawler = new ConcurrentWebCrawler(executor, true);
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        // when
        Map<URL, Set<URL>> urls = crawler.execute(rootUrl);

        // then
        assertEquals(1, urls.size());
        assertTrue(urls.get(rootUrl).isEmpty());
        System.setErr(errorStream);
    }

    public static void assertSameDomain(URL url, URL rootUrl) {
        assertTrue(url.toString().contains(rootUrl.getHost()));
    }
}
