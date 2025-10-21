package com.cryptofrypto.service.marketfeed.Producer;

import org.springframework.kafka.core.KafkaTemplate;

public class CoinbaseWebsocketFeed implements Runnable {
    public static enum RunningStatus {
        RUNNING, STOPPED;
    }

    private RunningStatus runningStatus = RunningStatus.STOPPED;

    private final KafkaTemplate<String,String> kafkaTemplate;

    public CoinbaseWebsocketFeed(KafkaTemplate<String,String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run() {
        runningStatus = RunningStatus.RUNNING;
    }

    public RunningStatus getRunningStatus() {
        return runningStatus;
    }
}
