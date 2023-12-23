package com.moraes.authenticator.api.mock;

import java.util.Base64;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.BasicToken;

public class MockBasicToken extends AbstractMock<BasicToken> {

    /**
     * 123456
     */
    public final static String PASSWORD = "{pbkdf2}f88acbc6eccb60d08e92318843587d22533a955d8e8d6246a360d23b9dfacbb2dfab247eb1d0a9c5";

    public final static String PASSWORD_DECRYPTED = "123456";

    public final static String USERNAME = "test";

    @Override
    protected Class<BasicToken> getClazz() {
        return BasicToken.class;
    }

    @Override
    protected void setOdersValues(BasicToken entity, Integer number) {
        entity.setPassword(PASSWORD);
    }

    public String getBasicTokenHash() {
        return new String(Base64.getEncoder().encode(new String(String.format("%s:%s", USERNAME, PASSWORD_DECRYPTED)).getBytes()));
    }

}
