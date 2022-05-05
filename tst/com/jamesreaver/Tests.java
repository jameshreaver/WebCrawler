package com.jamesreaver;

import com.jamesreaver.impl.ConcurrentWebCrawlerTest;
import com.jamesreaver.util.WebFetcherTest;
import com.jamesreaver.util.WebParserTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MainTest.class,
        ConcurrentWebCrawlerTest.class,
        WebFetcherTest.class,
        WebParserTest.class
})
public class Tests {};