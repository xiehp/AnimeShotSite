package xie.other.ma.db.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.RequestParam;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.exception.XException;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.other.ma.db.entity.CommentRecord;
import xie.other.ma.db.repository.CommentRecordDao;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class CommentRecordService extends BaseService<CommentRecord, String> {

	@Resource
	private CommentRecordDao commentRecordDao;

	@Override
	public BaseRepository<CommentRecord, String> getBaseRepository() {
		return commentRecordDao;
	}

}
