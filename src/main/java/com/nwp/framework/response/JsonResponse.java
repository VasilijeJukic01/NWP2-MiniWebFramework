package com.nwp.framework.response;

import com.google.gson.Gson;
import java.util.stream.Collectors;

public class JsonResponse extends Response {

    private final Gson gson;
    private final Object jsonObject;

    public JsonResponse(Object jsonObject) {
        this.gson = new Gson();
        this.jsonObject = jsonObject;
    }

    @Override
    public String render() {
        String headers = this.header.getKeys().stream()
                .map(key -> key + ":" + this.header.get(key))
                .collect(Collectors.joining("\n"));

        return "HTTP/1.1 200 OK\n" + headers + "\n\n" + this.gson.toJson(this.jsonObject);
    }
}