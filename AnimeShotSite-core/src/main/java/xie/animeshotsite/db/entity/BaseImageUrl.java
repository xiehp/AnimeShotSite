package xie.animeshotsite.db.entity;

import java.io.File;

import javax.persistence.MappedSuperclass;

import xie.animeshotsite.utils.FilePathUtils;

@MappedSuperclass
public abstract class BaseImageUrl extends BaseTietukuUrl {

	private static final long serialVersionUID = -3928494777094443090L;

	/** 本地所在根路径 */
	private String localRootPath;

	/** 本地所在相对路径 */
	private String localDetailPath;

	/** 本地所在文件名 */
	private String localFileName;

	public BaseImageUrl() {
	}

	public BaseImageUrl(AnimeInfo animeInfo) {
	}

	public BaseImageUrl(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
	}

	public BaseImageUrl(AnimeInfo animeInfo, AnimeEpisode animeEpisode, ShotInfo shotInfo) {
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

	public File getLocalFullFilePath() {
		return FilePathUtils.getCommonFilePath(getLocalRootPath(), getLocalDetailPath(), getLocalFileName());
	}
}
