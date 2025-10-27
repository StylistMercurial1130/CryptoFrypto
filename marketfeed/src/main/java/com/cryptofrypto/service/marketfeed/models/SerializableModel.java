package com.cryptofrypto.service.marketfeed.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SerializableModel {

    public String serialize() throws JsonProcessingException {
        try {
            var objMapper = new ObjectMapper();
            return objMapper.writeValueAsString(this);

        } catch (RuntimeException e) {
            throw e;
        }
    }
}
