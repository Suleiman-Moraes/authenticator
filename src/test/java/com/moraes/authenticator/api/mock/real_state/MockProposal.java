package com.moraes.authenticator.api.mock.real_state;

import java.math.BigDecimal;
import java.util.List;

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

    public ProposalDTO mockProposalDTOWrongValues() {
        return ProposalDTO.builder()
                .value(BigDecimal.ZERO)
                .vpl(BigDecimal.valueOf(-1))
                .valueM2(BigDecimal.ZERO)
                .sizeM2(BigDecimal.ZERO)
                .enterprise(new MockEnterprise().mockEnterpriseDTOWrongValues())
                .conditions(List.of(new MockCondition().mockConditionDTOWrongValues()))
                .build();
    }

    public ProposalDTO mockIntegrationProposalDTO() {
        try {
            ProposalDTO entity = new ProposalDTO();
            MockUtil.toFill(entity, 99, ignoreFields);
            // setOdersValues
            entity.setEnterprise(new MockEnterprise().mockIntegrationEnterpriseDTO());
            entity.setConditions(new MockCondition().mockConditionDTOList());
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}
