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

	/**
	 * 获取 本地所在根路径.
	 *
	 * @return 本地所在根路径
	 */
	public String getLocalRootPath() {
		return localRootPath;
	}

	/**
	 * 设置 本地所在根路径.
	 *
	 * @param localRootPath 本地所在根路径
	 */
	public void setLocalRootPath(String localRootPath) {
		this.localRootPath = localRootPath;
	}

	/**
	 * 获取 本地所在相对路径.
	 *
	 * @return 本地所在相对路径
	 */
	public String getLocalDetailPath() {
		return localDetailPath;
	}

	/**
	 * 设置 本地所在相对路径.
	 *
	 * @param localDetailPath 本地所在相对路径
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
	 * 获得local full file path.
	 *
	 * @return local full file path
	 */
	public File getLocalFullFilePath() {
		return FilePathUtils.getCommonFile(getLocalRootPath(), getLocalDetailPath(), getLocalFileName());
	}
}
