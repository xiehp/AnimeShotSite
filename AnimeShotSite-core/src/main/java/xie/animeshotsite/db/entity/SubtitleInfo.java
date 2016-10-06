package xie.animeshotsite.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = SubtitleInfo.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubtitleInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String COLUMN_animeInfoId = "animeInfoId";
	public static final String COLUMN_animeEpisodeId = "animeEpisodeId";

	public static final String TABLE_NAME = "subtitle_info";

	/** 简体 */
	public static final String LANGUAGE_CHS = "sc";
	/** 繁体 */
	public static final String LANGUAGE_CHT = "4";
	/** 日语 */
	public static final String LANGUAGE_JAPAN = "jp";
	/** 英语 */
	public static final String LANGUAGE_ENGLIST = "8";

	/** 动画信息的id */
	private String animeInfoId;

	@Transient
	private AnimeInfo animeInfo;

	/** 动画剧集ID */
	private String animeEpisodeId;

	@Transient
	private AnimeEpisode animeEpisode;

	/** 语言 */
	private String language;

	/** 字幕文件类型 ASS STR ... */
	private String fileType;

	/** 1:外部文件字幕（需要指明本地文件地址） 2:内嵌字幕 */
	private String fileLocation;

	/** 是否作为截图的字幕显示在网页上 */
	private Integer showFlg;

	/** 本地所在根路径,没有值时使用AnimeInfo的值 */
	private String localRootPath;

	/** 本地所在相对路径,没有值时使用AnimeInfo的值 */
	private String localDetailPath;

	/** 本地所在文件名 */
	private String localFileName;

	/** 当前字幕文件需要偏移的时间，单位毫秒 */
	private Long offsetTime;

	/** 包含某种属性文字 */
	private String filterInclude;

	/** 排除某种属性文字 */
	private String filterRemove;

	/**
	 * 获取 动画信息的id.
	 *
	 * @return 动画信息的id
	 */
	public String getAnimeInfoId() {
		return animeInfoId;
	}

	/**
	 * 设置 动画信息的id.
	 *
	 * @param animeInfoId 动画信息的id
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
	 * 获取 动画剧集ID.
	 *
	 * @return 动画剧集ID
	 */
	public String getAnimeEpisodeId() {
		return animeEpisodeId;
	}

	/**
	 * 设置 动画剧集ID.
	 *
	 * @param animeEpisodeId 动画剧集ID
	 */
	public void setAnimeEpisodeId(String animeEpisodeId) {
		this.animeEpisodeId = animeEpisodeId;
	}

	/**
	 * 获得anime episode.
	 *
	 * @return anime episode
	 */
	public AnimeEpisode getAnimeEpisode() {
		return animeEpisode;
	}

	/**
	 * 设置anime episode.
	 *
	 * @param animeEpisode anime episode
	 */
	public void setAnimeEpisode(AnimeEpisode animeEpisode) {
		this.animeEpisode = animeEpisode;
	}

	/**
	 * 获取 语言.
	 *
	 * @return 语言
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 设置 语言.
	 *
	 * @param language 语言
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 获取 字幕文件类型 ASS STR .
	 *
	 * @return 字幕文件类型 ASS STR
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * 设置 字幕文件类型 ASS STR .
	 *
	 * @param fileType 字幕文件类型 ASS STR
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 获取 1:外部文件字幕（需要指明本地文件地址） 2:内嵌字幕.
	 *
	 * @return 1:外部文件字幕（需要指明本地文件地址） 2:内嵌字幕
	 */
	public String getFileLocation() {
		return fileLocation;
	}

	/**
	 * 设置 1:外部文件字幕（需要指明本地文件地址） 2:内嵌字幕.
	 *
	 * @param fileLocation 1:外部文件字幕（需要指明本地文件地址） 2:内嵌字幕
	 */
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	/**
	 * 获取 是否作为截图的字幕显示在网页上.
	 *
	 * @return 是否作为截图的字幕显示在网页上
	 */
	public Integer getShowFlg() {
		return showFlg;
	}

	/**
	 * 设置 是否作为截图的字幕显示在网页上.
	 *
	 * @param showFlg 是否作为截图的字幕显示在网页上
	 */
	public void setShowFlg(Integer showFlg) {
		this.showFlg = showFlg;
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
	 * 获取 包含某种属性文字.
	 *
	 * @return 包含某种属性文字
	 */
	public String getFilterInclude() {
		return filterInclude;
	}

	/**
	 * 设置 包含某种属性文字.
	 *
	 * @param filterInclude 包含某种属性文字
	 */
	public void setFilterInclude(String filterInclude) {
		this.filterInclude = filterInclude;
	}

	/**
	 * 获取 排除某种属性文字.
	 *
	 * @return 排除某种属性文字
	 */
	public String getFilterRemove() {
		return filterRemove;
	}

	/**
	 * 设置 排除某种属性文字.
	 *
	 * @param filterRemove 排除某种属性文字
	 */
	public void setFilterRemove(String filterRemove) {
		this.filterRemove = filterRemove;
	}

	/**
	 * 获取 当前字幕文件需要偏移的时间，单位毫秒.
	 *
	 * @return 当前字幕文件需要偏移的时间，单位毫秒
	 */
	public Long getOffsetTime() {
		return offsetTime;
	}

	/**
	 * 设置 当前字幕文件需要偏移的时间，单位毫秒.
	 *
	 * @param offsetTime 当前字幕文件需要偏移的时间，单位毫秒
	 */
	public void setOffsetTime(Long offsetTime) {
		this.offsetTime = offsetTime;
	}

}
