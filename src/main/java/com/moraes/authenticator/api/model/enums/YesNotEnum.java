package com.moraes.authenticator.api.model.enums;

import com.moraes.authenticator.api.model.interfaces.IDescription;

import lombok.Getter;

@Getter
public enum YesNotEnum implements IDescription {

    YES("Yes"),
    NOT("Not");

    private String description;

    private YesNotEnum(String description) {
        this.description = description;
    }
}
