package com.moraes.authenticator.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionUtilDTO {

    private boolean condition;
    private String messageKey;
    private String[] messageParams;

    public boolean isNotCondition() {
        return !condition;
    }

    public boolean containsMessageParams() {
        return messageParams != null && messageParams.length > 0;
    }

    public static String[] messageParamsBuild(String... messageParams) {
        return messageParams;
    }
}
