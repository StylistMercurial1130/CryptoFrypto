package com.cryptofrypto.service.marketfeed.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrchestratorMessage {

    public final String coinName;

    @JsonCreator
    public OrchestratorMessage(
            @JsonProperty("coinName") String coinName) {
        this.coinName = coinName;
    }

    public static OrchestratorMessage fromString(String input) throws Exception {
        ObjectMapper objMapper = new ObjectMapper();

        try {
            return objMapper.readValue(input, OrchestratorMessage.class);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (JsonProcessingException e) {
            throw new Exception(e);
        }
    }

    public String getCoinName() {
        return coinName;
    }
}
