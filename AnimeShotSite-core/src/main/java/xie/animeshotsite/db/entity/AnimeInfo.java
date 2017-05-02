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
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = "anime_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnimeInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String COLUMN_FULL_NAME = "fullName";
	public static final String COLUMN_SHOW_FLG = "showFlg";

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

	/** 获取简介的url */
	private String summaryCollectUrl;

	/** 获取简介标题的正则 */
	private String summaryCollectTitleExp;

	/** 排序 */
	private Integer sort;

	/** 是否显示 */
	private Integer showFlg;

	private Integer status;

	/**
	 * 获取 名称.
	 *
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 名称.
	 *
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 副标题.
	 *
	 * @return 副标题
	 */
	public String getSecondName() {
		return secondName;
	}

	/**
	 * 设置 副标题.
	 *
	 * @param secondName 副标题
	 */
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	/**
	 * 获取 第几季名称.
	 *
	 * @return 第几季名称
	 */
	public String getDivisionName() {
		return divisionName;
	}

	/**
	 * 设置 第几季名称.
	 *
	 * @param divisionName 第几季名称
	 */
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	/**
	 * 获取 动画全称=NAME+空格+DIVISION_NAME.
	 *
	 * @return 动画全称=NAME+空格+DIVISION_NAME
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * 设置 动画全称=NAME+空格+DIVISION_NAME.
	 *
	 * @param fullName 动画全称=NAME+空格+DIVISION_NAME
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * 获得type.
	 *
	 * @return type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置type.
	 *
	 * @param type type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取 系列.
	 *
	 * @return 系列
	 */
	public String getSeries() {
		return series;
	}

	/**
	 * 设置 系列.
	 *
	 * @param series 系列
	 */
	public void setSeries(String series) {
		this.series = series;
	}

	/**
	 * 获取 截图状态 0：未截图 1：部分截图 2：已截图.
	 *
	 * @return 截图状态 0：未截图 1：部分截图 2：已截图
	 */
	public Integer getShotStatus() {
		return shotStatus;
	}

	/**
	 * 设置 截图状态 0：未截图 1：部分截图 2：已截图.
	 *
	 * @param shotStatus 截图状态 0：未截图 1：部分截图 2：已截图
	 */
	public void setShotStatus(Integer shotStatus) {
		this.shotStatus = shotStatus;
	}

	/**
	 * 获取 处理动作 0：不做任何事情 1：完整截图 2：只截缺少部分.
	 *
	 * @return 处理动作 0：不做任何事情 1：完整截图 2：只截缺少部分
	 */
	public Integer getProcessAction() {
		return processAction;
	}

	/**
	 * 设置 处理动作 0：不做任何事情 1：完整截图 2：只截缺少部分.
	 *
	 * @param processAction 处理动作 0：不做任何事情 1：完整截图 2：只截缺少部分
	 */
	public void setProcessAction(Integer processAction) {
		this.processAction = processAction;
	}

	/**
	 * 获取 本地所在根路径 如F:\\site\\resource 当数据量过大，新的文件会更换盘符，而老的文件不变.
	 *
	 * @return 本地所在根路径 如F:\\site\\resource 当数据量过大，新的文件会更换盘符，而老的文件不变
	 */
	public String getLocalRootPath() {
		return localRootPath;
	}

	/**
	 * 设置 本地所在根路径 如F:\\site\\resource 当数据量过大，新的文件会更换盘符，而老的文件不变.
	 *
	 * @param localRootPath 本地所在根路径 如F:\\site\\resource 当数据量过大，新的文件会更换盘符，而老的文件不变
	 */
	public void setLocalRootPath(String localRootPath) {
		this.localRootPath = localRootPath;
	}

	/**
	 * 获得local detal path.
	 *
	 * @return local detal path
	 */
	public String getLocalDetalPath() {
		return localDetailPath;
	}

	/**
	 * 设置local detal path.
	 *
	 * @param localDetalPath local detal path
	 */
	public void setLocalDetalPath(String localDetalPath) {
		this.localDetailPath = localDetalPath;
	}

	/**
	 * 获取 本地所在相对路径 ${localRootPath}\\动画，截图或图片区分路径\\${localDetailPath}}.
	 *
	 * @return 本地所在相对路径 ${localRootPath}\\动画，截图或图片区分路径\\${localDetailPath}}
	 */
	public String getLocalDetailPath() {
		return localDetailPath;
	}

	/**
	 * 设置 本地所在相对路径 ${localRootPath}\\动画，截图或图片区分路径\\${localDetailPath}}.
	 *
	 * @param localDetailPath 本地所在相对路径 ${localRootPath}\\动画，截图或图片区分路径\\${localDetailPath}}
	 */
	public void setLocalDetailPath(String localDetailPath) {
		this.localDetailPath = localDetailPath;
	}

	/**
	 * 获取 图片ID.
	 *
	 * @return 图片ID
	 */
	public String getTitleUrlId() {
		return titleUrlId;
	}

	/**
	 * 设置 图片ID.
	 *
	 * @param titleUrlId 图片ID
	 */
	public void setTitleUrlId(String titleUrlId) {
		this.titleUrlId = titleUrlId;
	}

	/**
	 * 获取 图片信息.
	 *
	 * @return 图片信息
	 */
	public ImageUrl getTitleUrl() {
		return titleUrl;
	}

	/**
	 * 设置 图片信息.
	 *
	 * @param titleUrl 图片信息
	 */
	public void setTitleUrl(ImageUrl titleUrl) {
		this.titleUrl = titleUrl;
	}

	/**
	 * 获取 简介.
	 *
	 * @return 简介
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * 获得summary clean html.
	 *
	 * @return summary clean html
	 */
	public String getSummaryCleanHtml() {
		if (summary == null) {
			return null;
		}
		// OutputSettings outputSettings = new OutputSettings().prettyPrint(false);
		return Jsoup.clean(summary, Whitelist.none());
	}

	/**
	 * 设置 简介.
	 *
	 * @param summary 简介
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}



	public String getSummaryCollectUrl() {
		return summaryCollectUrl;
	}

	public void setSummaryCollectUrl(String summaryCollectUrl) {
		this.summaryCollectUrl = summaryCollectUrl;
	}

	public String getSummaryCollectTitleExp() {
		return summaryCollectTitleExp;
	}

	public void setSummaryCollectTitleExp(String summaryCollectTitleExp) {
		this.summaryCollectTitleExp = summaryCollectTitleExp;
	}

	/**
	 * 获取 排序.
	 *
	 * @return 排序
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * 设置 排序.
	 *
	 * @param sort 排序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * 获取 是否显示.
	 *
	 * @return 是否显示
	 */
	public Integer getShowFlg() {
		return showFlg;
	}

	/**
	 * 设置 是否显示.
	 *
	 * @param showFlg 是否显示
	 */
	public void setShowFlg(Integer showFlg) {
		this.showFlg = showFlg;
	}

	/**
	 * 获得status.
	 *
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置status.
	 *
	 * @param status status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

}
