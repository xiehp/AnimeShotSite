package xie.other.ma.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.other.ma.db.entity.CommonRecord;
import xie.other.ma.db.repository.CommonRecordDao;

@Service
public class CommonRecordService extends BaseService<CommonRecord, String> {

	@Autowired
	private CommonRecordDao commonRecordDao;

	@Override
	public BaseRepository<CommonRecord, String> getBaseRepository() {
		return commonRecordDao;
	}

	public CommonRecord getRecord(String id) {
		return commonRecordDao.findById(id);
	}
	
	
}
