package xie.animeshotsite.db.entity;

import javax.persistence.MappedSuperclass;

import com.tietuku.entity.util.TietukuUtils;

import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.base.entity.BaseEntity;

@MappedSuperclass
public abstract class BaseTietukuUrl extends BaseEntity {

	private static final long serialVersionUID = 6035046572240046161L;

	/** 贴图库所在url的ID */
	private String tietukuUrlId;

	/** 贴图库所在url的前缀 如http://www.xxx.com/ */
	private String tietukuUrlPrefix;

	public BaseTietukuUrl() {
	}

	public BaseTietukuUrl(AnimeInfo animeInfo) {
	}

	public BaseTietukuUrl(AnimeInfo animeInfo, AnimeEpisode animeEpisode) {
	}

	public BaseTietukuUrl(AnimeInfo animeInfo, AnimeEpisode animeEpisode, ShotInfo shotInfo) {
	}

	/**
	 * 获取 贴图库所在url的ID.
	 *
	 * @return 贴图库所在url的ID
	 */
	public String getTietukuUrlId() {
		return tietukuUrlId;
	}

	/**
	 * 设置 贴图库所在url的ID.
	 *
	 * @param tietukuUrlId 贴图库所在url的ID
	 */
	public void setTietukuUrlId(String tietukuUrlId) {
		this.tietukuUrlId = tietukuUrlId;
	}

	/**
	 * 获取 贴图库所在url的前缀 如http://www.
	 *
	 * @return 贴图库所在url的前缀 如http://www
	 */
	public String getTietukuUrlPrefix() {
		return tietukuUrlPrefix;
	}

	/**
	 * 设置 贴图库所在url的前缀 如http://www.
	 *
	 * @param tietukuUrlPrefix 贴图库所在url的前缀 如http://www
	 */
	public void setTietukuUrlPrefix(String tietukuUrlPrefix) {
		this.tietukuUrlPrefix = tietukuUrlPrefix;
	}

	/**
	 * 获得tietuku O url.
	 *
	 * @return tietuku O url
	 */
	public String getTietukuOUrl() {
		return TietukuUtils.getImageOriginalUrl(getTietukuUrlPrefix(), getTietukuUrlId());
	}

	/**
	 * 获得tietuku S url.
	 *
	 * @return tietuku S url
	 */
	public String getTietukuSUrl() {
		return TietukuUtils.getImageShowUrl(getTietukuUrlPrefix(), getTietukuUrlId());
	}

	/**
	 * 获得tietuku T url.
	 *
	 * @return tietuku T url
	 */
	public String getTietukuTUrl() {
		return TietukuUtils.getImageThumbnailUrl(getTietukuUrlPrefix(), getTietukuUrlId());
	}

	/**
	 * 获得小图URL.
	 *
	 * @return url S
	 */
	public String getUrlS() {
		if (ShotCoreConstants.IMAGE_URL_GET_MODE_TIETUKU.equals(ShotSiteSetup.IMAGE_URL_GET_MODE)) {
			return TietukuUtils.getImageThumbnailUrl(getTietukuUrlPrefix(), getTietukuUrlId());
		} else {
			return "\\image\\type\\id"; // TODO 本地图片获取方式
		}
	}

	/**
	 * 获得中图URL.
	 *
	 * @return url M
	 */
	public String getUrlM() {
		if (ShotCoreConstants.IMAGE_URL_GET_MODE_TIETUKU.equals(ShotSiteSetup.IMAGE_URL_GET_MODE)) {
			return TietukuUtils.getImageShowUrl(getTietukuUrlPrefix(), getTietukuUrlId());
		} else {
			return "\\image\\image\\id"; // TODO 本地图片获取方式
		}
	}

	/**
	 * 获得大图URL.
	 *
	 * @return url L
	 */
	public String getUrlL() {
		if (ShotCoreConstants.IMAGE_URL_GET_MODE_TIETUKU.equals(ShotSiteSetup.IMAGE_URL_GET_MODE)) {
			return TietukuUtils.getImageOriginalUrl(getTietukuUrlPrefix(), getTietukuUrlId());
		} else {
			return "\\image\\type\\id"; // TODO 本地图片获取方式
		}
	}
}
