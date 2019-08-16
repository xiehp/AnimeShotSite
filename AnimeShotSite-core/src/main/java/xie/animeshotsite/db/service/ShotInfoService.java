package xie.animeshotsite.db.service;

import java.io.File;
import java.util.*;
import java.util.function.Function;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.vo.ShotInfoVO;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.repository.BaseSearchFilter.BaseOperator;
import xie.base.service.BaseService;
import xie.common.constant.XConst;
import xie.common.date.DateUtil;
import xie.common.number.XNumberUtils;
import xie.common.utils.XSSHttpUtil;
import xie.common.utils.XWaitTime;

@Service
public class ShotInfoService extends BaseService<ShotInfo, String> {

	@Resource
	private ShotInfoDao shotInfoDao;
	@Resource
	private AnimeInfoDao animeInfoDao;
	@Resource
	private AnimeEpisodeDao animeEpisodeDao;
	@Resource
	private EntityCache entityCache;
	@Resource
	private ShotSiteSetup shotSiteSetup;

	public ShotInfoVO convertToVO(ShotInfo shotInfo) {
		if (shotInfo == null) {
			return null;
		}

		ShotInfoVO shotInfoVO = new ShotInfoVO();
		BeanMapper.copy(shotInfo, shotInfoVO);

		String url = shotInfoVO.getTietukuOUrl();
		if (url != null) {
			url = XSSHttpUtil.changeTietukuDomain(url, shotSiteSetup.getSiteDomain(), shotSiteSetup.getTietukuChangeDoman());
		}
		shotInfoVO.setTietukuOUrlChangeDomain(url);

		return shotInfoVO;
	}

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
	 * 
	 * @param animeInfoId
	 * @param animeEpisodeId
	 * @param timeStamp
	 * @param originalTime
	 * @param rootPath 不需要
	 * @param localDetailPath 不需要
	 * @param fileName
	 * @param forceUpdateFlg 如果已经存在，是否进行更新
	 * @return
	 */
	public ShotInfo createShotInfo(String animeInfoId, String animeEpisodeId, long timeStamp, long originalTime, String rootPath, String localDetailPath, String fileName, boolean forceUpdateFlg) {
		ShotInfo shotInfo = shotInfoDao.findByAnimeEpisodeIdAndTimeStamp(animeEpisodeId, timeStamp);
		if (shotInfo == null) {
			shotInfo = new ShotInfo();
		} else {
			if (!forceUpdateFlg) {
				return shotInfo;
			}
		}
		shotInfo.setAnimeInfoId(animeInfoId);
		shotInfo.setAnimeEpisodeId(animeEpisodeId);
		shotInfo.setOriginalTime(originalTime);
		shotInfo.setTimeStamp(timeStamp);
		// shotInfo.setLocalRootPath(rootPath);
		// shotInfo.setLocalDetailPath(localDetailPath);
		shotInfo.setLocalFileName(fileName);
		return shotInfo;
	}

	public ShotInfo setTietukuUrl(ShotInfo shotInfo, String tietukuUrlId, String tietukuUrlPrefix) {
		logging.info("updateTietukuUrl, shotInfoID：" + shotInfo.getId() + ", animeEpisodeId:" + shotInfo.getAnimeEpisodeId() + ", timeStamp:" + shotInfo.getTimeStamp() + ", tietukuUrlId:" + tietukuUrlId + ", tietukuUrlPrefix:" + tietukuUrlPrefix);

		shotInfo.setTietukuUrlPrefix(tietukuUrlPrefix);
		shotInfo.setTietukuUrlId(tietukuUrlId);
		return shotInfo;
	}

	/**
	 * 
	 * 获取站长推荐图片
	 * 
	 * @param pageNumber 页数
	 * @param inDay 几天以内推荐的 null标识全部
	 * @param listCount 每页条数
	 * @param orderByRankFlg 是否根据rank排序
	 */
	public Page<ShotInfo> getMasterRecommandShotPage(int pageNumber, Integer inDay, int listCount, boolean orderByRankFlg) {
		Map<String, Object> searchParams = new LinkedHashMap<>();
		searchParams.put("GT_" + ShotInfo.COLUMN_MASTER_RECOMMEND_RANK, 0);
		if (inDay != null) {
			searchParams.put("GT_" + ShotInfo.COLUMN_MASTER_RECOMMEND_DATE, DateUtil.seekDate(DateUtil.getCurrentDate(), -inDay));
		}

		// 排序，分页条件
		List<Order> orders = new ArrayList<>();
		Order order1 = new Order(Direction.DESC, ShotInfo.COLUMN_MASTER_RECOMMEND_RANK);
		Order order2 = new Order(Direction.DESC, ShotInfo.COLUMN_MASTER_RECOMMEND_DATE);
		if (orderByRankFlg) {
			orders.add(order1);
		}
		orders.add(order2);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, listCount, orders);

		Page<ShotInfo> page = searchPageByParams(searchParams, pageRequest, ShotInfo.class);
		List<ShotInfo> list = page.getContent();
		list = fillParentData(list);
		return page;
	}

	/**
	 * 获取站长推荐图片
	 * 
	 * @param inDay 几天以内推荐的
	 * @return
	 */
	public List<ShotInfo> getMasterRecommandShotList(int pageNumber, Integer inDay, int listCount) {
		// Map<String, Object> searchParams = new LinkedHashMap<>();
		// if (inDay != null) {
		// searchParams.put("GT_" + ShotInfo.COLUMN_MASTER_RECOMMEND_DATE, DateUtil.seekDate(DateUtil.getCurrentDate(), -inDay));
		// }
		// searchParams.put("GT_" + ShotInfo.COLUMN_MASTER_RECOMMEND_RANK, 0);
		//
		// // 排序，分页条件
		// List<Order> orders = new ArrayList<>();
		// Order order1 = new Order(Direction.DESC, ShotInfo.COLUMN_MASTER_RECOMMEND_RANK);
		// Order order2 = new Order(Direction.DESC, ShotInfo.COLUMN_MASTER_RECOMMEND_DATE);
		// orders.add(order1);
		// orders.add(order2);
		// PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, listCount, orders);
		//
		// Page<ShotInfo> page = searchPageByParams(searchParams, pageRequest, ShotInfo.class);
		// List<ShotInfo> list = page.getContent();
		// list = fillParentData(list);
		// return list;

		List<ShotInfo> list = getMasterRecommandShotPage(pageNumber, inDay, listCount, true).getContent();
		return list;
	}

	/**
	 * 获得最新截图
	 * 
	 * @param listCount
	 * @return
	 */
	public List<ShotInfo> getNewestShotList(int listCount) {
		// 检索条件
		Map<String, Object> searchParams = new HashMap<>();

		// 排序，分页条件
		List<Order> orders = new ArrayList<>();
		Order order = new Order(Direction.DESC, ShotInfo.COLUMN_CREATE_DATE);
		orders.add(order);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(1, listCount, orders);

		// 检索
		Page<ShotInfo> page = searchPageByParams(searchParams, pageRequest, ShotInfo.class);

		List<ShotInfo> list = page.getContent();
		list = fillParentData(list);
		return list;
	}

	public List<ShotInfo> getPublicLikeShotList(int listCount) {
		Map<String, Object> searchParams = new HashMap<>();

		List<Order> orders = new ArrayList<>();
		Order order = new Order(Direction.DESC, ShotInfo.COLUMN_PUBLIC_LIKE_COUNT);
		orders.add(order);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(1, listCount, orders);

		Page<ShotInfo> page = searchPageByParams(searchParams, pageRequest, ShotInfo.class);
		List<ShotInfo> list = page.getContent();
		list = fillParentData(list);
		return page.getContent();
	}

	@Override
	public ShotInfo fillParentData(ShotInfo shotInfo) {
		if (shotInfo == null) {
			return shotInfo;
		}
		shotInfo.setAnimeInfo(entityCache.findOne(animeInfoDao, shotInfo.getAnimeInfoId()));
		shotInfo.setAnimeEpisode(entityCache.findOne(animeEpisodeDao, shotInfo.getAnimeEpisodeId()));
		return shotInfo;
	}

	// public Page<ShotInfo> searchAllShots(Map<String, Object> searchParams, int pageNumber, int defaultPageSize, String sortType) {
	//
	// // 创建分页对象
	// PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType);
	//
	// return searchAllShots(searchParams, pageRequest);
	// }

	// public Page<ShotInfo> searchAllShots(Map<String, Object> searchParams, PageRequest pageRequest) {
	//
	// searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");
	//
	// // Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
	// // Specification<ShotInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), ShotInfo.class);
	// Map<String, BaseSearchFilter> filters = BaseSearchFilter.parse(searchParams);
	// Specification<ShotInfo> spec = BaseSpecifications.bySearchFilter(filters.values(), ShotInfo.class);
	// Page<ShotInfo> page = shotInfoDao.findAll(spec, pageRequest);
	//
	// return page;
	// }

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

	/**
	 * 随机获得数据
	 *
	 * @param number
	 * @param animeInfoId 可以为空
	 * @param animeEpisodeId 可以为空
	 * @return
	 */
	public List<ShotInfo> findRandomShot(int number, String animeInfoId, String animeEpisodeId) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put(BaseOperator.EQ.getStr(ShotInfo.COLUMN_ANIME_INFO_ID), animeInfoId);
		searchParams.put(BaseOperator.EQ.getStr(ShotInfo.COLUMN_ANIME_EPISODE_ID), animeEpisodeId);
		List<ShotInfo> list = findRandom(-1, number, ShotInfo.class, searchParams);
		return list;
	}

	public ShotInfo findByTietukuUrlId(String id) {
		List<ShotInfo> list = shotInfoDao.findByTietukuUrlId(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public File getShotFile(String urlImageId) {
		XWaitTime aaaa = new XWaitTime(5111100);
		Function<String, File> fun = (tempId) -> {
			ShotInfo shotInfo = findByTietukuUrlId(tempId);
			logging.info("get shotInfo, " + aaaa.getPastTime() + "");
			if (shotInfo != null) {
				AnimeEpisode animeEpisode = animeEpisodeDao.findOne(shotInfo.getAnimeEpisodeId());
				logging.info("get animeEpisode, " + aaaa.getPastTime() + "");
				AnimeInfo animeInfo = animeInfoDao.findOne(shotInfo.getAnimeInfoId());
				logging.info("get animeInfo, " + aaaa.getPastTime() + "");

				return FilePathUtils.getShotFullFilePath(shotInfo, animeEpisode, animeInfo);
			} else {
				return null;
			}
		};
		File file = entityCache.get("imageId_" + urlImageId, fun, urlImageId, XConst.SECOND_10_MIN);
		return file;
	}
}
