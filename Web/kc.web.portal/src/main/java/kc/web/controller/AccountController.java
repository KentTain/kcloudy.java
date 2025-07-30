package kc.web.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kc.service.base.ServiceResult;
import kc.service.enums.ServiceResultType;
import kc.service.webapiservice.IAccountApiService;
import kc.web.base.WebBaseController;
import kc.web.models.ChangeEmailPhoneViewModel;
import kc.web.models.ChangePasswordViewModel;
import kc.web.multitenancy.IdSrvOidcUser;

@Controller
@RequestMapping("/account")
public class AccountController extends WebBaseController {
	//private Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private IAccountApiService accountService;

	@GetMapping("/ChangePassword")
	public @ResponseBody ServiceResult<Boolean> ChangePassword(ChangePasswordViewModel model) {
		if (!IsAuthenticated())
			return new ServiceResult<Boolean>(ServiceResultType.PurviewLack, "用户未登陆，不能修改邮箱及手机号.");

//        if (!ModelState.IsValid)
//            return new ServiceResult<Boolean>(ServiceResultType.ParamError, "数据有误,请重新输入." );

		ServiceResult<Boolean> result = accountService.ChangePassword(CurrentUserId(), model.getOldPassword(),
				model.getNewPassword());

		return result.isSuccess() ? new ServiceResult<Boolean>(ServiceResultType.Success, "密码修改成功.")
				: new ServiceResult<Boolean>(ServiceResultType.Error,
				result.getMessage().contains("密码不正确") ? "当前密码不正确." : result.getMessage());
	}

	@GetMapping("/ChangeMailPhone")
	public @ResponseBody ServiceResult<Boolean> ChangeMailPhone(ChangeEmailPhoneViewModel model) {
		if (!IsAuthenticated())
			return new ServiceResult<Boolean>(ServiceResultType.PurviewLack, "用户未登陆，不能修改邮箱及手机号.");

//        if (!ModelState.IsValid)
//            return new ServiceResult<Boolean>(ServiceResultType.ParamError, "数据有误,请重新输入." );

		ServiceResult<Boolean> result = accountService.ChangeMailPhone(CurrentUserId(), model.Email, model.Phone);

		return result.isSuccess() ? new ServiceResult<Boolean>(ServiceResultType.Success, "邮箱及手机号修改成功.")
				: new ServiceResult<Boolean>(ServiceResultType.Error, "邮箱及手机号修改失败，请重试.");
	}

	@GetMapping("/SignOut")
	public String SignOut(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/logout";
	}

	@GetMapping("/userinfo")
	public @ResponseBody String userinfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
		IdSrvOidcUser userDetails = (IdSrvOidcUser) authentication.getPrincipal();
		if (userDetails != null) {
			//Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
			String username = userDetails.getName();
			String accessToken = userDetails.getIdToken().getTokenValue();

			StringBuilder sb = new StringBuilder();
			for (GrantedAuthority authority : auths) {
				sb.append(authority.getAuthority() + ", ");
			}
			return "Welcome, " + username + ", accessToken: " + accessToken + ", authorities: " + sb.toString();
		}

		return "Welcome, " + authentication.getName();
	}
}