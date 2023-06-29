package com.byborgip.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

public class HttpUtil {


    private final static HttpClient httpClient = HttpClient.newBuilder().build();


    public CompletableFuture<HttpResponse<String>> post(final String url, final String body) throws URISyntaxException {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> get(final String url, final int timeout) throws URISyntaxException {
        final HttpRequest request = HttpRequest.newBuilder(new URI(url))
                .GET()
                .timeout(Duration.of(timeout, ChronoUnit.SECONDS))
                .build();
        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

}
