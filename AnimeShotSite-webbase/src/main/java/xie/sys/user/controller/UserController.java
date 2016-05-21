package xie.sys.user.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.assertj.core.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;

import xie.base.controller.BaseController;
import xie.base.controller.BaseFunctionController;
import xie.common.Constants;
import xie.common.utils.DictionaryLoader;
import xie.sys.auth.entity.User;
import xie.sys.auth.entity.UserSetting;
import xie.sys.auth.service.UserService;
import xie.sys.auth.service.UserSettingService;
import xie.sys.dictionary.common.DictionaryConstants;
import xie.sys.dictionary.utils.PublicDictionaryLoader;
import xie.sys.user.vo.UserVo;

@Controller
@RequestMapping(value="/user")
public class UserController extends BaseFunctionController<User, String> {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserSettingService userSettingService;
	
	private Logger _log = LoggerFactory.getLogger(UserController.class);
	
	/**
	 * 列表, 权限, 分页 综合演示方法
	 * @param sortType
	 * @param pageNumber
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions(value = "userList:view")
	@RequestMapping(value= "/list")
	public String userList(@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request) throws Exception {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		//增加删除过滤
		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");
		searchParams.put("EQ_userType", Constants.USER_TYPE_BACKGROUND + "");
		
		Page<UserVo> list = userService.searchAllUsers(searchParams, pageNumber, Constants.PAGE_SIZE_DEFAULT, sortType);
	
		model.addAttribute("userList", list);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		
		return "user/list";
	}
	
	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/add", method=RequestMethod.POST)
	public ResponseEntity<?> userAdd(@RequestBody UserVo vo){
		Map<String, Object> result = Maps.newHashMap();
		try {
			userService.addUser(vo);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, "添加用户错误,请联系管理员");
			_log.error("添加用户失败: {}", e.getCause());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/validateUserName", method=RequestMethod.POST)
	public ResponseEntity<?> validateUserName(@RequestBody UserVo vo){
		User user = userService.findUserByLoginNameAndId(vo.getLoginName(), vo.getId());
		Map<String, Object> result = Maps.newHashMap();
		result.put(Constants.HTTP_KEY, user != null);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequiresPermissions(value = "userList:view")
	@RequestMapping(value= "/view/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> view(@PathVariable String id){
		Map<String, Object> resultMap = userService.viewUser(id);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@RequiresPermissions(value = "userList:update")
	@RequestMapping(value = "/update", method=RequestMethod.POST)	
	public ResponseEntity<?> update(@RequestBody UserVo vo){
		Map<String, Object> result = Maps.newHashMap();
		try {
			userService.updateUser(vo);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, e.getMessage());
			_log.error("更新用户失败: {}", e.getCause());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequiresPermissions(value = "userList:update")
	@RequestMapping(value = "/lock/{userId}", method=RequestMethod.GET)
	public ResponseEntity<?> lock(@PathVariable String userId){
		Map<String, Object> result = Maps.newHashMap();
		try {
			userService.changeUserStatus(userId, Constants.USER_STATUS_LOCK);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, e.getMessage());
			_log.error("锁定用户失败: {}", e.getCause());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequiresPermissions(value = "userList:update")
	@RequestMapping(value = "/unlock/{userId}", method=RequestMethod.GET)
	public ResponseEntity<?> unlock(@PathVariable String userId){
		Map<String, Object> result = Maps.newHashMap();
		try {
			userService.changeUserStatus(userId, Constants.USER_STATUS_NORMAL);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, e.getMessage());
			_log.error("解锁用户失败: {}", e.getCause());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequiresPermissions(value = "userList:delete")
	@RequestMapping(value = "/delete/{userId}", method=RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable String userId){
		Map<String, Object> result = Maps.newHashMap();
		try {
			userService.delete(userId);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, e.getMessage());
			_log.error("删除用户失败: {}", e.getCause());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/setting", method=RequestMethod.GET)
	public String setting(Model model){
		User user = userService.findOne(getCurrentUserId());
		UserSetting setting = userSettingService.findByUserId(getCurrentUserId());
		Map<String, String> skinMap = PublicDictionaryLoader.getMap(DictionaryConstants.MAP_SKIN_PATH);
		model.addAttribute("user", user);
		model.addAttribute("userSetting", setting);
		model.addAttribute("skinMap", skinMap);
		return "setting/setting";
	}
	
	@RequestMapping(value="/updateSetting")
	public String updateSetting(UserVo vo, RedirectAttributes attr){
		try {
			userService.updateUserAndSetting(vo, getCurrentUserId());
			attr.addAttribute("success", "true");
		} catch (Exception e) {
			attr.addAttribute("error", "更新用户信息错误");
			_log.error("更新用户信息失败: {}", e.getCause());
		}
		return "redirect:/user/setting";
	}
	
	
	public List<String> getAutoCompleteList(String param){
		return userService.getLoginNameList(param);
	}
}
