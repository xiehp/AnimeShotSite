package xie.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.utils.SiteUtils;
import xie.base.controller.BaseController;
import xie.base.module.ajax.vo.GoPageResult;
import xie.common.exception.XException;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.common.web.util.ConstantsWeb;
import xie.other.ma.db.entity.CommentRecord;
import xie.other.ma.db.service.CommentRecordService;

@Controller
@RequestMapping(value = "/comment")
public class CommentController extends BaseController {

	@Resource
	EntityCache entityCache;
	@Resource
	CommentRecordService commentRecordService;

	/**
	 *
	 * @param request
	 * @param content
	 * @param userName
	 * @param class2 all, main
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "createComment")
	@ResponseBody
	public GoPageResult create(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) String replyCommentId,
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) String class1,
			@RequestParam(required = false) String class2,
			@RequestParam(required = false) String class3,
			@RequestParam(required = false) String class4,
			@RequestParam(required = false) String targetId,
			@RequestParam(required = false) String targetAttr1,
			@RequestParam(required = false) String targetAttr2,
			@RequestParam(required = false) String targetAttr3) throws XException, UnsupportedEncodingException {

		if (XStringUtils.isBlank(content)) {
			throw new XException(messageSourceUtils.getMessage("请输入评论内容"));
		}

		if (content.length() > 2000) {
			throw new XException(messageSourceUtils.getMessage("对不起，评论最多只能输入2000个字"));
		}

		if (userName != null && userName.length() > 50) {
			throw new XException(messageSourceUtils.getMessage("对不起，昵称最多只能输入50个字"));
		}

		if (userName != null) {
			XCookieUtils.addCookieValue(response, ConstantsWeb.SITE_COOKIE_USER_NAME, userName);
		}

		CommentRecord commentRecord = new CommentRecord();
		commentRecord.setReplyCommentId(replyCommentId);
		commentRecord.setCommentDate(new Date());
		commentRecord.setCookieId(SiteUtils.getSiteCookieId(request));
		commentRecord.setUserName(userName);
		commentRecord.setClass1(class1);
		commentRecord.setTargetId(targetId);
		commentRecord.setContent(content);
		commentRecord = commentRecordService.save(commentRecord);

		// 清除缓存
		entityCache.remove("commentPageMain");
		entityCache.remove("commentPageALl");
		entityCache.remove("commentPageSingle_" + targetId);
		entityCache.remove("shot_comment_" + targetId);

		GoPageResult goPageResult;
		if (commentRecord != null) {
			goPageResult = createSuccess(request, messageSourceUtils.getMessage("评论成功"));
		} else {
			goPageResult = createFail(request, messageSourceUtils.getMessage("评论失败"));
		}

		return goPageResult;
	}
}
