package xie.animeshotsite.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = AutoRunParam.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class AutoRunParam extends BaseEntity {

	private static final long serialVersionUID = -5667693713699407485L;

	public static final String TABLE_NAME = "auto_run_param";
	public static final String ENTITY_NAME = AutoRunParam.class.getSimpleName();

	public static final String COLUMN_KEY = "key";
	public static final String COLUMN_VALUE = "value";
	public static final String COLUMN_SORT = "sort";

	/** 类型:模板 */
	public static final String TYPE_TEMPLET = "templet";

	/** 动画模板 */
	public static final String TEMPLET_ANIME = "anime_templet";
	/** 剧集模板 */
	public static final String TEMPLET_EPISODE = "episode_templet";
	
	

	/** 类型 templet:模板 其他:通常数据 */
	private String type;

	/** 参数表示名 */
	private String name;

	/** 参数备注 */
	private String remark;

	/** key */
	@Column(name = "PARAM_KEY")
	private String key;

	/** 值 */
	private String value;

	/** 动画ID */
	private String animeInfoId;

	/** 剧集ID */
	private String animeEpisodeId;

	/** 所属对象的ID */
	private String refId;

	/** 所属对象类型，用于表示REF_ID是哪种对象 */
	private String refType;

	/** 排序 */
	private String sort;

	/** 信息 */
	private String message;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAnimeInfoId() {
		return animeInfoId;
	}

	public void setAnimeInfoId(String animeInfoId) {
		this.animeInfoId = animeInfoId;
	}

	public String getAnimeEpisodeId() {
		return animeEpisodeId;
	}

	public void setAnimeEpisodeId(String animeEpisodeId) {
		this.animeEpisodeId = animeEpisodeId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
