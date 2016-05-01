package xie.animeshotsite.utils;

import java.io.File;

import xie.animeshotsite.constants.ShotConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.common.string.XStringUtils;

public class FilePathUtils {

	// /**
	// * 前后增加或去除路径分割符
	// *
	// * @return
	// */
	// public static String getStandardPath(String path, boolean start, boolean end) {
	// File file = new File(path);
	// file.getPath();
	// }

	public static File getRootDefault() {
		return new File(ShotConstants.LOCAL_ROOT_PATH);
	}

	public static File getAnimeRootDefault() {
		return new File(ShotConstants.LOCAL_ROOT_PATH, ShotConstants.LOCAL_ROOT_ANIME_PATH);
	}

	public static File getShotRootDefault() {
		return new File(ShotConstants.LOCAL_ROOT_PATH, ShotConstants.LOCAL_ROOT_SHOT_PATH);
	}

	public static File getAnimeRoot(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
		if (animeEpisode != null && XStringUtils.isNotBlank(animeEpisode.getLocalRootPath())) {
			return new File(animeEpisode.getLocalRootPath(), ShotConstants.LOCAL_ROOT_ANIME_PATH);
		}

		if (animeInfo != null && XStringUtils.isNotBlank(animeInfo.getLocalRootPath())) {
			return new File(animeInfo.getLocalRootPath(), ShotConstants.LOCAL_ROOT_ANIME_PATH);
		}

		return getAnimeRootDefault();
	}

	public static File getShotRoot(AnimeEpisode animeEpisode, ShotInfo shotInfo) {
		if (shotInfo != null && animeEpisode.getShotLocalRootPath() != null) {
			return new File(animeEpisode.getShotLocalRootPath(), ShotConstants.LOCAL_ROOT_SHOT_PATH);
		}

		return getShotRootDefault();
	}

	public static File getAnimeDetailFolderDefault(String detailPath) {
		return new File(getAnimeRootDefault(), detailPath);
	}

	public static File getShotDetailFolderDefault(String detailPath, String number) {
		File file = new File(getShotRootDefault(), detailPath);
		file = new File(file, number);
		return file;
	}

	public static File getAnimeDetailFolder(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
		String detailPath = null;
		if (animeEpisode != null) {
			detailPath = animeEpisode.getLocalDetailPath();
		}

		if (detailPath == null && animeInfo != null) {
			detailPath = animeInfo.getLocalDetalPath();
		}

		if (detailPath == null) {
			return getAnimeRoot(animeInfo, animeEpisode);
		} else {
			return new File(getAnimeRoot(animeInfo, animeEpisode), detailPath);
		}
	}

	public static File getShotDetailFolder(AnimeEpisode animeEpisode, ShotInfo shotInfo) {
		String detailPath = null;
		if (shotInfo != null) {
			detailPath = animeEpisode.getShotLocalDetailPath();
		}

		if (detailPath == null) {
			return getShotRoot(animeEpisode, shotInfo);
		} else {
			return new File(getShotRoot(animeEpisode, shotInfo), detailPath);
		}
	}

	public static File getAnimeFullFilePathDefault(String detailPath, String fileName) {
		File fileDetailPath = getAnimeDetailFolderDefault(detailPath);
		File fileFullPath = new File(fileDetailPath, fileName);

		return fileFullPath;
	}

	public static File getShotFullFilePathDefault(String detailPath, String number, String fileName) {
		File fileDetailPath = getShotDetailFolderDefault(detailPath, number);
		File fileFullPath = new File(fileDetailPath, fileName);

		return fileFullPath;
	}

	public static File getAnimeFullFilePath(AnimeInfo animeInfo, AnimeEpisode animeEpisode, String fileName) {
		File fileDetailPath = getAnimeDetailFolder(animeInfo, animeEpisode);
		File fileFullPath = new File(fileDetailPath, fileName);

		return fileFullPath;
	}

	public static File getShotFullFilePath(AnimeEpisode animeEpisode, ShotInfo shotInfo) {
		File fileDetailPath = getShotDetailFolder(animeEpisode, shotInfo);
		File fileFullPath = new File(fileDetailPath, shotInfo.getLocalFileName());

		return fileFullPath;
	}

	/**
	 * 获取普通文件的路径
	 * 
	 * @return
	 */
	public static File getCommonFilePath(String rootPath, String detailPath, String fileName) {
		File file = new File(rootPath, detailPath);
		file = new File(file, fileName);
		return file;
	}

	/**
	 * 获得找不到图片的图片路径
	 */
	public static File getNoImageFilePath() {
		// TODO 图片配置
		return new File("F:\\AnimeShotSite\\shot\\动画4\\719992.jpg");
	}
}
