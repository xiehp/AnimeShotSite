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

	public String getAnimeInfoId() {
		return animeInfoId;
	}

	public void setAnimeInfoId(String animeInfoId) {
		this.animeInfoId = animeInfoId;
	}

	public AnimeInfo getAnimeInfo() {
		return animeInfo;
	}

	public void setAnimeInfo(AnimeInfo animeInfo) {
		this.animeInfo = animeInfo;
	}

	@Deprecated
	public String getName() {
		return name;
	}

	@Deprecated
	public void setName(String name) {
		this.name = name;
	}

	public String getDivisionName() {
		return divisionName;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getLocalDetailPath() {
		return localDetailPath;
	}

	public void setLocalDetailPath(String localDetailPath) {
		this.localDetailPath = localDetailPath;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

	public String getShotLocalRootPath() {
		return shotLocalRootPath;
	}

	public void setShotLocalRootPath(String shotLocalRootPath) {
		this.shotLocalRootPath = shotLocalRootPath;
	}

	public String getShotLocalDetailPath() {
		return shotLocalDetailPath;
	}

	public void setShotLocalDetailPath(String shotLocalDetailPath) {
		this.shotLocalDetailPath = shotLocalDetailPath;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Transient
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

	public String getSummaryCleanHtml() {
		if (summary == null) {
			return null;
		}
		// OutputSettings outputSettings = new OutputSettings().prettyPrint(false);
		return Jsoup.clean(summary, Whitelist.none());
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

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
