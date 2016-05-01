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

	public String getAnimeEpisodeId() {
		return animeEpisodeId;
	}

	public void setAnimeEpisodeId(String animeEpisodeId) {
		this.animeEpisodeId = animeEpisodeId;
	}

	public AnimeEpisode getAnimeEpisode() {
		return animeEpisode;
	}

	public void setAnimeEpisode(AnimeEpisode animeEpisode) {
		this.animeEpisode = animeEpisode;
	}

	public String getSubtitleInfoId() {
		return subtitleInfoId;
	}

	public void setSubtitleInfoId(String subtitleInfoId) {
		this.subtitleInfoId = subtitleInfoId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(Integer lineIndex) {
		this.lineIndex = lineIndex;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public Long getStartTime() {
		return startTime;
	}

	public String getStartTimeMinSec() {
		return DateUtil.formatTime(startTime, 0);
	}

	public String getStartTimeMicroSec() {
		return DateUtil.formatTime(startTime, 1);
	}

	public String getStartTimeMinSecMicro() {
		return DateUtil.formatTime(startTime, 2);
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public String getEndTimeMinSec() {
		return DateUtil.formatTime(endTime, 0);
	}

	public String getEndTimeMicroSec() {
		return DateUtil.formatTime(endTime, 1);
	}

	public String getEndTimeMinSecMicro() {
		return DateUtil.formatTime(endTime, 2);
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getName() {
		return name;
	}

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
