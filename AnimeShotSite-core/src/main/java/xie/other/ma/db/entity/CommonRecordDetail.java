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

	/**
	 * 获取 通用记录ID.
	 *
	 * @return 通用记录ID
	 */
	public String getCommonRecordId() {
		return commonRecordId;
	}

	/**
	 * 设置 通用记录ID.
	 *
	 * @param commonRecordId 通用记录ID
	 */
	public void setCommonRecordId(String commonRecordId) {
		this.commonRecordId = commonRecordId;
	}

	/**
	 * 获得common record.
	 *
	 * @return common record
	 */
	public CommonRecord getCommonRecord() {
		return CommonRecord;
	}

	/**
	 * 设置common record.
	 *
	 * @param commonRecord common record
	 */
	public void setCommonRecord(CommonRecord commonRecord) {
		CommonRecord = commonRecord;
	}

}
