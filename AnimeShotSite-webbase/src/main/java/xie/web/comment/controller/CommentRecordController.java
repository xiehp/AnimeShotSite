package xie.web.comment.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.utils.SiteUtils;
import xie.base.controller.BaseFunctionController;
import xie.other.ma.db.entity.CommentRecord;
import xie.other.ma.db.service.CommentRecordService;

@Controller
@RequestMapping(value = "/comment")
public class CommentRecordController extends BaseFunctionController<ShotInfo, String> {

	@Autowired
	private EntityCache entityCache;
	@Autowired
	private CommentRecordService CommentRecordService;

	protected String getJspFileRootPath() {
		return "/comment/";
	};

}
