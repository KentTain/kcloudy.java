package kc.web.controller;

import kc.dto.TreeNodeSimpleDTO;
import kc.dto.account.MenuNodeSimpleDTO;
import kc.dto.message.MemberRemindMessageDTO;
import kc.enums.MessageStatus;
import kc.framework.GlobalConfig;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;
import kc.service.base.ServiceResult;
import kc.web.annotation.MenuAnnotation;
import kc.web.base.WebBaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;

@Controller
@lombok.extern.slf4j.Slf4j
@RequestMapping(value="/Main")
public class MainController extends WebBaseController {
	@PreAuthorize("hasAuthority('" + ApplicationConstant.DefaultAuthorityId + "')")
//	@PermissionAnnotation(MenuName = "门户管理首页", PermissionName = "首页页面", Url = "Home/Index",
//			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsPage = true,
//            ResultType = ResultType.ActionResult, AuthorityId = ApplicationConstant.DefaultAuthorityId)
	@RequestMapping("/Index")
	public String index(Locale locale, ModelMap model) {
        model.addAttribute("title", GlobalConfig.CurrentApplication.getAppName());
        model.addAttribute("tenantDisplayName", CurrentUserTenantName());
        model.addAttribute("userName", CurrentUserName());
        model.addAttribute("userDisplayName", CurrentUserDisplayName());
        model.addAttribute("userEmail", CurrentUserEmail());
        model.addAttribute("userPhone", CurrentUserPhone());
		return "main/index";
	}

    /*------------------------------------------菜单设置-----------------------------------------------*/
    /**
     * 一级级菜单：门户管理
     */
    @MenuAnnotation(ParentMenuName = "门户管理", MenuName = "门户管理", Url = "/Main/Portal",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-globe", AuthorityId = "8E54E761-6861-4FED-9C85-36D22F57DC60",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 1)
    @RequestMapping("/Portal")
    public String Portal() {
        return super.ThrowErrorJsonMessage(true, "菜单-门户管理");
    }

    /**
     * 二级级菜单：门户管理/门户信息管理
     */
    @MenuAnnotation(ParentMenuName = "门户管理", MenuName = "门户信息管理", Url = "/Main/PortalInfoManage",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-list", AuthorityId = "D113B4A9-AA6B-4B83-A8DD-F30C80CCBE31",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
    @RequestMapping("/PortalInfoManage")
    public String PortalInfoManage() {
        return super.ThrowErrorJsonMessage(true, "门户管理-门户信息管理");
    }

    /**
     * 二级级菜单：门户管理/商品管理
     */
    @MenuAnnotation(ParentMenuName = "门户管理", MenuName = "商品管理", Url = "/Main/OfferingManage",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-list", AuthorityId = "D01C43FE-6359-4469-9328-28DB53E333D3",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 2)
    @RequestMapping("/OfferingManage")
    public String OfferingManage() {
        return super.ThrowErrorJsonMessage(true, "门户管理-商品管理");
    }

    /**
     * 二级级菜单：门户管理/新闻管理
     */
    @MenuAnnotation(ParentMenuName = "门户管理", MenuName = "新闻管理", Url = "/Main/NewsManage",
            Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
            SmallIcon = "fa fa-list", AuthorityId = "57BEFCDB-3AE7-4A19-9195-871A023B557D",
            DefaultRoleId = RoleConstants.AdminRoleId, Order = 3, IsExtPage = false, Level = 2)
    @RequestMapping("/NewsManage")
    public String NewsManage() {
        return super.ThrowErrorJsonMessage(true, "门户管理-新闻管理");
    }


    /*------------------------------------------主页数据-----------------------------------------------*/
    @GetMapping("/GetMenuIdByUrl")
    public @ResponseBody
    ServiceResult<Integer> GetMenuIdByUrl(String url) {
        return GetServiceResult(() -> {
            List<MenuNodeSimpleDTO> allMenus = GetCachedCurrentUserMenus();
            UUID appId = GlobalConfig.ApplicationGuid;
            Optional<MenuNodeSimpleDTO> menu = allMenus.stream()
                    .filter(m -> m.getApplicationId().equals(appId) && m.getUrl().contains(url)).findFirst();
            return menu.map(TreeNodeSimpleDTO::getId).orElse(0);
        }, log);
    }

    @GetMapping("/LoadMenus")
    public @ResponseBody
    List<MenuNodeSimpleDTO> LoadMenus() {
        if (!IsAuthenticated()) //IllegalAccessException
            return new ArrayList<MenuNodeSimpleDTO>();

        Predicate<MenuNodeSimpleDTO> predicate = m -> true;
        return GetCurrentUserMenuTree(predicate);
    }

    @GetMapping("/LoadMessages")
    public @ResponseBody
    List<MemberRemindMessageDTO> LoadMessages() {
        if (!IsAuthenticated()) //IllegalAccessException
            return new ArrayList<MemberRemindMessageDTO>();

        List<MemberRemindMessageDTO> messages = new ArrayList<MemberRemindMessageDTO>();
        messages.add(MemberRemindMessageDTO.builder()
                .id(1)
                .status(MessageStatus.Unread)
                .messageTitle("Validators")
                .messageContent ( "In order to provide the best possible experience to old and buggy browsers, Font Awesome uses CSS browser hacks in several places to target special CSS to certain browser versions in order to work around bugs in the browsers themselves. These hacks understandably cause CSS validators to complain that they are invalid. In a couple places, we also use bleeding-edge CSS features that aren't yet fully standardized, but these are used purely for progressive enhancement.<br/>" +
                        "These validation warnings don't matter in practice since the non-hacky portion of our CSS does fully validate and the hacky portions don't interfere with the proper functioning of the non - hacky portion,hence why we deliberately ignore these particular warnings.")
                .build()
        );
        messages.add(MemberRemindMessageDTO.builder()
                .id(2)
                .status(MessageStatus.Unread)
                .messageTitle("Internet Explorer 8 and @font-face")
                .messageContent ( "IE8 has some issues with @font-face when combined with :before. Font Awesome uses that combination. If a page is cached, and loaded without the mouse over the window (i.e. hit the refresh button or load something in an iframe) then the page gets rendered before the font loads. Hovering over the page (body) will show some of the icons and hovering over the remaining icons will show those as well. See issue #954 for details.")
                .build()
        );
        messages.add(MemberRemindMessageDTO.builder()
                .id(3)
                .status(MessageStatus.Unread)
                .messageTitle("Need IE7 Support")
                .messageContent ( "IE8 has some issues with @font-face when combined with :before. Font Awesome uses that combination. If a page is cached, and loaded without the mouse over the window (i.e. hit the refresh button or load something in an iframe) then the page gets rendered before the font loads. Hovering over the page (body) will show some of the icons and hovering over the remaining icons will show those as well. See issue #954 for details.")
                .build()
        );
        return messages;
    }
}
