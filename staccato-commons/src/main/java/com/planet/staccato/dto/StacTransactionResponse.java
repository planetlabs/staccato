package com.planet.staccato.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Model for responses from the transaction API.
 *
 * @author joshfix
 * Created on 12/6/17
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StacTransactionResponse {
    private String id;
    private boolean success;
    private Object result;
    private String reason;

    public StacTransactionResponse id(String id) {
        this.id = id;
        return this;
    }

    public StacTransactionResponse success(boolean success) {
        this.success = success;
        return this;
    }

    public StacTransactionResponse result(Object result) {
        this.result = result;
        return this;
    }

    public StacTransactionResponse reason(String reason) {
        this.reason = reason;
        return this;
    }
}
