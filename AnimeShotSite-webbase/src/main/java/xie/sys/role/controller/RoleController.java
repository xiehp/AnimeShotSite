package xie.sys.role.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.assertj.core.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.Servlets;

import xie.base.controller.BaseController;
import xie.common.Constants;
import xie.sys.auth.entity.Role;
import xie.sys.auth.service.ResourceService;
import xie.sys.auth.service.RoleService;
import xie.sys.role.vo.RoleVo;

@Controller
@RequestMapping(value="/role")
public class RoleController extends BaseController {
	
	private Logger _log = LoggerFactory.getLogger(RoleController.class);

	@Resource
	private RoleService roleService;

	@Resource
	private ResourceService resourceService;
	
	@RequiresPermissions(value = "roleList:view")
	@RequestMapping(value= "/list")
	public String roleList(@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request){
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		//增加删除过滤
		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");
		
		Page<Role> list = roleService.searchAllRoles(searchParams, pageNumber, Constants.PAGE_SIZE_DEFAULT, sortType);
		
		List<RoleVo> voList = BeanMapper.mapList(list.getContent(), RoleVo.class);
		
		Page<RoleVo> pageVoList = getPageByList(list, voList, RoleVo.class);
		
		model.addAttribute("roleList", pageVoList);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "role/list";
	}
	
	@RequiresPermissions(value = "roleList:add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> addRole(@RequestBody RoleVo roleVo){
		Map<String, Object> result = Maps.newHashMap();
		try {
			roleService.addRole(roleVo);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, e.getMessage());
			_log.error("新增角色失败: {}", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequiresPermissions(value = "roleList:delete")
	@RequestMapping(value = "/delete/{roleId}")
	public ResponseEntity<?> delete(@PathVariable String roleId){
		Map<String, Object> result = Maps.newHashMap();
		try {
			roleService.deleteRole(roleId);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, "添加用户错误,请联系管理员");
			_log.error("删除角色失败: {}", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequiresPermissions(value = "roleList:view")
	@RequestMapping(value = "/permissionList/{roleId}")
	public String permissionList(@PathVariable String roleId, Model model){
		Map<String, Object> rolePermissionList = resourceService.loadRolePermissions(roleId);
		model.addAttribute("rolePermissionList", rolePermissionList);
		return "role/permissionList";
	}
	
	@RequiresPermissions(value = "roleList:authorize")
	@RequestMapping(value = "/setRoleResourcePermission", method = RequestMethod.POST)
	public ResponseEntity<?> setRoleResourcePermission(@RequestBody RoleVo roleVo){
		Map<String, Object> result = Maps.newHashMap();
		try {
			roleService.updateRoleResourcePermission(roleVo);
			result.put(Constants.HTTP_KEY, "true");
		} catch (Exception e) {
			result.put(Constants.HTTP_KEY, "false");
			result.put(Constants.HTTP_MESSAGE, "角色权限失败,请联系管理员");
			_log.error("修改权限失败: {}", e.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/initRoles", method = RequestMethod.GET)
	public ResponseEntity<?> initRoles(){
		List<Role> roleList = roleService.findALlRoles();
		List<RoleVo> voList = BeanMapper.mapList(roleList, RoleVo.class);
		return new ResponseEntity<List<RoleVo>>(voList, HttpStatus.OK);
	}
}
