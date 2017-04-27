package xie.animeshotsite.db.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.base.repository.BaseRepository;
import xie.base.repository.BaseSearchFilter.BaseOperator;
import xie.base.service.BaseService;
import xie.common.Constants;

@Service
public class AnimeInfoService extends BaseService<AnimeInfo, String> {

	@Autowired
	private AnimeInfoDao animeInfoDao;
	@Autowired
	private ImageUrlService imageUrlService;

	@Override
	public BaseRepository<AnimeInfo, String> getBaseRepository() {
		return animeInfoDao;
	}

	/** 获取所有需要进行截图的动画列表 */
	public List<AnimeInfo> findByProcessAction(Integer processAction) {
		return animeInfoDao.findByProcessAction(processAction);
	}

	/**
	 * 新增加一个图片title
	 */
	public void saveTitleUrl(AnimeInfo animeInfo, String rootPath, String detailPath, String name, String tietukuImageUrlId, String tietukuImageUrlPrefix) {
		ImageUrl imageUrl = imageUrlService.saveImageInfo(rootPath, detailPath, name, tietukuImageUrlId, tietukuImageUrlPrefix);
		animeInfo.setTitleUrlId(imageUrl.getId());
		animeInfoDao.save(animeInfo);
	}

	public List<AnimeInfo> findRandom(int number) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put(BaseOperator.EQ.getStr(AnimeInfo.COLUMN_SHOW_FLG), Constants.FLAG_INT_YES);
		List<AnimeInfo> list = findRandom(-1, number, AnimeInfo.class, searchParams);
		return list;
	}
}
