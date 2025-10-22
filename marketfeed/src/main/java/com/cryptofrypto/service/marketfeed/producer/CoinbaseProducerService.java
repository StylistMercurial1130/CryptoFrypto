package com.cryptofrypto.service.marketfeed.producer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CoinbaseProducerService {
    
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Value("${producer.coinbase.source.marketdata.sandbox}")
    private String resourceUri;

    private final Logger logger = LoggerFactory.getLogger(CoinbaseProducerService.class); 
    
    private final Map<String,CoinbaseWebsocketFeed> marketFeedMap = new ConcurrentHashMap<>();

    private final ThreadPoolExecutor taskExecutor;

    public CoinbaseProducerService(@Qualifier("MarketFeedThreadPool") ThreadPoolExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    public void createFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName)) {
            logger.info("creating feed for {}",coinName);
            marketFeedMap.put(coinName,new CoinbaseWebsocketFeed(resourceUri,kafkaTemplate,coinName));
        }
    }

    public void startFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName)) 
            return;

        marketFeedMap.get(coinName).connect(); 
    }

    public void startAllFeeds() {
        for (var entry : marketFeedMap.entrySet()) {
            entry.getValue().connect();
        }
    }

    public void stopFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName))
            return;
        
        marketFeedMap.get(coinName).close();
    }

    public void stopAllFeeds() {
        for (var entry : marketFeedMap.entrySet()) {
            entry.getValue().close();
        }
    }
}
