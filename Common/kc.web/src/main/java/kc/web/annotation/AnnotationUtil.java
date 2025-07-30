package kc.web.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import kc.dto.TreeNodeDTO;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import kc.dto.account.MenuNodeDTO;
import kc.dto.account.PermissionDTO;
import kc.framework.tenant.ApplicationConstant;

public class AnnotationUtil {

	/**
	 * 获取某个包下面的PermissionAnnotation及MenuAnnotation并保存至PermissionData、MenuData中
	 * 
	 * @param packageName 包名称
	 */
	public static void initAnnotationDataByPackageName(String packageName) {
		Reflections reflections = new Reflections(packageName, new MethodAnnotationsScanner());

		Set<Method> menuMethods = reflections.getMethodsAnnotatedWith(MenuAnnotation.class);
		List<MenuAnnotation> menuAttrData = new ArrayList<>();
		for (Method method : menuMethods) {
			MenuAnnotation menuAttr = method.getAnnotation(MenuAnnotation.class);
			if (null != menuAttr) {
				menuAttrData.add(menuAttr);
			}
		}
		// 一级菜单
		for (MenuAnnotation item : menuAttrData.stream().filter(m -> m.Level() == 1).collect(Collectors.toList())) {
			MenuData.AddResource(item, null);
		}

		// 二级菜单
		for (MenuAnnotation item : menuAttrData.stream().filter(m -> m.Level() == 2).collect(Collectors.toList())) {
			Optional<MenuNodeDTO> parentItem = MenuData.AllMenus.stream()
					.filter(m -> m.getText().equalsIgnoreCase(item.ParentMenuName())).findFirst();
			parentItem.ifPresent(menuNodeDTO -> MenuData.AddResource(item, menuNodeDTO));
		}

		// 三级菜单
		for (MenuAnnotation item : menuAttrData.stream().filter(m -> m.Level() == 3).collect(Collectors.toList())) {
			List<MenuNodeDTO> childMenus = new ArrayList<>();
			MenuData.AllMenus.stream().map(TreeNodeDTO::getChildren).forEach(childMenus::addAll);

			Optional<MenuNodeDTO> parentItem = childMenus.stream()
					.filter(m -> m.getText().equalsIgnoreCase(item.ParentMenuName())).findFirst();
			parentItem.ifPresent(menuNodeDTO -> MenuData.AddResource(item, menuNodeDTO));
		}

		Set<Method> permissionMethods = reflections.getMethodsAnnotatedWith(PermissionAnnotation.class);
		List<PermissionAnnotation> permissionAttrData = new ArrayList<>();
		for (Method method : permissionMethods) {
			PermissionAnnotation permissionAttr = method.getAnnotation(PermissionAnnotation.class);
			if (null != permissionAttr) {
				permissionAttrData.add(permissionAttr);
			}
		}

		// 父节点
		for (PermissionAnnotation item : permissionAttrData.stream().filter(PermissionAnnotation::IsPage)
				.collect(Collectors.toList())) {
			PermissionData.AddResource(item, null);
		}

		// 子节点
		for (PermissionAnnotation item : permissionAttrData.stream().filter(m -> !m.IsPage())
				.collect(Collectors.toList())) {
			
			Optional<PermissionDTO> parentItem = PermissionData.AllPermissions.stream()
					.filter(m -> m.getLevel() == 1 
						&& m.getText().endsWith(ApplicationConstant.DefaultAuthoritySplitChar + item.MenuName()))
					.findFirst();
			
//			Optional<PermissionDTO> parentItem = PermissionData.AllPermissions.stream()
//					.filter(m -> m.getText().equals(item.MenuName() + ApplicationConstant.DefaultAuthoritySplitChar + item.MenuName())).findFirst();
			parentItem.ifPresent(permissionDTO -> PermissionData.AddResource(item, permissionDTO));
		}

	}
}
