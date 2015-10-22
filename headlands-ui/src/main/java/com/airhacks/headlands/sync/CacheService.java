package com.airhacks.headlands.sync;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author airhacks.com
 */
public class CacheService {

    private Client client;
    private String resource = "/headlands/resources/caches";

    @PostConstruct
    public void initialize() {
        this.client = ClientBuilder.newClient();
    }

    public void sync(String inputText, String outputText) {

    }

}
