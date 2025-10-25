package com.cryptofrypto.service.marketfeed.producer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.cryptofrypto.service.marketfeed.models.SourceConfiguation;

public class CoinbaseWebsocketFeed {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String coinName;
    private final String topic;
    private final HttpClient httpClient;

    private WebSocket ws;
    private String resourceUri;
    private Logger logger = LoggerFactory.getLogger(CoinbaseWebsocketFeed.class);

    public CoinbaseWebsocketFeed(
            String resourceUri,
            KafkaTemplate<String, String> kafkaTemplate,
            String coinName,
            String topic,
            HttpClient httpClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.coinName = coinName;
        this.resourceUri = resourceUri;
        this.topic = topic;
        this.httpClient = httpClient;
    }

    public void connect() {
        logger.info("connecting websocket feed for coin {}", coinName);

        var sourceConfig = new SourceConfiguation(List.of("ticker"), List.of(coinName));

        ws = httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(resourceUri),
                        new CoinbaseWebsocketListener(resourceUri, kafkaTemplate, sourceConfig, topic))
                .join();
    }

    public void close() {
        logger.info("closing websocket for feed {}", coinName);

        ws.sendClose(WebSocket.NORMAL_CLOSURE, "");
    }
}
