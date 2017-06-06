package xie.animeshotsite.utils;

import java.io.File;

import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.common.string.XStringUtils;

/**
 * 动画的路径和截图路径规则<br>
 * 路径由"root path", "detail path", "file name"组成<br>
 * 动画和截图的root path规则："盘符:\路径名1\路径名2\路径名X\网站设定动画或截图文件名"<br>
 * 在动画，剧集，截图表中，只包含"盘符:\路径名1\路径名2\路径名X，而不包含网站设定动画或截图文件名"<br>
 * 动画和截图的detail path规则："\路径名1\路径名2\路径名X\剧集的唯一标识number"<br>
 * 在动画，剧集，截图表中，只包含"\路径名1\路径名2\路径名X"，而不包含"剧集的唯一标识number"<br>
 * <br>
 * 取值规则<br>
 * 动画：剧集表中路径->动画表中路径->取默认值<br>
 * 截图：截图表中路径->剧集表中截图路径->剧集表中动画路径->动画表中路径->默认值<br>
 * <br>
 * 本类规则<br>
 * 函数名中带full，表明是从盘符开始的完整路径<br>
 * 返回File类型， 一定是完整路径<br>
 * 返回String类型的path，一定是相对路径 <br>
 *
 */
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

	/**
	 * 获取默认的root路径
	 */
	public static File getRootDefault() {
		return new File(ShotCoreConstants.LOCAL_ROOT_PATH);
	}

	/**
	 * 获取默认的root路径
	 */
	public static File getRootDefault(String typePath) {
		return new File(ShotCoreConstants.LOCAL_ROOT_PATH, typePath);
	}

	/**
	 * 获取默认的带动画文件夹的root路径
	 */
	public static File getAnimeRootDefault() {
		return new File(ShotCoreConstants.LOCAL_ROOT_PATH, ShotCoreConstants.LOCAL_ROOT_ANIME_PATH);
	}

	/**
	 * 获取默认的带截图文件夹的root路径
	 */
	public static File getShotRootDefault() {
		return new File(ShotCoreConstants.LOCAL_ROOT_PATH, ShotCoreConstants.LOCAL_ROOT_SHOT_PATH);
	}

	/**
	 * 追加动画文件夹
	 */
	public static String addAnimePath(String path) {
		return new File(path, ShotCoreConstants.LOCAL_ROOT_ANIME_PATH).getPath();
	}

	/**
	 * 追加截图文件夹
	 */
	public static String addShotPath(String path) {
		return new File(path, ShotCoreConstants.LOCAL_ROOT_SHOT_PATH).getPath();
	}

	/**
	 * 追加动画文件夹
	 */
	public static File addAnimePath(File file) {
		return new File(file, ShotCoreConstants.LOCAL_ROOT_ANIME_PATH);
	}

	/**
	 * 追加截图文件夹
	 */
	public static File addShotPath(File file) {
		return new File(file, ShotCoreConstants.LOCAL_ROOT_SHOT_PATH);
	}

	/**
	 * 获取带动画文件夹的root路径
	 */
	public static File getAnimeRoot(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
		if (animeEpisode != null && XStringUtils.isNotBlank(animeEpisode.getLocalRootPath())) {
			return new File(animeEpisode.getLocalRootPath(), ShotCoreConstants.LOCAL_ROOT_ANIME_PATH);
		}

		if (animeInfo != null && XStringUtils.isNotBlank(animeInfo.getLocalRootPath())) {
			return new File(animeInfo.getLocalRootPath(), ShotCoreConstants.LOCAL_ROOT_ANIME_PATH);
		}

		return getAnimeRootDefault();
	}

	/**
	 * 获取带截图文件夹的root路径
	 */
	public static File getShotRoot(ShotInfo shotInfo, AnimeEpisode animeEpisode, AnimeInfo animeInfo) {
		if (shotInfo != null) {
			// if (XStringUtils.isNotBlank(shotInfo.getLocalRootPath())) {
			// return new File(shotInfo.getLocalRootPath(), ShotConstants.LOCAL_ROOT_SHOT_PATH);
			// }
		}

		if (animeEpisode != null) {
			if (XStringUtils.isNotBlank(animeEpisode.getShotLocalRootPath())) {
				return new File(animeEpisode.getShotLocalRootPath(), ShotCoreConstants.LOCAL_ROOT_SHOT_PATH);
			}

			if (XStringUtils.isNotBlank(animeEpisode.getLocalRootPath())) {
				return new File(animeEpisode.getLocalRootPath(), ShotCoreConstants.LOCAL_ROOT_SHOT_PATH);
			}
		}

		if (animeInfo != null) {
			if (XStringUtils.isNotBlank(animeInfo.getLocalDetailPath())) {
				return new File(animeInfo.getLocalDetailPath(), ShotCoreConstants.LOCAL_ROOT_SHOT_PATH);
			}
		}

		return getShotRootDefault();
	}

	/**
	 * 获取默认的带动画文件夹的detail路径
	 */
	public static File getAnimeDetailFolderDefault(String detailPath) {
		return new File(getAnimeRootDefault(), detailPath);
	}

	/**
	 * 获取默认的带截图文件夹的detail路径
	 */
	public static File getShotDetailFolderDefault(String detailPath, String number) {
		File file = new File(getShotRootDefault(), detailPath);
		file = new File(file, number);
		return file;
	}

	/**
	 * 获取动画的detail路径
	 */
	public static String getDetailPath(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
		if (animeEpisode != null && XStringUtils.isNotBlank(animeEpisode.getLocalDetailPath())) {
			return animeEpisode.getLocalDetailPath();
		}

		if (animeInfo != null && XStringUtils.isNotBlank(animeInfo.getLocalDetailPath())) {
			return animeInfo.getLocalDetalPath();
		}

		return null;
	}

	/**
	 * 获取截图的detail路径
	 */
	public static String getDetailPath(ShotInfo shotInfo, AnimeEpisode animeEpisode, AnimeInfo animeInfo) {
		// if (shotInfo != null && XStringUtils.isNotBlank(shotInfo.getLocalDetailPath())) {
		// return shotInfo.getLocalDetailPath();
		// }

		if (animeEpisode != null) {
			if (XStringUtils.isNotBlank(animeEpisode.getShotLocalDetailPath())) {
				return animeEpisode.getShotLocalDetailPath();
			}

			if (XStringUtils.isNotBlank(animeEpisode.getLocalDetailPath())) {
				return animeEpisode.getLocalDetailPath();
			}
		}

		if (animeInfo != null) {
			if (XStringUtils.isNotBlank(animeInfo.getLocalDetailPath())) {
				return animeInfo.getLocalDetailPath();
			}
		}

		return null;
	}

	// /**
	// * 获取动画的detail路径
	// */
	// public static String getAnimeDetailPath(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
	// String detailPath = getDetailPath(animeInfo, animeEpisode);
	//
	// if (XStringUtils.isNotBlank(detailPath)) {
	// return new File(getAnimeRoot(animeInfo, animeEpisode), detailPath).getPath();
	// }
	//
	// // 没有设定detail路径
	// return getAnimeRoot(animeInfo, animeEpisode).getPath();
	// }
	//
	// /**
	// * 获取截图的detail路径
	// */
	// public static String getAnimeDetailPath(ShotInfo shotInfo, AnimeEpisode animeEpisode, AnimeInfo animeInfo) {
	// String detailPath = getDetailPath(shotInfo, animeEpisode, animeInfo);
	//
	// if (XStringUtils.isNotBlank(detailPath)) {
	// return new File(getShotRoot(shotInfo, animeEpisode, animeInfo), detailPath).getPath();
	// }
	//
	// // 没有设定detail路径
	// return getShotRoot(shotInfo, animeEpisode, animeInfo).getPath();
	// }

	/**
	 * 获取带动画文件夹的完整detail路径<br>
	 * root path/anime/detail path<br>
	 */
	public static File getAnimeDetailFolder(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
		String detailPath = getDetailPath(animeInfo, animeEpisode);

		if (XStringUtils.isNotBlank(detailPath)) {
			return new File(getAnimeRoot(animeInfo, animeEpisode), detailPath);
		}

		// 没有设定detail路径
		return getAnimeRoot(animeInfo, animeEpisode);
	}

	/**
	 * 获取带动画文件夹的完整detail路径,添加[自动下载资源]目录用于下载bt和视频文件<br>
	 * root path/anime/detail/自动下载资源 path<br>
	 */
	public static File getAnimeDetailFolderWithTorrent(AnimeInfo animeInfo, AnimeEpisode animeEpisode, String torrenName) {
		File detailPath = getAnimeDetailFolder(animeInfo, animeEpisode);
		File torrentFile = new File(detailPath, "自动下载资源");
		torrentFile = new File(torrentFile, torrenName);
		return torrentFile;
	}

	/**
	 * 获取带截图文件夹的完整detail路径<br>
	 * root path/shot/detail path/number<br>
	 */
	public static File getShotDetailFolder(ShotInfo shotInfo, AnimeEpisode animeEpisode, AnimeInfo animeInfo) {
		String detailPath = getDetailPath(shotInfo, animeEpisode, animeInfo);

		if (XStringUtils.isNotBlank(detailPath)) {
			File file = new File(getShotRoot(shotInfo, animeEpisode, animeInfo), detailPath);
			if (animeEpisode != null) {
				String number = animeEpisode.getNumber();
				if (number != null) {
					file = new File(file, number);
				}
			}
			return file;
		}

		return getShotRoot(shotInfo, animeEpisode, animeInfo);
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

	/**
	 * 获取完整的动画路径
	 */
	public static File getAnimeFullFilePath(AnimeInfo animeInfo, AnimeEpisode animeEpisode, String fileName) {
		File fileDetailPath = getAnimeDetailFolder(animeInfo, animeEpisode);
		File fileFullPath = new File(fileDetailPath, fileName);

		return fileFullPath;
	}

	/**
	 * 获取完整的截图路径
	 */
	public static File getShotFullFilePath(ShotInfo shotInfo, AnimeEpisode animeEpisode, AnimeInfo animeInfo) {
		File fileDetailPath = getShotDetailFolder(shotInfo, animeEpisode, animeInfo);
		File fileFullPath = new File(fileDetailPath, shotInfo.getLocalFileName());

		return fileFullPath;
	}

	/**
	 * 获取普通文件夹的路径
	 */
	public static File getCommonFolder(String rootPath, String detailPath) {
		File folder = new File(rootPath, detailPath);
		return folder;
	}

	/**
	 * 获取普通文件的路径
	 */
	public static File getCommonFile(String rootPath, String detailPath, String fileName) {
		File folder = new File(rootPath, detailPath);
		File file = new File(folder, fileName);
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
