package xie.animeshotsite.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

import java.util.Date;

@Entity
@Table(name = SubtitleSearchHistory.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SubtitleSearchHistory extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String COLUMN_animeInfoId = "animeInfoId";
	public static final String COLUMN_animeEpisodeId = "animeEpisodeId";
	public static final String COLUMN_searchText = "searchText";

	public static final String TABLE_NAME = "subtitle_search_history";

	/** 动画信息的id */
	private String animeInfoId;

	@Transient
	private AnimeInfo animeInfo;

	/** 动画剧集ID */
	private String animeEpisodeId;

	@Transient
	private AnimeEpisode animeEpisode;

	/** 搜索文本 */
	private String searchText;

	/** 搜索次数 ... */
	private Integer searchCount;

	/** 首次搜索时间 */
	private Date firstDate;

	/** COOKIE_ID */
	private String cookieId;

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

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

	public Date getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}

	public String getCookieId() {
		return cookieId;
	}

	public void setCookieId(String cookieId) {
		this.cookieId = cookieId;
	}
}
