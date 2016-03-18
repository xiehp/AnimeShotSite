package xie.web.menu.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import xie.base.controller.BaseController;
import xie.common.Constants;
import xie.sys.auth.entity.tmp.Menu;
import xie.sys.auth.service.ResourceService;

@Controller
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {
	

	@Autowired 
	private ResourceService resourceService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public ResponseEntity<?> initMenu(HttpSession session){
		List<Menu> menus = (List<Menu>)session.getAttribute(Constants.MENU_KEY);
		if(menus == null){
			menus = resourceService.findMenus(getCurrentUserId());
			session.setAttribute(Constants.MENU_KEY, menus);
		}
		return new ResponseEntity<List<Menu>>(menus, HttpStatus.OK);
	}
	
}
