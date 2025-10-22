package com.cryptofrypto.service.marketfeed.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SerializableModel {
    
    public String serialize() {
        var objMapper = new ObjectMapper();
        return objMapper.writeValueAsString(this);
    }
}
