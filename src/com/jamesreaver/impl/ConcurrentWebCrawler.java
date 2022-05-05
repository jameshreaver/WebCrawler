package com.jamesreaver.impl;

import com.jamesreaver.api.WebCrawler;
import com.jamesreaver.util.WebFetcher;
import com.jamesreaver.util.WebParser;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import org.jsoup.nodes.Document;

import static com.jamesreaver.util.Constants.TASK_COUNT_MESSAGE;

/**
 * An implementation of {@WebCrawler} which employs multiple threads
 * to perform scraping in a breadth-first fashion.
 */
public class ConcurrentWebCrawler implements WebCrawler {

    private final ExecutorService executor;
    private final boolean verbose;

    public ConcurrentWebCrawler(ExecutorService executor, boolean verbose) {
        this.executor = executor;
        this.verbose = verbose;
    }

    @Override
    public Map<URL, Set<URL>> execute(URL rootUrl) {
        Map<URL, Set<URL>> visited = new ConcurrentHashMap<>();
        AtomicInteger taskCount = new AtomicInteger();
        crawl(rootUrl, visited, taskCount);
        while (taskCount.get() > 0);
        return visited;
    }

    private void crawl(URL url, Map<URL, Set<URL>> visited, AtomicInteger taskCount) {
        if (visited.putIfAbsent(url, Collections.emptySet()) == null) {
            taskCount.incrementAndGet();
            executor.submit(crawlTask(url, visited, taskCount));
            printTaskCount(taskCount);
        }
    }

    private Runnable crawlTask(URL url, Map<URL, Set<URL>> visited, AtomicInteger taskCount) {
        return () -> {
            try {
                Document doc = WebFetcher.fetch(url);
                Set<URL> links = WebParser.extractLinks(doc, url);
                visited.put(url, links);
                links.forEach(link -> crawl(link, visited, taskCount));
            } catch (IOException e) {
                printException(e);
            } finally {
                taskCount.decrementAndGet();
            }
        };
    }

    private void printTaskCount(AtomicInteger taskCount) {
        System.out.printf(TASK_COUNT_MESSAGE, taskCount.get());
    }

    private void printException(Exception e) {
        if (verbose) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
