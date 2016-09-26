package xie.animeshotsite.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = "anime_episode")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnimeEpisode extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String COLUMN_SHOW_FLG = "SHOW_FLG";
	public static final String COLUMN_ANIME_INFO_ID = "animeInfoId";

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

	private String animeInfoId;

	@Transient
	private AnimeInfo animeInfo;

	/** 剧集名称 */
	@Deprecated
	private String name;

	/** 第几集名称 */
	private String divisionName;

	/** 剧集标题 */
	private String title;

	/** 剧集全称=动画全称+空格+DIVISION_NAME */
	private String fullName;

	/** 剧集类型 0:正片 1：花絮 2：SP 3：PV 4：片头 5：片尾 9：其他 */
	private Integer type;

	/** 截图状态 0：未截图 1：部分截图 2：已截图 */
	private Integer shotStatus;

	/** 处理动作 0：不做任何事情 1：完整截图 2：只截缺少部分 */
	private Integer processAction;

	/** 本地所在根路径,没有值时使用AnimeInfo的值 */
	private String localRootPath;

	/** 本地所在相对路径,没有值时使用AnimeInfo的值 */
	private String localDetailPath;

	/** 本地所在文件名 */
	private String localFileName;

	/** 截图的本地根路径 null时和上面相同 */
	private String shotLocalRootPath;
	/** 截图的本地相对路径 null时和上面相同 */
	private String shotLocalDetailPath;
	/** 编号，唯一标识符 在本地相对路径基础上追加的一层path，用于区分每个剧集，唯一性, 同时生成字幕信息时，也用该字段作是否曾经创建过的判断依据 */
	private String number;

	/** 分辨率 宽度 */
	private Integer width;

	/** 分辨率 高度 */
	private Integer height;

	/** 图片ID,没有值时使用AnimeInfo的值 */
	private String titleUrlId;
	public final static String COLUMN_TITLE_URL_ID = "titleUrlId";

	/** 图片信息 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = COLUMN_TITLE_URL_ID, insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private ImageUrl titleUrl;

	/** 简介 */
	private String summary;

	private Integer sort;

	/** 是否显示 */
	private Integer showFlg;

	/** 显示时间，用于排序 */
	private Date showDate;

	private Integer status;

	/**
	 * 获得anime info id.
	 *
	 * @return anime info id
	 */
	public String getAnimeInfoId() {
		return animeInfoId;
	}

	/**
	 * 设置anime info id.
	 *
	 * @param animeInfoId anime info id
	 */
	public void setAnimeInfoId(String animeInfoId) {
		this.animeInfoId = animeInfoId;
	}

	/**
	 * 获得anime info.
	 *
	 * @return anime info
	 */
	public AnimeInfo getAnimeInfo() {
		return animeInfo;
	}

	/**
	 * 设置anime info.
	 *
	 * @param animeInfo anime info
	 */
	public void setAnimeInfo(AnimeInfo animeInfo) {
		this.animeInfo = animeInfo;
	}

	/**
	 * 获取 剧集名称.
	 *
	 * @return 剧集名称
	 */
	@Deprecated
	public String getName() {
		return name;
	}

	/**
	 * 设置 剧集名称.
	 *
	 * @param name 剧集名称
	 */
	@Deprecated
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 第几集名称.
	 *
	 * @return 第几集名称
	 */
	public String getDivisionName() {
		return divisionName;
	}

	/**
	 * 设置 第几集名称.
	 *
	 * @param divisionName 第几集名称
	 */
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	/**
	 * 获取 剧集标题.
	 *
	 * @return 剧集标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置 剧集标题.
	 *
	 * @param title 剧集标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取 剧集全称=动画全称+空格+DIVISION_NAME.
	 *
	 * @return 剧集全称=动画全称+空格+DIVISION_NAME
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * 设置 剧集全称=动画全称+空格+DIVISION_NAME.
	 *
	 * @param fullName 剧集全称=动画全称+空格+DIVISION_NAME
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * 获取 剧集类型 0:正片 1：花絮 2：SP 3：PV 4：片头 5：片尾 9：其他.
	 *
	 * @return 剧集类型 0:正片 1：花絮 2：SP 3：PV 4：片头 5：片尾 9：其他
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置 剧集类型 0:正片 1：花絮 2：SP 3：PV 4：片头 5：片尾 9：其他.
	 *
	 * @param type 剧集类型 0:正片 1：花絮 2：SP 3：PV 4：片头 5：片尾 9：其他
	 */
	public void setType(Integer type) {
		this.type = type;
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
	 * 获取 本地所在根路径,没有值时使用AnimeInfo的值.
	 *
	 * @return 本地所在根路径,没有值时使用AnimeInfo的值
	 */
	public String getLocalRootPath() {
		return localRootPath;
	}

	/**
	 * 设置 本地所在根路径,没有值时使用AnimeInfo的值.
	 *
	 * @param localRootPath 本地所在根路径,没有值时使用AnimeInfo的值
	 */
	public void setLocalRootPath(String localRootPath) {
		this.localRootPath = localRootPath;
	}

	/**
	 * 获取 本地所在相对路径,没有值时使用AnimeInfo的值.
	 *
	 * @return 本地所在相对路径,没有值时使用AnimeInfo的值
	 */
	public String getLocalDetailPath() {
		return localDetailPath;
	}

	/**
	 * 设置 本地所在相对路径,没有值时使用AnimeInfo的值.
	 *
	 * @param localDetailPath 本地所在相对路径,没有值时使用AnimeInfo的值
	 */
	public void setLocalDetailPath(String localDetailPath) {
		this.localDetailPath = localDetailPath;
	}

	/**
	 * 获取 本地所在文件名.
	 *
	 * @return 本地所在文件名
	 */
	public String getLocalFileName() {
		return localFileName;
	}

	/**
	 * 设置 本地所在文件名.
	 *
	 * @param localFileName 本地所在文件名
	 */
	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

	/**
	 * 获取 截图的本地根路径 null时和上面相同.
	 *
	 * @return 截图的本地根路径 null时和上面相同
	 */
	public String getShotLocalRootPath() {
		return shotLocalRootPath;
	}

	/**
	 * 设置 截图的本地根路径 null时和上面相同.
	 *
	 * @param shotLocalRootPath 截图的本地根路径 null时和上面相同
	 */
	public void setShotLocalRootPath(String shotLocalRootPath) {
		this.shotLocalRootPath = shotLocalRootPath;
	}

	/**
	 * 获取 截图的本地相对路径 null时和上面相同.
	 *
	 * @return 截图的本地相对路径 null时和上面相同
	 */
	public String getShotLocalDetailPath() {
		return shotLocalDetailPath;
	}

	/**
	 * 设置 截图的本地相对路径 null时和上面相同.
	 *
	 * @param shotLocalDetailPath 截图的本地相对路径 null时和上面相同
	 */
	public void setShotLocalDetailPath(String shotLocalDetailPath) {
		this.shotLocalDetailPath = shotLocalDetailPath;
	}

	/**
	 * 获取 编号，唯一标识符 在本地相对路径基础上追加的一层path，用于区分每个剧集，唯一性, 同时生成字幕信息时，也用该字段作是否曾经创建过的判断依据.
	 *
	 * @return 编号，唯一标识符 在本地相对路径基础上追加的一层path，用于区分每个剧集，唯一性, 同时生成字幕信息时，也用该字段作是否曾经创建过的判断依据
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * 设置 编号，唯一标识符 在本地相对路径基础上追加的一层path，用于区分每个剧集，唯一性, 同时生成字幕信息时，也用该字段作是否曾经创建过的判断依据.
	 *
	 * @param number 编号，唯一标识符 在本地相对路径基础上追加的一层path，用于区分每个剧集，唯一性, 同时生成字幕信息时，也用该字段作是否曾经创建过的判断依据
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * 获取 分辨率 宽度.
	 *
	 * @return 分辨率 宽度
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * 设置 分辨率 宽度.
	 *
	 * @param width 分辨率 宽度
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * 获取 分辨率 高度.
	 *
	 * @return 分辨率 高度
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * 设置 分辨率 高度.
	 *
	 * @param height 分辨率 高度
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * 获取 图片ID,没有值时使用AnimeInfo的值.
	 *
	 * @return 图片ID,没有值时使用AnimeInfo的值
	 */
	@Transient
	public String getTitleUrlId() {
		return titleUrlId;
	}

	/**
	 * 设置 图片ID,没有值时使用AnimeInfo的值.
	 *
	 * @param titleUrlId 图片ID,没有值时使用AnimeInfo的值
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

	/**
	 * 获得sort.
	 *
	 * @return sort
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * 设置sort.
	 *
	 * @param sort sort
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
	 * 获取 显示时间，用于排序.
	 *
	 * @return 显示时间，用于排序
	 */
	public Date getShowDate() {
		return showDate;
	}

	/**
	 * 设置 显示时间，用于排序.
	 *
	 * @param showDate 显示时间，用于排序
	 */
	public void setShowDate(Date showDate) {
		this.showDate = showDate;
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
