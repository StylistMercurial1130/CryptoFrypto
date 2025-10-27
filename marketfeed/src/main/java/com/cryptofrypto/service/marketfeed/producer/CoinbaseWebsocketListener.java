package com.cryptofrypto.service.marketfeed.producer;

import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.cryptofrypto.service.marketfeed.models.RequestMessage;
import com.cryptofrypto.service.marketfeed.models.SourceConfiguation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CoinbaseWebsocketListener implements WebSocket.Listener {

    public final KafkaTemplate<String, String> kafkaTemplate;
    public final SourceConfiguation sourceConfiguation;
    public final String resourceUri;
    public final Logger logger = LoggerFactory.getLogger(CoinbaseWebsocketListener.class);
    public final String topic;

    public CoinbaseWebsocketListener(
            String resourceUri,
            KafkaTemplate<String, String> kafkaTemplate,
            SourceConfiguation sourceConfiguation,
            String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.sourceConfiguation = sourceConfiguation;
        this.resourceUri = resourceUri;
        this.topic = topic;
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
        try {
            ObjectMapper objMapper = new ObjectMapper();

            var feedData = objMapper.readValue(data.toString(), new TypeReference<HashMap<String, String>>() {
            });

            switch (feedData.get("type")) {
                case "heartbeat": {
                    // ignore for now
                    WebSocket.Listener.super.onText(webSocket, data, last);
                    return null;
                }
                case "ticker": {

                    var key = sourceConfiguation.coinNames.size() > 0
                            ? sourceConfiguation.coinNames.stream().collect(Collectors.joining(" | "))
                            : sourceConfiguation.coinNames.getFirst();

                    kafkaTemplate.send(topic, key, data.toString());

                    return WebSocket.Listener.super.onText(webSocket, data, last);
                }
                case "error": {
                    var requestMessage = RequestMessage.UnsubscribeMessage(
                            sourceConfiguation.channels,
                            sourceConfiguation.coinNames);

                    try {
                        var byteBuffer = ByteBuffer.wrap(requestMessage.serialize().getBytes(StandardCharsets.UTF_8));

                        webSocket.sendBinary(byteBuffer, true);
                        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "");

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    throw new RuntimeException(feedData.get("message"));
                }
                default: {
                    WebSocket.Listener.super.onText(webSocket, data, last);
                    return null;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
