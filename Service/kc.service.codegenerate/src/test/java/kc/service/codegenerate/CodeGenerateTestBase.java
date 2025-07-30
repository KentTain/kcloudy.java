package kc.service.codegenerate;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.dto.TreeNodeDTO;
import kc.framework.GlobalConfigData;
import kc.framework.TestBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import kc.service.webapiservice.thridparty.impl.GlobalConfigApiService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = AppTest.class)
abstract class CodeGenerateTestBase extends TestBase {
    @Autowired
    private IGlobalConfigApiService globalConfigApiService;

    @BeforeEach
    void setUpBeforeClass() {
        GlobalConfigData configData = globalConfigApiService.GetGlobalConfigData();
        intilize(configData);

        TenantDataSourceProvider.addDataSource(DbaTenant);
        TenantDataSourceProvider.addDataSource(TestTenant);
    }

    @AfterEach
    void setDownAfterClass() {
        tearDown();

        TenantDataSourceProvider.clearDataSource();
    }

    /**
     * 打印树结构
     *
     * @param nodes
     */
    public <T extends TreeNodeDTO<T>> void printTreeNodeDTOs(List<T> nodes) {
        for (T node : nodes) {
            printTreeNode(node, 1);
        }
    }

    private <T extends TreeNodeDTO<T>> void printTreeNode(T node, int level) {
        String preStr = "";
        for (int i = 0; i < level; i++) {
            preStr += "|----";
        }
        System.out.println(preStr + node.getLevel() + ": " + node.getText());
        for (T children : node.getChildren()) {
            printTreeNode(children, level + 1);
        }
    }
}
