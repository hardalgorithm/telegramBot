package com.gricko.telegram.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.net.URL;



public class JsouParser {

    @Scheduled(fixedRate = 86400000)
    public String sendQuote() throws IOException {


       Document page = getPage();
       String quote = page.select("div[class=clearfix]").first().text();

        return quote;
    }


    private Document getPage() throws IOException {
        String url = "https://www.brainyquote.com/topics/every-day-quotes";
        Document page = Jsoup.parse(new URL(url),5000);
        return page;
    }


}
