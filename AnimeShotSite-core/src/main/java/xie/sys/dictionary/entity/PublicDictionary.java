package xie.sys.dictionary.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = "SYS_PUBLIC_DICTIONARY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PublicDictionary extends BaseEntity {

	private static final long serialVersionUID = -2026574135274474467L;

	private String code;
	private String typeId;
	private String value;
	private Integer sort;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
