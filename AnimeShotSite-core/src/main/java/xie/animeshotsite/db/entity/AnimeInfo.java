package xie.animeshotsite.db.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = "anime_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnimeInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 截图状态 0：未截图 */
	public static final Integer SHOT_STATUS_NO = 0;
	/** 截图状态 1：部分截图 */
	public static final Integer SHOT_STATUS_PART = 1;
	/** 截图状态 2：已截图 */
	public static final Integer SHOT_STATUS_FULL = 2;

	/** 处理动作 0：不做任何事情 */
	public static final Integer PROCESS_ACTION_NO = 0;
	/** 处理动作 1：完整截图 */
	public static final Integer PROCESS_ACTION_FULL = 1;
	/** 处理动作 2：只截缺少部分 */
	public static final Integer PROCESS_ACTION_PART = 2;

	/** 名称 */
	private String name;

	/** 副标题 */
	private String secondName;

	/** 第几季名称 */
	private String divisionName;

	/** 动画全称=NAME+空格+DIVISION_NAME */
	private String fullName;

	private Integer type;

	/** 系列 */
	private String series;

	/** 截图状态 0：未截图 1：部分截图 2：已截图 */
	private Integer shotStatus;

	/** 处理动作 0：不做任何事情 1：完整截图 2：只截缺少部分 */
	private Integer processAction;

	/** 本地所在根路径 如F:\\site\\resource 当数据量过大，新的文件会更换盘符，而老的文件不变 */
	private String localRootPath;

	/** 本地所在相对路径 ${localRootPath}\\动画，截图或图片区分路径\\${localDetailPath}} */
	private String localDetailPath;

	// /** 本地所在文件名 */
	// private String localFileName;

	/** 图片ID */
	private String titleUrlId;
	public final static String COLUMN_TITLE_URL_ID = "titleUrlId";

	/** 图片信息 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = COLUMN_TITLE_URL_ID, insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private ImageUrl titleUrl;

	/** 简介 */
	private String summary;

	/** 排序 */
	private Integer sort;

	/** 是否显示 */
	private Integer showFlg;

	private Integer status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Integer getShotStatus() {
		return shotStatus;
	}

	public void setShotStatus(Integer shotStatus) {
		this.shotStatus = shotStatus;
	}

	public Integer getProcessAction() {
		return processAction;
	}

	public void setProcessAction(Integer processAction) {
		this.processAction = processAction;
	}

	public String getLocalRootPath() {
		return localRootPath;
	}

	public void setLocalRootPath(String localRootPath) {
		this.localRootPath = localRootPath;
	}

	public String getLocalDetalPath() {
		return localDetailPath;
	}

	public void setLocalDetalPath(String localDetalPath) {
		this.localDetailPath = localDetalPath;
	}

	public String getLocalDetailPath() {
		return localDetailPath;
	}

	public void setLocalDetailPath(String localDetailPath) {
		this.localDetailPath = localDetailPath;
	}

	public String getTitleUrlId() {
		return titleUrlId;
	}

	public void setTitleUrlId(String titleUrlId) {
		this.titleUrlId = titleUrlId;
	}

	public ImageUrl getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(ImageUrl titleUrl) {
		this.titleUrl = titleUrl;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getShowFlg() {
		return showFlg;
	}

	public void setShowFlg(Integer showFlg) {
		this.showFlg = showFlg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
