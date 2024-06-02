package com.moraes.authenticator.api.model.enums;

import com.moraes.authenticator.api.model.interfaces.IDescription;
import com.moraes.authenticator.api.util.MessagesUtil;

import lombok.Getter;

public enum FrequencyEnum implements IDescription {
    SIGNAL("frequency_enum.signal"),
    ONLY("frequency_enum.only"),
    MONTHLY("frequency_enum.monthly"),
    SEMIANNUAL("frequency_enum.semiannual"),
    YEARLY("frequency_enum.yearly"),
    FINANCING("frequency_enum.financing");

    @Getter
    private final String messageKey;

    private FrequencyEnum(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getDescription() {
        return MessagesUtil.getMessage(messageKey);
    }
}
