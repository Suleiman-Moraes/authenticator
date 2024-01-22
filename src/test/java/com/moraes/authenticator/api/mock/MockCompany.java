package com.moraes.authenticator.api.mock;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.Company;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockCompany extends AbstractMock<Company> {

    @Override
    protected Class<Company> getClazz() {
        return Company.class;
    }

    @Override
    protected void setOdersValues(Company entity, Integer number) {
        log.info("setOdersValues: {}", number);
    }
}
