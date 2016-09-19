package xie.other.ma.db.repository;

import xie.base.repository.BaseRepository;
import xie.other.ma.db.entity.MaDamage;

public interface MaDamageDao extends BaseRepository<MaDamage, String> {

	/** 用通用记录ID获取一条数据 */
	MaDamage findByCommonRecordId(String commonRecordId);
}
