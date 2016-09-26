package xie.animeshotsite.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;
import xie.common.date.DateUtil;

@Entity
@Table(name = SubtitleLine.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubtitleLine extends BaseEntity {

	private static final long serialVersionUID = 1L;
	public static final String COLUMN_START_TIME = "startTime";
	public static final String COLUMN_END_TIME = "endTime";

	public static final String TABLE_NAME = "subtitle_line";

	/** 动画信息的id */
	private String animeInfoId;

	@Transient
	private AnimeInfo animeInfo;

	/** 动画剧集ID */
	private String animeEpisodeId;

	@Transient
	private AnimeEpisode animeEpisode;

	/** 字幕信息ID */
	private String subtitleInfoId;

	/** 语言 */
	private String language;

	/** 字幕原始文件该字幕行的排序 */
	private Integer lineIndex;

	/** 字幕所在层级 */
	private Integer layer;

	/** 开始时间 */
	private Long startTime;

	/** 结束时间 */
	private Long endTime;

	/** 字幕文本 */
	private String text;

	private String style;

	private String name;

	//private String marginL;

	//private String marginR;

	//private String marginV;

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
	 * 获取 字幕信息ID.
	 *
	 * @return 字幕信息ID
	 */
	public String getSubtitleInfoId() {
		return subtitleInfoId;
	}

	/**
	 * 设置 字幕信息ID.
	 *
	 * @param subtitleInfoId 字幕信息ID
	 */
	public void setSubtitleInfoId(String subtitleInfoId) {
		this.subtitleInfoId = subtitleInfoId;
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
	 * 获取 字幕原始文件该字幕行的排序.
	 *
	 * @return 字幕原始文件该字幕行的排序
	 */
	public Integer getLineIndex() {
		return lineIndex;
	}

	/**
	 * 设置 字幕原始文件该字幕行的排序.
	 *
	 * @param lineIndex 字幕原始文件该字幕行的排序
	 */
	public void setLineIndex(Integer lineIndex) {
		this.lineIndex = lineIndex;
	}

	/**
	 * 获取 字幕所在层级.
	 *
	 * @return 字幕所在层级
	 */
	public Integer getLayer() {
		return layer;
	}

	/**
	 * 设置 字幕所在层级.
	 *
	 * @param layer 字幕所在层级
	 */
	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	/**
	 * 获取 开始时间.
	 *
	 * @return 开始时间
	 */
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * 获得start time min sec.
	 *
	 * @return start time min sec
	 */
	public String getStartTimeMinSec() {
		return DateUtil.formatTime(startTime, 0);
	}

	/**
	 * 获得start time micro sec.
	 *
	 * @return start time micro sec
	 */
	public String getStartTimeMicroSec() {
		return DateUtil.formatTime(startTime, 1);
	}

	/**
	 * 获得start time min sec micro.
	 *
	 * @return start time min sec micro
	 */
	public String getStartTimeMinSecMicro() {
		return DateUtil.formatTime(startTime, 2);
	}

	/**
	 * 设置 开始时间.
	 *
	 * @param startTime 开始时间
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获取 结束时间.
	 *
	 * @return 结束时间
	 */
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * 获得end time min sec.
	 *
	 * @return end time min sec
	 */
	public String getEndTimeMinSec() {
		return DateUtil.formatTime(endTime, 0);
	}

	/**
	 * 获得end time micro sec.
	 *
	 * @return end time micro sec
	 */
	public String getEndTimeMicroSec() {
		return DateUtil.formatTime(endTime, 1);
	}

	/**
	 * 获得end time min sec micro.
	 *
	 * @return end time min sec micro
	 */
	public String getEndTimeMinSecMicro() {
		return DateUtil.formatTime(endTime, 2);
	}

	/**
	 * 设置 结束时间.
	 *
	 * @param endTime 结束时间
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取 字幕文本.
	 *
	 * @return 字幕文本
	 */
	public String getText() {
		return text;
	}

	/**
	 * 设置 字幕文本.
	 *
	 * @param text 字幕文本
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * 获得style.
	 *
	 * @return style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * 设置style.
	 *
	 * @param style style
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * 获得name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置name.
	 *
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

//	public String getMarginL() {
//		return marginL;
//	}
//
//	public void setMarginL(String marginL) {
//		this.marginL = marginL;
//	}
//
//	public String getMarginR() {
//		return marginR;
//	}
//
//	public void setMarginR(String marginR) {
//		this.marginR = marginR;
//	}
//
//	public String getMarginV() {
//		return marginV;
//	}
//
//	public void setMarginV(String marginV) {
//		this.marginV = marginV;
//	}

}
