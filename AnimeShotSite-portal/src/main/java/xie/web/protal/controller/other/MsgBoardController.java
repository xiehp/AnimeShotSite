package xie.web.protal.controller.other;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.base.controller.BaseController;
import xie.base.page.PageRequestUtil;
import xie.other.ma.db.entity.CommentRecord;
import xie.other.ma.db.repository.CommonRecordDao;
import xie.other.ma.db.repository.MaDamageDao;
import xie.other.ma.db.service.CommentRecordService;
import xie.other.ma.db.service.CommonRecordService;
import xie.other.ma.db.service.MaDamageService;

@Controller
@RequestMapping(value = "/msgboard")
public class MsgBoardController extends BaseController {

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
		return "/msgboard/";
	};

	@RequestMapping(value = "")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 显示评论
		// 主页面评论
		Page<CommentRecord> shotCommentPage = entityCache.get("msgboard_page1", () -> {
			Map<String, Object> searchParamsMain = new HashMap<>();
			searchParamsMain.put("EQ_" + CommentRecord.COLUMN_CLASS1, "msgboard");
			Page<CommentRecord> page = commentRecordService.searchPageByParams(searchParamsMain, 1, 500, PageRequestUtil.SORT_TYPE_AUTO, CommentRecord.class);
			return page;
		});
		model.addAttribute("commentPage", shotCommentPage);
		return getJspFilePath("list");
	}
}
