package com.moraes.authenticator.api.mock.real_state;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.real_state.Proposal;

import lombok.Getter;
import lombok.Setter;

public class MockProposal extends AbstractMock<Proposal> {

    @Setter
    @Getter
    private int maxSize = 14;

    @Override
    protected Class<Proposal> getClazz() {
        return Proposal.class;
    }

    @Override
    protected void setOdersValues(Proposal entity, Integer number) {
        entity.setEnterprise(new MockEnterprise().mockEntity(number));
    }
}
