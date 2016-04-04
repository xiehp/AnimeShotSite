package xie.animeshotsite.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.spring.SpringUtil;
import xie.base.entity.BaseEntity;
import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.repository.BaseSearchFilter;
import xie.base.repository.BaseSpecifications;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.common.number.XNumberUtils;

@Service
public class ShotInfoService extends BaseService<ShotInfo, String> {

	@Autowired
	private ShotInfoDao shotInfoDao;
	@Autowired
	private AnimeInfoDao animeInfoDao;
	@Autowired
	private AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	private EntityCache entityCache;

	@Override
	public BaseRepository<ShotInfo, String> getBaseRepository() {
		return shotInfoDao;
	}

	public ShotInfo findPrevious(String animeEpisodeId, long timeStamp) {
		return shotInfoDao.findPrevious(animeEpisodeId, timeStamp);
	}

	public ShotInfo findNext(String animeEpisodeId, long timeStamp) {
		return shotInfoDao.findNext(animeEpisodeId, timeStamp);
	}

	/**
	 * @param updateFlg 如果已经存在，是否进行更新
	 * @return
	 */
	public ShotInfo saveShotInfo(String animeInfoId, String animeEpisodeId, long timeStamp, String rootPath, String localDetailPath, String fileName, boolean updateFlg) {
		ShotInfo shotInfo = shotInfoDao.findByAnimeEpisodeIdAndTimeStamp(animeEpisodeId, timeStamp);
		if (shotInfo == null) {
			shotInfo = new ShotInfo();
		} else {
			if (!updateFlg) {
				return shotInfo;
			}
		}
		shotInfo.setAnimeInfoId(animeInfoId);
		shotInfo.setAnimeEpisodeId(animeEpisodeId);
		shotInfo.setTimeStamp(timeStamp);
		// shotInfo.setLocalRootPath(rootPath);
		// shotInfo.setLocalDetailPath(localDetailPath);
		shotInfo.setLocalFileName(fileName);
		return shotInfoDao.save(shotInfo);
	}

	public ShotInfo updateTietukuUrl(String animeEpisodeId, long timeStamp, String tietukuUrlId, String tietukuUrlPrefix) {
		ShotInfo shotInfo = shotInfoDao.findByAnimeEpisodeIdAndTimeStamp(animeEpisodeId, timeStamp);
		shotInfo.setTietukuUrlPrefix(tietukuUrlPrefix);
		shotInfo.setTietukuUrlId(tietukuUrlId);
		return shotInfoDao.save(shotInfo);
	}

	/**
	 * 获取站长推荐图片
	 * 
	 * @param inDay 几天以内推荐的
	 * @return
	 */
	public List<ShotInfo> getMasterRecommandShotList(Integer inDay, int listCount) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("GT_" + ShotInfo.COLUMN_MASTER_RECOMMEND_DATE, DateUtil.seekDate(DateUtil.getCurrentDate(), -inDay));
		Page<ShotInfo> page = searchAllShots(searchParams, 1, listCount, BaseEntity.COLUMN_CREATE_BY);
		List<ShotInfo> list = page.getContent();
		list = setParentData(list);
		return list;
	}

	/**
	 * 获得最新截图
	 * 
	 * @param listCount
	 * @return
	 */
	public List<ShotInfo> getNewestShotList(int listCount) {
		Map<String, Object> searchParams = new HashMap<>();
		List<Order> orders = new ArrayList<>();
		Order order = new Order(Direction.DESC, ShotInfo.COLUMN_CREATE_DATE);
		orders.add(order);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(1, listCount, orders);
		Page<ShotInfo> page = searchAllShots(searchParams, pageRequest);

		List<ShotInfo> list = page.getContent();
		list = setParentData(list);
		return list;
	}

	public List<ShotInfo> getPublicLikeShotList(int listCount) {
		Map<String, Object> searchParams = new HashMap<>();
		List<Order> orders = new ArrayList<>();
		Order order = new Order(Direction.DESC, ShotInfo.COLUMN_PUBLIC_LIKE_COUNT);
		orders.add(order);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(1, listCount, orders);
		Page<ShotInfo> page = searchAllShots(searchParams, pageRequest);
		List<ShotInfo> list = page.getContent();
		list = setParentData(list);
		return page.getContent();
	}

	public List<ShotInfo> setParentData(List<ShotInfo> list) {
		if (list == null) {
			return list;
		}
		for (ShotInfo shotInfo : list) {
			shotInfo.setAnimeInfo(entityCache.findOne(animeInfoDao, shotInfo.getAnimeInfoId()));
			shotInfo.setAnimeEpisode(entityCache.findOne(animeEpisodeDao, shotInfo.getAnimeEpisodeId()));
		}
		return list;
	}

	public Page<ShotInfo> searchAllShots(Map<String, Object> searchParams, int pageNumber, int defaultPageSize, String sortType) {

		// 创建分页对象
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType);

		return searchAllShots(searchParams, pageRequest);
	}

	public Page<ShotInfo> searchAllShots(Map<String, Object> searchParams, PageRequest pageRequest) {

		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");

		// Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// Specification<ShotInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), ShotInfo.class);
		Map<String, BaseSearchFilter> filters = BaseSearchFilter.parse(searchParams);
		Specification<ShotInfo> spec = BaseSpecifications.bySearchFilter(filters.values(), ShotInfo.class);
		Page<ShotInfo> userPage = shotInfoDao.findAll(spec, pageRequest);

		return userPage;
	}

	public ShotInfo publicLikeAdd(String id) {
		ShotInfo shotInfo = null;
		synchronized (this) {
			shotInfo = findOne(id);
			if (shotInfo != null) {
				shotInfo.setPublicLikeCount(XNumberUtils.add(shotInfo.getPublicLikeCount(), 1L));
				shotInfo = shotInfoDao.save(shotInfo);
			}
		}
		return shotInfo;
	}

	public ShotInfo masterLikeAdd(String id) {
		ShotInfo shotInfo = null;
		synchronized (this) {
			shotInfo = findOne(id);
			if (shotInfo != null) {
				shotInfo.setPublicLikeCount(XNumberUtils.add(shotInfo.getPublicLikeCount(), 1L));
				shotInfo.setMasterRecommendRank(XNumberUtils.add(shotInfo.getMasterRecommendRank(), 1L));
				shotInfo.setMasterRecommendDate(DateUtil.getCurrentDate());
				shotInfo = shotInfoDao.save(shotInfo);
			}
		}
		return shotInfo;
	}

	public List<ShotInfo> findRandom(Integer number) {
		long count = shotInfoDao.count();
		Integer from = RandomUtils.nextInt((int) count);
		List<ShotInfo> list = shotInfoDao.findRandom(from, number);
		list = setParentData(list);
		return list;
	}

	public static void main(String[] args) {
		ShotInfoService shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		shotInfoService.findRandom(22);
		shotInfoService.findRandom(22);
		shotInfoService.findRandom(22);
		shotInfoService.findRandom(22);
	}
}
