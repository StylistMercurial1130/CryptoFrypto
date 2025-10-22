package com.cryptofrypto.service.marketfeed.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RequestMessage extends SerializableModel {
    
    @JsonProperty("type")
    public final String type;

    @JsonProperty("channels")
    public final List<String> channels;

    @JsonProperty("product_ids")
    public final List<String> productIds;     

    public RequestMessage(String type,List<String> channels,List<String> productIds) {
        this.type = type;
        this.channels = channels;
        this.productIds = productIds;
    }

    public static RequestMessage SubscribeMessage(List<String> channels,List<String> productIds) {
        return new RequestMessage("subscribe", channels, productIds);
    }

    public static RequestMessage UnsubscribeMessage(List<String> channels, List<String> productIds) {
        return new RequestMessage("unsubscribe", channels, productIds);
    }
}
