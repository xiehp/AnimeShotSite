package xie.animeshotsite.constants;

import com.yjysh.framework.common.utils.props.PropsUtil;

public class ShotConstants {

	/** 本地根路径 */
	public static final String LOCAL_ROOT_PATH = PropsUtil.getProperty("local.root.path");

	/** 本地动画文件夹 */
	public static final String LOCAL_ROOT_ANIME_PATH = PropsUtil.getProperty("local.root.anime.folder");

	/** 本地截图文件夹 */
	public static final String LOCAL_ROOT_SHOT_PATH = PropsUtil.getProperty("local.root.shot.folder");
	


	/** 网站图片URL获取方式 本地 */
	public static final String IMAGE_URL_GET_MODE_LOCAL = "local";
	/** 网站图片URL获取方式 贴图库 */
	public static final String IMAGE_URL_GET_MODE_TIETUKU = "tietuku";
	/** 网站图片URL获取方式 腾讯 */
	public static final String IMAGE_URL_GET_MODE_TENXUN = "tengxun";


	/** 图片类型  动画 */
	public static final String IMAGE_URL_TYPE_ANIME = "anime";
	/** 图片类型  剧集 */
	public static final String IMAGE_URL_TYPE_EPISODE = "episode";
	/** 图片类型  截图 */
	public static final String IMAGE_URL_TYPE_SHOT = "shot";
	/** 图片类型  图片 */
	public static final String IMAGE_URL_TYPE_IMAGE = "image";
	/** 图片类型  无图 */
	public static final String IMAGE_URL_TYPE_NO_IMAGE = "noimage";
}
