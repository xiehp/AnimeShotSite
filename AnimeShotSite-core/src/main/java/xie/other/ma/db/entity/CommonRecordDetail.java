package xie.other.ma.db.entity;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import xie.base.entity.BaseEntity;

/**
 * 通用记录详情，本身并没有表，需要子类继承
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class CommonRecordDetail extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 通用记录ID */
	private String commonRecordId;
	/** 通用记录 */
	@Transient
	private CommonRecord CommonRecord;

	public String getCommonRecordId() {
		return commonRecordId;
	}

	public void setCommonRecordId(String commonRecordId) {
		this.commonRecordId = commonRecordId;
	}

	public CommonRecord getCommonRecord() {
		return CommonRecord;
	}

	public void setCommonRecord(CommonRecord commonRecord) {
		CommonRecord = commonRecord;
	}

}
