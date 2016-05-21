package xie.animeshotsite.db.entity;

import javax.persistence.MappedSuperclass;

import com.tietuku.entity.util.TietukuUtils;

import xie.animeshotsite.constants.ShotConstants;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.base.entity.BaseEntity;
import xie.common.utils.props.PropsUtil;

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

	public String getTietukuUrlId() {
		return tietukuUrlId;
	}

	public void setTietukuUrlId(String tietukuUrlId) {
		this.tietukuUrlId = tietukuUrlId;
	}

	public String getTietukuUrlPrefix() {
		return tietukuUrlPrefix;
	}

	public void setTietukuUrlPrefix(String tietukuUrlPrefix) {
		this.tietukuUrlPrefix = tietukuUrlPrefix;
	}

	public String getTietukuOUrl() {
		return TietukuUtils.getImageOriginalUrl(getTietukuUrlPrefix(), getTietukuUrlId());
	}

	public String getTietukuSUrl() {
		return TietukuUtils.getImageShowUrl(getTietukuUrlPrefix(), getTietukuUrlId());
	}

	public String getTietukuTUrl() {
		return TietukuUtils.getImageThumbnailUrl(getTietukuUrlPrefix(), getTietukuUrlId());
	}

	/**
	 * 获得小图URL
	 */
	public String getUrlS() {
		if (ShotConstants.IMAGE_URL_GET_MODE_TIETUKU.equals(ShotSiteSetup.IMAGE_URL_GET_MODE)) {
			return TietukuUtils.getImageThumbnailUrl(getTietukuUrlPrefix(), getTietukuUrlId());
		} else {
			return "\\image\\type\\id"; // TODO 本地图片获取方式
		}
	}

	/**
	 * 获得中图URL
	 */
	public String getUrlM() {
		if (ShotConstants.IMAGE_URL_GET_MODE_TIETUKU.equals(ShotSiteSetup.IMAGE_URL_GET_MODE)) {
			return TietukuUtils.getImageShowUrl(getTietukuUrlPrefix(), getTietukuUrlId());
		} else {
			return "\\image\\image\\id"; // TODO 本地图片获取方式
		}
	}

	/**
	 * 获得大图URL
	 */
	public String getUrlL() {
		if (ShotConstants.IMAGE_URL_GET_MODE_TIETUKU.equals(ShotSiteSetup.IMAGE_URL_GET_MODE)) {
			return TietukuUtils.getImageOriginalUrl(getTietukuUrlPrefix(), getTietukuUrlId());
		} else {
			return "\\image\\type\\id"; // TODO 本地图片获取方式
		}
	}
}
