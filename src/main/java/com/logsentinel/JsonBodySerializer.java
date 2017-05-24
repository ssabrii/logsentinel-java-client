package com.logsentinel;

public class JsonBodySerializer implements BodySerializer {
    private JSON json;
    
    public JsonBodySerializer(JSON json) {
        this.json = json;
    }
    
    @Override
    public String serialize(Object object) {
        return json.serialize(object);
    }

}
