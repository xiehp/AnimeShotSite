package xie.web.login.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.base.controller.BaseController;
import xie.base.module.ajax.vo.GoPageResult;
import xie.common.Constants;
import xie.common.exception.NoPermissionException;
import xie.common.utils.props.PropsKeys;
import xie.module.spring.utils.XMessageSourceUtils;
import xie.sys.auth.entity.User;

@Controller
@RequestMapping
public class LoginPortalController extends BaseController {

	@Resource
	private XMessageSourceUtils messageSourceUtils;

	@RequestMapping(value = "/login")
	public String login() {
		return "login/login";
	}

	@RequestMapping(value = "/submitLoginAjax", method = RequestMethod.POST)
	@ResponseBody
	public GoPageResult webLoginAjax(@RequestBody User user, HttpServletRequest request) {

		Map<String, Object> resultMap = getFailCode();

		GoPageResult goPageResult = createFail(request, null);

		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			// 已经登陆过
			resultMap = getSuccessCode();
			goPageResult.setSuccess(true);
		} else {
			UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassword().toCharArray());
			try {
				subject.login(token);
				// 登录成功
				resultMap = getSuccessCode();
				goPageResult.setSuccess(true);
			} catch (UnknownAccountException e) {
				resultMap.put(Constants.MESSAGE, messageSourceUtils.getMessage(PropsKeys.LOGIN_UNKNOWNACCOUNT_EXCEPTION));
				goPageResult.addAlertMessage(messageSourceUtils.getMessage(PropsKeys.LOGIN_UNKNOWNACCOUNT_EXCEPTION));
			} catch (DisabledAccountException e) {
				resultMap.put(Constants.MESSAGE, messageSourceUtils.getMessage(PropsKeys.LOGIN_DISABLEDACCOUNT_EXCEPTION));
				goPageResult.addAlertMessage(messageSourceUtils.getMessage(PropsKeys.LOGIN_DISABLEDACCOUNT_EXCEPTION));
			} catch (NoPermissionException e) {
				resultMap.put(Constants.MESSAGE, messageSourceUtils.getMessage(PropsKeys.LOGIN_NOPERMISSION_EXCEPTION));
				goPageResult.addAlertMessage(messageSourceUtils.getMessage(PropsKeys.LOGIN_NOPERMISSION_EXCEPTION));
			} catch (IncorrectCredentialsException ae) {
				resultMap.put(Constants.MESSAGE, messageSourceUtils.getMessage(PropsKeys.LOGIN_INCORRECTCREDENTIALS_EXCEPTION));
				goPageResult.addAlertMessage(messageSourceUtils.getMessage(PropsKeys.LOGIN_INCORRECTCREDENTIALS_EXCEPTION));
			} catch (AuthenticationException ae) {
				resultMap.put(Constants.MESSAGE, messageSourceUtils.getMessage(PropsKeys.LOGIN_AUTHENTICATION_EXCEPTION));
				goPageResult.addAlertMessage(messageSourceUtils.getMessage(PropsKeys.LOGIN_AUTHENTICATION_EXCEPTION));
			} catch (Exception e) {
				resultMap.put(Constants.MESSAGE, messageSourceUtils.getMessage(PropsKeys.LOGIN_OTHER_EXCEPTION));
				goPageResult.addAlertMessage(messageSourceUtils.getMessage(PropsKeys.LOGIN_OTHER_EXCEPTION));
			}
		}

		return goPageResult;
	}

	@RequestMapping(value = "/logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null && subject.isAuthenticated()) {
			subject.logout();
		}
		return "login/login";
	}

}
