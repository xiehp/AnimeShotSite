package xie.other.ma.db.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import xie.base.repository.BaseRepository;
import xie.other.ma.db.entity.MaDamage;
import xie.other.ma.db.repository.CommonRecordDao;
import xie.other.ma.db.repository.MaDamageDao;

@Service
public class MaDamageService extends CommonRecordDetailService<MaDamage, String> {

	@Resource
	private MaDamageDao maDamageDao;
	@Resource
	private CommonRecordDao commonRecordDao;

	@Override
	public BaseRepository<MaDamage, String> getBaseRepository() {
		return maDamageDao;
	}

	@Override
	public MaDamage getDetail(String id) {
		MaDamage maDamage = maDamageDao.findById(id);
		if (maDamage != null && maDamage.getCommonRecordId() != null) {
			maDamage.setCommonRecord(commonRecordDao.findById(maDamage.getCommonRecordId()));
		}

		return maDamage;
	}

}
