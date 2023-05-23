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

    public boolean isNotCondition() {
        return !condition;
    }
}
