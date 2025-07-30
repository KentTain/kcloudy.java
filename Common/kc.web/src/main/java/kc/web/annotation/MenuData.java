package kc.web.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import kc.dto.account.MenuNodeDTO;
import kc.framework.GlobalConfig;
import kc.framework.extension.StringExtensions;

public class MenuData {
	static {
		IsUpgradeDatabase = false;
		AllMenus = new ArrayList<MenuNodeDTO>();
	}

	public final static void AddResource(MenuAnnotation item, MenuNodeDTO parentItem) {
		String parentName = item.ParentMenuName();
		String menuName = item.MenuName();
		if (StringExtensions.isNullOrEmpty(parentName) || StringExtensions.isNullOrEmpty(menuName)) {
			return;
		}

		String url = item.Url();
		// Url: /ControllerName/ActionName?QueryString=xxxx
		// Url: /AreaName/ControllerName/ActionName?QueryString=xxxx
		url = url.startsWith("/") ? url.replaceFirst("/", "") : url;
		url = url.endsWith("/") ? url.replaceFirst("/", "") : url;

		String[] queryParms = new String[] { url };
		if (url.contains("?")) {
			queryParms = url.split("?");
		}

		String[] urlParms = queryParms[0].split("/");

		String query = "";
		if (queryParms.length == 2)
			query = queryParms[1];

		UUID appId = GlobalConfig.ApplicationGuid;
		String area = "";
		String controller = "";
		String action = "";
		if (urlParms.length == 2) {
			area = "";
			controller = urlParms[0];
			action = urlParms[1];
		} else if (urlParms.length == 3) {
			area = urlParms[0];
			controller = urlParms[1];
			action = urlParms[2];
		}

		if (StringExtensions.isNullOrEmpty(area) && StringExtensions.isNullOrEmpty(controller)
				&& StringExtensions.isNullOrEmpty(action))
			return;

		final String areaName = area;
		final String controllerName = controller;
		final String actionName = action;
		Predicate<MenuNodeDTO> predicate = m -> m.getApplicationId() == appId;
		if (!StringExtensions.isNullOrEmpty(areaName))
			predicate = predicate.and(m -> m.getControllerName().equalsIgnoreCase(areaName));
		if (!StringExtensions.isNullOrEmpty(controllerName))
			predicate = predicate.and(m -> m.getControllerName().equalsIgnoreCase(controllerName));
		if (!StringExtensions.isNullOrEmpty(actionName))
			predicate = predicate.and(m -> m.getActionName().equalsIgnoreCase(actionName));

		List<MenuNodeDTO> filterMenus = AllMenus.stream().filter(predicate).collect(Collectors.toList());
		if (filterMenus.size() <= 0) {
			MenuNodeDTO node = new MenuNodeDTO();
			node.setText(menuName);
			node.setApplicationId(appId);
			node.setApplicationName(GlobalConfig.CurrentApplication != null
        			? GlobalConfig.CurrentApplication.getAppName()
        			: "");
			node.setAreaName(area);
			node.setControllerName(controller);
			node.setActionName(action);
			node.setParameters(query);
			node.setVersion(item.Version());
			node.setTenantType(item.TenantType());
			node.setSmallIcon(item.SmallIcon());
			node.setLeaf(item.Level() == 3);
			node.setLevel(item.Level());
			node.setParentNode(parentItem);
			node.setDefaultRoleId(item.DefaultRoleId());
			node.setAuthorityId(item.AuthorityId());
			node.setExtPage(item.IsExtPage());
			node.setIndex(item.Order());

			switch (item.Level()) {
			case 1:
				node.setDescription(String.format("一级菜单【%s】", menuName));
				break;
			case 2:
				node.setDescription(String.format("一级菜单【%s】下的二级菜单【%s】", parentName, menuName));
				break;
			case 3:
				node.setDescription(String.format("二级菜单【%s】下的三级菜单【%s】", parentName, menuName));
				break;
			}

			if (parentItem == null) {
				AllMenus.add(node);
			} else {
				parentItem.getChildren().add(node);
			}
		}
	}

	public static boolean IsUpgradeDatabase;

	public static List<MenuNodeDTO> AllMenus;
}
