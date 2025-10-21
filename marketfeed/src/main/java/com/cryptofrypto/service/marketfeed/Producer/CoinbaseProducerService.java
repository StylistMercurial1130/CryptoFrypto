package com.cryptofrypto.service.marketfeed.Producer;

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

    private final Map<String,Future<?>> runnningFeeds = new ConcurrentHashMap<>();

    private final ThreadPoolExecutor taskExecutor;

    public CoinbaseProducerService(@Qualifier("MarketFeedThreadPool") ThreadPoolExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    public void createFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName)) {
            logger.info("creating feed for {}",coinName);
            marketFeedMap.put(coinName,new CoinbaseWebsocketFeed(kafkaTemplate));
        }
    }

    public void startFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName)) 
            return;

        if (!runnningFeeds.containsKey(coinName)) {
            logger.info("starting feed for {}",coinName);
            runnningFeeds.put(
                coinName, 
                taskExecutor.submit(marketFeedMap.get(coinName))
            );
        }
    }

    public void startAllFeeds() {
        for (var entry : marketFeedMap.entrySet()) {
            if (!runnningFeeds.containsKey(entry.getKey())) {
                logger.info("start feed for {}",entry.getKey());
                
                runnningFeeds.put(
                    entry.getKey(),
                    taskExecutor.submit(entry.getValue())
                );
            }
        }
    }

    public void stopFeed(String coinName) {
        if (!marketFeedMap.containsKey(coinName))
            return;
        
        if (runnningFeeds.containsKey(coinName)) {
            logger.info("stopping feed for {}",coinName);

            runnningFeeds.get(coinName).cancel(true);
            runnningFeeds.remove(coinName);
        } 
    }

    public void stopAllFeeds() {
        for (var entry : marketFeedMap.entrySet()) {
            if (!runnningFeeds.containsKey(entry.getKey())) {
                logger.info("stopping feed for {}",entry.getKey());

                runnningFeeds.get(entry.getKey()).cancel(true);
                runnningFeeds.remove(entry.getKey());
            }
        }
    }
}
