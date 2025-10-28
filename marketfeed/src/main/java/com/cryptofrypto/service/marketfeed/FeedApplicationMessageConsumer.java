package com.cryptofrypto.service.marketfeed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.cryptofrypto.service.marketfeed.models.OrchestratorMessage;
import com.cryptofrypto.service.marketfeed.producer.CoinbaseProducerService;

@Component
public class FeedApplicationMessageConsumer {

    @Autowired
    public CoinbaseProducerService producerService;

    @KafkaListener(id = "request-listener", topics = "${consumer.request.topic}")
    public void consumeRequestMessage(String requestMessage) {
        try {
            var request = OrchestratorMessage.fromString(requestMessage);

            // for now only handling create feed and starting feed automatically, later will
            // add complexity
            // keep things simple for now for testing and checking if feed works

            createFeed(request.coinName);
            startFeed(request.coinName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFeed(String coinName) {
        producerService.createFeed(coinName);
    }

    public void startFeed(String coinName) {
        producerService.startFeed(coinName);
    }

    public void stopFeed(String coinName) {
        producerService.stopFeed(coinName);
    }
}
