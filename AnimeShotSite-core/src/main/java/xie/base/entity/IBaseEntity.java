package xie.base.entity;

import java.util.Date;

public interface IBaseEntity {

	public String getId();

	public void setId(String id);

	Date getCreateDate();

	void setCreateDate(Date createDate);

	Date getUpdateDate();

	void setUpdateDate(Date updateDate);

	String getCreateBy();

	void setCreateBy(String createBy);

	String getUpdateBy();

	void setUpdateBy(String updateBy);

	Long getVersion();

	void setVersion(Long version);

	Integer getDeleteFlag();

	void setDeleteFlag(Integer deleteFlag);
}
