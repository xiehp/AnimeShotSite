package xie.web.protal.controller.other.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.base.controller.BaseController;
import xie.base.module.ajax.vo.GoPageResult;
import xie.base.module.exception.CodeApplicationException;
import xie.other.ma.db.repository.CommonRecordDao;
import xie.other.ma.db.repository.MaDamageDao;
import xie.other.ma.db.service.CommentRecordService;
import xie.other.ma.db.service.CommonRecordService;
import xie.other.ma.db.service.MaDamageService;

@Controller
@RequestMapping(value = "/testController")
public class TestController extends BaseController {

	@Autowired
	CommonRecordDao commonRecordDao;
	@Autowired
	MaDamageDao maDamageDao;
	@Autowired
	CommonRecordService commonRecordService;
	@Autowired
	MaDamageService maDamageService;
	@Autowired
	CommentRecordService commentRecordService;
	@Autowired
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

	@RequestMapping(value = "/test1")
	public String test1(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		throw new Exception();
	}

	@RequestMapping(value = "/test2")
	public String test2(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		throw new Exception();
	}

	@RequestMapping(value = "/goPageResult")
	public GoPageResult goPageResult(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		GoPageResult goPageResult = new GoPageResult();
		return goPageResult;
	}

	@RequestMapping(value = "/goPageResult1")
	@ResponseBody
	public GoPageResult goPageResult1(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		GoPageResult goPageResult = new GoPageResult();
		return goPageResult;
	}

	@RequestMapping(value = "/goPageResult2")
	@ResponseBody
	public GoPageResult goPageResult2(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		GoPageResult goPageResult = new GoPageResult();
		goPageResult.setGoPage("anime");
		return goPageResult;
	}

	@RequestMapping(value = "/goPageResult3")
	@ResponseBody
	public GoPageResult goPageResult3(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		Thread.sleep(1000);
		throw new RuntimeException("RuntimeException goPageResult3");
	}

	@RequestMapping(value = "/goPageResult4")
	@ResponseBody
	public GoPageResult goPageResult4(
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		throw new CodeApplicationException("login.incorrectcredentials.exception");
	}
}
