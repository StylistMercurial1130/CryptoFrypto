package com.cryptofrypto.service.marketfeed.producer;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CoinbaseProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${producer.coinbase.source.marketdata.sandbox}")
    private String resourceUri;

    @Value("${producer.coinbase.configuration.topic}")
    private String kafkaTopic;

    private final Logger logger = LoggerFactory.getLogger(CoinbaseProducerService.class);
    private final Map<String, CoinbaseWebsocketFeed> marketFeedMap = new ConcurrentHashMap<>();
    private final HttpClient httpClient;

    public CoinbaseProducerService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public void createFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName)) {
            logger.info("creating feed for {}", coinName);
            marketFeedMap.put(coinName, new CoinbaseWebsocketFeed(resourceUri, kafkaTemplate, coinName, kafkaTopic,httpClient));
        }
    }

    public void createFeed(List<String> coinNames) {
        for (var coinName : coinNames) {
            this.createFeed(coinName);
        }
    }

    public void startFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName))
            return;

        marketFeedMap.get(coinName).connect();
    }

    public void startAllFeeds() {
        logger.info("starting all feeds");

        for (var entry : marketFeedMap.entrySet()) {
            logger.info("starting feed {}", entry.getKey());
            entry.getValue().connect();
        }
    }

    public void stopFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName))
            return;

        logger.info("stopping feed {}", coinName);

        marketFeedMap.get(coinName).close();
    }

    public void stopAllFeeds() {
        logger.info("stopping all feeds");

        for (var entry : marketFeedMap.entrySet()) {
            logger.info("stopping feed {}", entry.getKey());

            entry.getValue().close();
        }
    }
}
