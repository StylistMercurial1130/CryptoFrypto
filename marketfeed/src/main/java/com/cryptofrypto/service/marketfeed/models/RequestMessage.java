package com.cryptofrypto.service.marketfeed.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RequestMessage extends SerializableModel {
    
    @JsonProperty("type")
    public final String type;

    @JsonProperty("channel")
    public final String channel;

    @JsonProperty("product_ids")
    public final List<String> productIds;     

    public RequestMessage(String type,String channel,List<String> productIds) {
        this.type = type;
        this.channel = channel;
        this.productIds = productIds;
    }

    public static RequestMessage SubscribeMessage(String channel,List<String> productIds) {
        return new RequestMessage("subscribe", channel, productIds);
    }

    public static RequestMessage UnsubscribeMessage(String channel, List<String> productIds) {
        return new RequestMessage("unsubscribe", channel, productIds);
    }
}
