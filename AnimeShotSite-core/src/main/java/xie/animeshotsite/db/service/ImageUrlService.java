package xie.animeshotsite.db.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ImageUrlDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.exception.XException;

@Service
public class ImageUrlService extends BaseService<ImageUrl, String> {

	@Resource
	private ImageUrlDao imageUrlDao;
	@Resource
	private ShotInfoDao shotInfoDao;
	@Resource
	private AnimeInfoDao animeInfoDao;
	@Resource
	private AnimeEpisodeDao animeEpisodeDao;

	@Override
	public BaseRepository<ImageUrl, String> getBaseRepository() {
		return imageUrlDao;
	}

	public ImageUrl saveImageInfo(String rootPath, String detailPath, String name, String tietukuImageUrlId, String tietukuImageUrlPrefix) {
		ImageUrl imageUrl = new ImageUrl();
		imageUrl.setLocalRootPath(rootPath);
		imageUrl.setLocalDetailPath(detailPath);
		imageUrl.setLocalFileName(name);

		imageUrl.setTietukuUrlId(tietukuImageUrlId);
		imageUrl.setTietukuUrlPrefix(tietukuImageUrlPrefix);

		return imageUrlDao.save(imageUrl);
	}

	public AnimeInfo saveAnimeTitleImage(String animeInfoId, String animeShotId) throws XException {
		AnimeInfo animeInfo = animeInfoDao.findOne(animeInfoId);

		ImageUrl imageUrl = saveAnimeTitleImageInfo(animeInfo.getTitleUrlId(), animeShotId);

		if (imageUrl != null && !imageUrl.getId().equals(animeInfo.getTitleUrlId())) {
			animeInfo.setTitleUrlId(imageUrl.getId());
			animeInfoDao.save(animeInfo);
		}

		return animeInfo;
	}

	public AnimeEpisode saveEpisodeTitleImage(String animeEpisodeId, String animeShotId) throws XException {
		AnimeEpisode animeEpisode = animeEpisodeDao.findOne(animeEpisodeId);

		ImageUrl imageUrl = saveAnimeTitleImageInfo(animeEpisode.getTitleUrlId(), animeShotId);

		if (imageUrl != null && !imageUrl.getId().equals(animeEpisode.getTitleUrlId())) {
			animeEpisode.setTitleUrlId(imageUrl.getId());
			animeEpisodeDao.save(animeEpisode);
		}

		return animeEpisode;
	}

	/**
	 * 保存或变更动画或剧集的图片
	 * 
	 * @param imageUrlId 图片ID
	 * @param animeShotId 截图ID
	 * @throws XException 
	 */
	public ImageUrl saveAnimeTitleImageInfo(String imageUrlId, String animeShotId) throws XException {
		ImageUrl imageUrl = findOne(imageUrlId);
		if (imageUrl == null) {
			imageUrl = new ImageUrl();
		}

		ShotInfo shotInfo = shotInfoDao.findById(animeShotId);
		if (shotInfo == null) {
			throw new XException("需要设置的截图ID不存在："+ animeShotId);
		}

		imageUrl.setLocalRootPath(null);
		imageUrl.setLocalDetailPath(null);
		AnimeEpisode animeEpisode = animeEpisodeDao.findOne(shotInfo.getAnimeEpisodeId());
		if (animeEpisode != null) {
			imageUrl.setLocalRootPath(animeEpisode.getShotLocalRootPath());
			imageUrl.setLocalDetailPath(animeEpisode.getShotLocalDetailPath());
		}
		imageUrl.setLocalFileName(shotInfo.getLocalFileName());

		imageUrl.setTietukuUrlId(shotInfo.getTietukuUrlId());
		imageUrl.setTietukuUrlPrefix(shotInfo.getTietukuUrlPrefix());

		return imageUrlDao.save(imageUrl);
	}
}
