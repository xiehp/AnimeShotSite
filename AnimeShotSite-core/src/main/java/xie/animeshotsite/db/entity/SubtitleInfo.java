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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Integer getShowFlg() {
		return showFlg;
	}

	public void setShowFlg(Integer showFlg) {
		this.showFlg = showFlg;
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

}
