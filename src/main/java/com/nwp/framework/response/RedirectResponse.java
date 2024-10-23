package com.nwp.framework.response;

import java.util.stream.Collectors;

public class RedirectResponse extends Response {

    private final String url;

    public RedirectResponse(String url) {
        this.url = url;
        this.header.add("location", url);
    }

    @Override
    public String render() {
        String headers = this.header.getKeys().stream()
                .map(key -> key + ":" + this.header.get(key))
                .collect(Collectors.joining("\n"));

        return "HTTP/1.1 301 redirect\n" + headers + "\n";
    }
}