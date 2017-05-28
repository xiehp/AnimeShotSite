package xie.web.login.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import xie.base.controller.BaseController;
import xie.common.Constants;
import xie.common.exception.NoPermissionException;
import xie.common.utils.props.PropsKeys;
import xie.common.web.util.ConstantsWeb;
import xie.sys.auth.entity.User;

@Controller
@RequestMapping(value = ConstantsWeb.MANAGE_URL_PREFIX_STR)
public class LoginController extends BaseController {

	@Resource
	private MessageSource messageSource;

	@RequestMapping(value = "/login456")
	public String login() {
		return "login";
	}

	// @Deprecated
	// @RequestMapping(value="/login")
	// public String loginFail(@RequestParam(required=false) String username, Model model, HttpServletRequest request){
	//
	// //判断用户是否已经登录
	// if(SecurityUtils.getSubject().isAuthenticated()){
	// return "redirect:/index";
	// }
	//
	// model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
	//
	// //判断是否有错误
	// String shiroLoginFailureExStr = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	// if(!StringUtils.isEmpty(shiroLoginFailureExStr)){
	// if(IncorrectCredentialsException.class.getName().equals(shiroLoginFailureExStr) || AuthenticationException.class.getName().equals(shiroLoginFailureExStr)){
	// model.addAttribute(Constants.ERROR, messageSource.getMessage("login.incorrectcredentials.exception", null, null));
	// }else if(UnknownAccountException.class.getName().equals(shiroLoginFailureExStr)){
	// model.addAttribute(Constants.ERROR, messageSource.getMessage("login.unknownaccount.exception", null, null));
	// }else if(NoPermissionException.class.getName().equals(shiroLoginFailureExStr)){
	// model.addAttribute(Constants.ERROR, messageSource.getMessage("login.noPermission.exception", null, null));
	// }else{
	// model.addAttribute(Constants.ERROR, messageSource.getMessage("login.other.exception", null, null));
	// }
	//
	// }
	//
	// return "login";
	// }
	//
	// @RequestMapping(value="/webLogin", method = RequestMethod.POST)
	// public String webLogin(@RequestParam String loginId, @RequestParam String pasword) {
	//
	// Map<String, Object> resultMap = getFailCode();
	//
	// Subject subject = SecurityUtils.getSubject();
	// if(subject.isAuthenticated()){
	// //已经登陆过
	// resultMap = getSuccessCode();
	// }else{
	// UsernamePasswordToken token = new UsernamePasswordToken(loginId, pasword);
	// try {
	// subject.login(token);
	// //登录成功
	// resultMap = getSuccessCode();
	// } catch (UnknownAccountException e) {
	// resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_UNKNOWNACCOUNT_EXCEPTION, null, null));
	// }catch (DisabledAccountException e) {
	// resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_DISABLEDACCOUNT_EXCEPTION, null, null));
	// }catch (NoPermissionException e) {
	// resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_NOPERMISSION_EXCEPTION, null, null));
	// }catch (IncorrectCredentialsException ae) {
	// resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_INCORRECTCREDENTIALS_EXCEPTION, null, null));
	// }catch (AuthenticationException ae) {
	// resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_AUTHENTICATION_EXCEPTION, null, null));
	// }catch(Exception e){
	// resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_OTHER_EXCEPTION, null, null));
	// }
	// }
	//
	// return "/index";
	// }

	@RequestMapping(value = "/webLoginAjax", method = RequestMethod.POST)
	public ResponseEntity<?> webLoginAjax(@RequestBody User user) {

		Map<String, Object> resultMap = getFailCode();

		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			// 已经登陆过
			resultMap = getSuccessCode();
		} else {
			UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginName(), user.getPassword().toCharArray());
			try {
				subject.login(token);
				// 登录成功
				resultMap = getSuccessCode();
			} catch (UnknownAccountException e) {
				resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_UNKNOWNACCOUNT_EXCEPTION, null, null));
			} catch (DisabledAccountException e) {
				resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_DISABLEDACCOUNT_EXCEPTION, null, null));
			} catch (NoPermissionException e) {
				resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_NOPERMISSION_EXCEPTION, null, null));
			} catch (IncorrectCredentialsException ae) {
				resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_INCORRECTCREDENTIALS_EXCEPTION, null, null));
			} catch (AuthenticationException ae) {
				resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_AUTHENTICATION_EXCEPTION, null, null));
			} catch (Exception e) {
				resultMap.put(Constants.MESSAGE, messageSource.getMessage(PropsKeys.LOGIN_OTHER_EXCEPTION, null, null));
			}
		}

		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject != null && subject.isAuthenticated()) {
			subject.logout();
		}
		return "login";
	}

}
