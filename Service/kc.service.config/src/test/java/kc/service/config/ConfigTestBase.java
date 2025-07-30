package kc.service.config;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.dto.TreeNodeDTO;
import kc.framework.GlobalConfigData;
import kc.framework.TestBase;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.List;


//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = AppTest.class)
abstract class ConfigTestBase extends TestBase{
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
	 * @param nodes
	 */
	public <T extends TreeNodeDTO<T>> void printTreeNodeDTOs(List<T> nodes) {
		for (T node : nodes) {
			printTreeNode(node, 1);
		}
	}

	private <T extends TreeNodeDTO<T>> void printTreeNode(T node, int level) {
		StringBuilder preStr = new StringBuilder();
		for (int i = 0; i < level; i++) {
			preStr.append("|----");
		}
		System.out.println(preStr.toString() + node.getLevel() + ": " + node.getText());
		for (T children : node.getChildren()) {
			printTreeNode(children, level + 1);
		}
	}
}
