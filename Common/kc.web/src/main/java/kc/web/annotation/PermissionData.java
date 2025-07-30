package kc.web.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import kc.dto.account.PermissionDTO;
import kc.framework.GlobalConfig;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ApplicationConstant;

public class PermissionData {
	static
    {
        IsUpgradeDatabase = false;
        AllPermissions = new ArrayList<PermissionDTO>();
    }

    public final static void AddResource(PermissionAnnotation item, PermissionDTO parentItem)
    {
        String menuName = item.MenuName();
        String permissionName = item.PermissionName();
        if (StringExtensions.isNullOrEmpty(menuName) 
            || StringExtensions.isNullOrEmpty(permissionName))
        {
            return;
        }

        String url = item.Url();
        //Url: /ControllerName/ActionName?QueryString=xxxx
        //Url: /AreaName/ControllerName/ActionName?QueryString=xxxx
        url = url.startsWith("/") ? url.replaceFirst("/", "") : url;
        url = url.endsWith("/") ? url.replaceFirst("/", "") : url;

        String[] queryParms = new String[] {url};
        if(url.contains("?"))
        {
        	queryParms = url.split("?");
        }

        String[] urlParms = queryParms[0].split( "/");

        String query = "";
        if (queryParms.length == 2)
            query = queryParms[1];

        UUID appId = GlobalConfig.ApplicationGuid;
        String area = "";
        String controller = "";
        String action = "";
        if(urlParms.length == 2)
        {
        	area = "";
            controller = urlParms[0];
            action = urlParms[1];
        }
        else if (urlParms.length == 3)
        {
        	area = urlParms[0];
            controller = urlParms[1];
            action = urlParms[2];
        }

        if (StringExtensions.isNullOrEmpty(area) 
            && StringExtensions.isNullOrEmpty(controller) 
            && StringExtensions.isNullOrEmpty(action))
            return;
        
        final String areaName = area;
        final String controllerName = controller;
        final String actionName = action;
        Predicate<PermissionDTO> predicate = m -> m.getApplicationId() == appId;
        if (!StringExtensions.isNullOrEmpty(areaName))
        	predicate = predicate.and(m -> m.getControllerName().equalsIgnoreCase(areaName));
        if (!StringExtensions.isNullOrEmpty(controllerName))
            predicate = predicate.and(m -> m.getControllerName().equalsIgnoreCase(controllerName));
        if (!StringExtensions.isNullOrEmpty(actionName))
            predicate = predicate.and(m -> m.getActionName().equalsIgnoreCase(actionName));

        List<PermissionDTO> filterPermissions = AllPermissions.stream().filter(predicate).collect(Collectors.toList());
        if (filterPermissions.size() <= 0)
        {
        	PermissionDTO node = new PermissionDTO();
        	node.setText(menuName + ApplicationConstant.DefaultAuthoritySplitChar + permissionName);
        	node.setApplicationId(appId);
        	node.setApplicationName(GlobalConfig.CurrentApplication != null 
        			? GlobalConfig.CurrentApplication.getAppName()
        			: "");
        	node.setAreaName(area);
        	node.setControllerName(controller);
        	node.setActionName(action);
        	node.setParameters(query);
			node.setDescription(item.IsPage() ? String.format("页面【%s】的权限", menuName)
					: String.format("页面【%s】下的权限【%s】", menuName, permissionName));
			node.setIndex(item.Order());
			node.setLeaf(parentItem == null ? false : true);
			node.setLevel(item.IsPage() ? 1 : 2);
			node.setParentNode(parentItem);
			node.setDefaultRoleId(item.DefaultRoleId());
			node.setAuthorityId(item.AuthorityId());
            node.setIndex(item.Order());
            
            if (parentItem == null)
            {
                AllPermissions.add(node);
            }
            else
            {
                parentItem.getChildren().add(node);
            }
        }
    }
    
    public static boolean IsUpgradeDatabase ;

    public static List<PermissionDTO> AllPermissions;
}
