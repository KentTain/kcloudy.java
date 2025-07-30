package kc.web.annotation;

import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import kc.dto.TreeNodeDTO;
import kc.framework.GlobalConfig;
import kc.framework.base.ApplicationInfo;
import kc.framework.tenant.ApplicationConstant;

public class AnnotationUtilTest {
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		GlobalConfig.CurrentApplication = new ApplicationInfo(ApplicationConstant.AdminAppSId, ApplicationConstant.AdminCode,
				ApplicationConstant.AdminAppName, "http://ctest.localhost:1003", ApplicationConstant.AdminScope, 1);
	}

	@AfterAll
	public static void setDownAfterClass() throws Exception {
		GlobalConfig.CurrentApplication = null;
	}

	@Test
	public void testAnnotationUtil() {
		AnnotationUtil.initAnnotationDataByPackageName("kc.web.annotation");

		System.out.println("-----AnnotationUtil get MenuData: ");
		printTreeNodes(MenuData.AllMenus);
		System.out.println("-----AnnotationUtil get PermissionData: ");
		printTreeNodes(PermissionData.AllPermissions);
	}

	private <T extends TreeNodeDTO<T>> void printTreeNodes(List<T> nodes) {
		for (T node : nodes) {
			printTreeNode(node, 1);
		}
	}
	private <T  extends TreeNodeDTO<T>> void printTreeNode(T node, int level) {
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
