package com.moraes.authenticator.api.mock.real_state;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public ProposalDTO mockProposalDTO(int number) {
        try {
            ProposalDTO entity = new ProposalDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setEnterprise(new MockEnterprise().mockEnterpriseDTO(number));
            entity.setConditions(new MockCondition().mockConditionDTOList());
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}
