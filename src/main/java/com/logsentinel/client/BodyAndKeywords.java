package com.logsentinel.client;

import java.util.List;

public class BodyAndKeywords {

    private String body;
    private List<String> keywords;

    public BodyAndKeywords(String body, List<String> keywords) {
        this.body = body;
        this.keywords = keywords;
    }

    public String getBody() {
        return body;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
