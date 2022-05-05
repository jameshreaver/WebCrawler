package com.jamesreaver;

import com.jamesreaver.api.WebCrawler;
import com.jamesreaver.impl.ConcurrentWebCrawler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jamesreaver.util.Constants.REQUEST_ERROR;
import static com.jamesreaver.util.Constants.REQUEST_INPUT;
import static com.jamesreaver.util.Constants.OUTPUT_HEADER;
import static com.jamesreaver.util.Constants.OUTPUT_LINK;

public class Main {

    public static final short NUM_THREADS = 30;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        WebCrawler crawler = new ConcurrentWebCrawler(executor, false);
        URL url = getUrl(scanner);
        while (url != null) {
            printUrls(crawler.execute(url));
            url = getUrl(scanner);
        }
        executor.shutdown();
    }

    private static URL getUrl(Scanner scanner) {
        System.out.println(REQUEST_INPUT);
        String input = scanner.nextLine();
        if (input.isEmpty()) return null;
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            System.out.printf(REQUEST_ERROR, e.getMessage());
            return getUrl(scanner);
        }
    }

    private static void printUrls(Map<URL, Set<URL>> urls) {
        System.out.println(OUTPUT_HEADER);
        urls.keySet().forEach(url -> {
            System.out.println(url.toString());
            urls.get(url).forEach(link -> System.out.printf(OUTPUT_LINK, link));
        });
    }
}
