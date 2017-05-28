package xie.web.protal.controller.other;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.base.controller.BaseController;
import xie.other.ma.db.repository.CommonRecordDao;
import xie.other.ma.db.repository.MaDamageDao;
import xie.other.ma.db.service.CommentRecordService;
import xie.other.ma.db.service.CommonRecordService;
import xie.other.ma.db.service.MaDamageService;

@Controller
@RequestMapping(value = "/site-status")
public class StatusController extends BaseController {

	@Resource
	CommonRecordDao commonRecordDao;
	@Resource
	MaDamageDao maDamageDao;
	@Resource
	CommonRecordService commonRecordService;
	@Resource
	MaDamageService maDamageService;
	@Resource
	CommentRecordService commentRecordService;
	@Resource
	EntityCache entityCache;

	protected String getJspFileRootPath() {
		return "/";
	};

	@RequestMapping(value = "/test")
	public String test(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		return getJspFilePath("other/test/test");
	}
}
