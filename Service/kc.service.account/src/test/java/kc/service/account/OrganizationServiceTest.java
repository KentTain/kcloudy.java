package kc.service.account;

import java.util.List;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.dto.account.OrganizationDTO;

//@Disabled
@DisplayName("组织架构管理")
class OrganizationServiceTest extends AccountTestBase {

    @Autowired
    private IOrganizationService organizationService;

    @Test
    void test_GetRootOrganizationsByName() {
        //TenantContext.setCurrentTenant(TestTenant);

        List<OrganizationDTO> data = organizationService.findRootOrganizationsByName("采购");
        assertTrue(data.size() > 0);
        printTreeNodeDTOs(data);
    }

    @Test
    void test_GetOrganizationsByUserId() {
        //TenantContext.setCurrentTenant(TestTenant);

        List<OrganizationDTO> data = organizationService.findOrganizationsByUserId("ede9edf9-5909-42d0-8563-aaa5c04cd8c8");
        assertTrue(data.size() > 0);
        printTreeNodeDTOs(data);
    }
}
