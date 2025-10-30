package com.cryptofrypto.service.marketfeed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "requestMessage")
class MarketfeedApplicationTests {

    @Autowired
    public FeedApplicationMessageConsumer consumer;

    @Test
    void contextLoads() {
        consumer.createFeed("BTC-USD");
        consumer.startFeed("BTC-USD");
    }

}
