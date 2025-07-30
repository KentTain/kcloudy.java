package kc.web.base;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import kc.dto.TreeNodeDTO;
import kc.dto.TreeNodeSimpleDTO;
import kc.service.ServiceWrapper;
import kc.service.base.ServiceResult;
import kc.web.multitenancy.IdSrvOidcUser;

/**
 * Controller的基类，包含当前登录用户信息及菜单、权限数据
 * 
 * @author 田长军
 *
 */
public abstract class WebApiBaseController {
	// 自定义类型转换器
	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"), true));
		binder.registerCustomEditor(java.math.BigDecimal.class, new WebApiBaseController.CustomerBigDecimalEditor());
	}

	private static class CustomerBigDecimalEditor extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if (text == null || text.equals("")) {
				text = "0";
			}
			setValue(new java.math.BigDecimal(text));
		}

		@Override
		public String getAsText() {
			Object decObj = getValue();
			if (decObj == null)
				return "0";
			return getValue().toString();
		}
	}

	protected Authentication authentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected IdSrvOidcUser userDetails() {
		if (authentication().getPrincipal() instanceof IdSrvOidcUser) {
			return (IdSrvOidcUser) authentication().getPrincipal();
		}

		return null;
	}

	protected boolean hasAuthority(String authorityId) {
		Collection<? extends GrantedAuthority> auths = authentication().getAuthorities();
		return auths.parallelStream().anyMatch(m -> m.getAuthority().equalsIgnoreCase(authorityId));
	}

	/**
	 * 返回boolean类型的快捷方式
	 * 
	 * @param success 是否成功
	 * @param message 失败后的消息
	 * @return
	 */
	protected String ThrowErrorJsonMessage(boolean success, String message) {
		return "{\"success\":\"" + success + "\",\"message\":\"\"" + message + "\"\"}";
	}

	/**
	 * 获取ServiceResult<T>对象
	 * 
	 * @param func   Lamdba表达式: Supplier<T>
	 * @param logger 记录错误日志
	 * @return ServiceResult<T>
	 */
	protected <T> ServiceResult<T> GetServiceResult(Supplier<T> func, Logger logger) {
		return ServiceWrapper.Invoke("ControllerBase", func.getClass().getName(), func, logger);
	}

	/**
	 * 根据条件，将树结构列表中，归属于父节点的所有的递归子节点拼凑至父节点，形成一颗以父节点为顶层的树
	 * 
	 * @param          <T> TreeNodeSimpleDTO<T>
	 * @param parent   树形结构的父节点
	 * @param nodeList 树结构列表
	 * @param predict  拼凑条件
	 * 
	 */
	protected <T extends TreeNodeDTO<T>> void GetTreeWithChild(T parent, List<T> nodeList,
			Predicate<T> predict) {
		if (predict == null) {
			List<T> child = nodeList.stream().filter(m -> m.getParentNode() != null && m.getParentNode().getId() == parent.getId())
					.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetTreeWithChild(children, nodeList, predict);
			}
		} else {
			List<T> child = nodeList.stream().filter(m -> m.getParentNode() != null && m.getParentNode().getId() == parent.getId()).filter(predict)
					.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetTreeWithChild(children, nodeList, predict);
			}
		}
	}
	
	/**
	 * 根据条件，将树结构列表中，归属于父节点的所有的递归子节点拼凑至父节点，形成一颗以父节点为顶层的树
	 * 
	 * @param          <T> TreeNodeSimpleDTO<T>
	 * @param parent   树形结构的父节点
	 * @param nodeList 树结构列表
	 * @param predict  拼凑条件
	 * 
	 */
	protected <T extends TreeNodeSimpleDTO<T>> void GetSimpleTreeWithChild(T parent, List<T> nodeList,
			Predicate<T> predict) {
		if (predict == null) {
			List<T> child = nodeList.stream().filter(m -> m.getParentId() != null && m.getParentId().equals(parent.getId()))
					.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetSimpleTreeWithChild(children, nodeList, predict);
			}
		} else {
			List<T> child = nodeList.stream().filter(m -> m.getParentId() != null && m.getParentId().equals(parent.getId())).filter(predict)
					.sorted(Comparator.comparing(T::getIndex).reversed())
					.collect(Collectors.toList());
			parent.getChildren().addAll(child);
			for (T children : child) {
				GetSimpleTreeWithChild(children, nodeList, predict);
			}
		}
	}
}
