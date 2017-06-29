package com.logsentinel;

public class PlainTextSerializer implements BodySerializer {

    @Override
    public String serialize(Object text) {
        return text.toString();
    }

}
