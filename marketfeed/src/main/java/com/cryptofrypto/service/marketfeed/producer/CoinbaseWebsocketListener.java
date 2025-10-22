package com.cryptofrypto.service.marketfeed.producer;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.cryptofrypto.service.marketfeed.models.RequestMessage;
import com.cryptofrypto.service.marketfeed.models.SourceConfiguation;

public class CoinbaseWebsocketListener implements WebSocket.Listener {

    public final KafkaTemplate<String,String> kafkaTemplate;

    public final SourceConfiguation sourceConfiguation;

    public final String resourceUri;

    public final Logger logger = LoggerFactory.getLogger(CoinbaseWebsocketListener.class);

    public CoinbaseWebsocketListener(
        String resourceUri,
        KafkaTemplate<String,String> kafkaTemplate,
        SourceConfiguation sourceConfiguation
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.sourceConfiguation = sourceConfiguation;
        this.resourceUri = resourceUri;
    }
    @Override
    public void onOpen(WebSocket webSocket) {
        var requestMessage = RequestMessage.SubscribeMessage(sourceConfiguation.channels, sourceConfiguation.coinNames);

        try {
            var byteBuffer = ByteBuffer.wrap(requestMessage.serialize().getBytes(StandardCharsets.UTF_8));
            webSocket.sendBinary(byteBuffer, true);
        } catch (Exception e) {
            throw new RuntimeException(e); 
        }
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
    }
}
