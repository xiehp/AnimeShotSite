package xie.web.comment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.base.controller.BaseFunctionController;
import xie.other.ma.db.service.CommentRecordService;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/comment")
public class CommentRecordController extends BaseFunctionController<ShotInfo, String> {

	@Resource
	private EntityCache entityCache;
	@Resource
	private CommentRecordService CommentRecordService;

	protected String getJspFileRootPath() {
		return "/comment/";
	};

}
