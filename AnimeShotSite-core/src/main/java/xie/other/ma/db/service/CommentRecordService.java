package xie.other.ma.db.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.other.ma.db.entity.CommentRecord;
import xie.other.ma.db.repository.CommentRecordDao;

@Service
public class CommentRecordService extends BaseService<CommentRecord, String> {

	@Resource
	private CommentRecordDao commentRecordDao;

	@Override
	public BaseRepository<CommentRecord, String> getBaseRepository() {
		return commentRecordDao;
	}

}
