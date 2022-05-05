package com.jamesreaver.util;

import java.io.IOException;
import java.net.URL;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class WebFetcherTest {

    @Test
    public void fetch() throws IOException {
        // given
        URL url = new URL("https://www.example.com");

        // when
        Document doc = WebFetcher.fetch(url);

        // then
        assertTrue(doc.hasText());
        assertEquals(url.toString(), doc.location());
        assertEquals("Example Domain", doc.title());
        assertTrue(doc.html().contains("for use in illustrative examples"));
    }

    @Test(expected = IOException.class)
    public void fetchException() throws IOException {
        // given invalid port
        URL url = new URL("https://www.example.com:80");

        // when
        WebFetcher.fetch(url);

        // then exception
    }

}