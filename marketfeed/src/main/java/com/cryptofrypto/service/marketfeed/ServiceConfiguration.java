package com.cryptofrypto.service.marketfeed;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Value("${threadpool.configuration.corePoolSize}")
    public int corePoolSize;

    @Value("${threadpool.configuration.maxPoolSize}")
    public int maxPoolSize;

    @Value("${threadpool.configuration.queueCapacity}")
    public int queueCapacity;

    public Logger logger = LoggerFactory.getLogger(ServiceConfiguration.class);

    @Bean(name = "MarketFeedThreadPool")
    public ThreadPoolExecutor createThreadPoolExecutor() {
        logger.info("creating thread pool : corePoolSize={}, maxPoolSize={}, queueCapacity={}",
                corePoolSize, maxPoolSize, queueCapacity);

        return new ThreadPoolExecutor(
            corePoolSize, 
            maxPoolSize, 
            corePoolSize,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(queueCapacity));
    }
}
