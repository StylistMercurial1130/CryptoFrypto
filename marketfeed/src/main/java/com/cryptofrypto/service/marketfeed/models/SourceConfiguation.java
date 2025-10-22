package com.cryptofrypto.service.marketfeed.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SourceConfiguation {

    @JsonProperty("channels")
    public final List<String> channels;

    @JsonProperty("coinNames")
    public final List<String> coinNames;

    public SourceConfiguation(List<String> channels,List<String> coinNames) {
        this.channels = channels;
        this.coinNames = coinNames;
    }
}
