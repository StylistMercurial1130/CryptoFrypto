package com.cryptofrypto.service.marketfeed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeedApplicationRunner implements ApplicationRunner {

    @SuppressWarnings("unused")
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private final Logger logger = LoggerFactory.getLogger(FeedApplicationRunner.class); 
    
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("starting coinbase feed");
    }


}
