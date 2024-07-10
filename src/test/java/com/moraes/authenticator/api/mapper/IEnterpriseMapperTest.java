package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.mock.real_state.MockEnterprise;
import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.model.real_state.Enterprise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

class IEnterpriseMapperTest {

	@Spy
	@InjectMocks
	private IEnterpriseMapper mapper = Mappers.getMapper(IEnterpriseMapper.class);

	private MockEnterprise input;

	private static final Long KEY = 1L;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		input = new MockEnterprise();
	}

	@Test
	@DisplayName("JUnit test given enterprise and enterpriseDTO when updateFromEnterpriseDTO then update enterprise")
	void testGivenEnterpriseAndEnterpriseDTOWhenUpdateFromEnterpriseDTOThenUpdateEnterprise() {
		Enterprise enterprise = input.mockEntity(1);
		enterprise.setKey(KEY);
		enterprise.setConstruction(null);
		enterprise.setProposals(null);
		EnterpriseDTO enterpriseDTO = input.mockEnterpriseDTO(2);

		mapper.updateFromEnterpriseDTO(enterprise, enterpriseDTO);

		assertEquals(enterpriseDTO.getName(), enterprise.getName(), "Name is different");
		assertEquals(enterpriseDTO.getUnit(), enterprise.getUnit(), "Unit is different");
		assertEquals(enterpriseDTO.getValue().doubleValue(), enterprise.getValue().doubleValue(), "Value is different");
		assertEquals(enterpriseDTO.getVpl().doubleValue(), enterprise.getVpl().doubleValue(), "Vpl is different");
		assertEquals(enterpriseDTO.getValueM2().doubleValue(), enterprise.getValueM2().doubleValue(),
				"ValueM2 is different");
		assertEquals(enterpriseDTO.getSizeM2().doubleValue(), enterprise.getSizeM2().doubleValue(),
				"SizeM2 is different");
		assertEquals(KEY, enterprise.getKey(), "Key is different");
		assertNotNull(enterprise.getDate(), "Date is null");
		assertNull(enterprise.getConstruction(), "Construction is not null");
		assertNull(enterprise.getProposals(), "Proposals is not null");
	}
}