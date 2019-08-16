package xie.animeshotsite.db.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.GifInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.GifInfoDao;
import xie.animeshotsite.db.vo.GifInfoVO;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.constant.XConst;
import xie.common.date.DateUtil;
import xie.common.number.XNumberUtils;
import xie.common.utils.XSSHttpUtil;
import xie.common.utils.XWaitTime;

@Service
public class GifInfoService extends BaseService<GifInfo, String> {

	@Resource
	private GifInfoDao gifInfoDao;
	@Resource
	private AnimeInfoDao animeInfoDao;
	@Resource
	private AnimeEpisodeDao animeEpisodeDao;
	@Resource
	private EntityCache entityCache;
	@Resource
	private ShotSiteSetup shotSiteSetup;

	public GifInfoVO convertToVO(GifInfo gifInfo) {
		if (gifInfo == null) {
			return null;
		}

		GifInfoVO gifInfoVO = new GifInfoVO();
		BeanMapper.copy(gifInfo, gifInfoVO);

		String url = gifInfoVO.getTietukuOUrl();
		if (url != null) {
			url = XSSHttpUtil.changeTietukuDomain(url, shotSiteSetup.getSiteDomain(), shotSiteSetup.getTietukuChangeDoman());
		}
		gifInfoVO.setTietukuOUrlChangeDomain(url);

		return gifInfoVO;
	}

	@Override
	public BaseRepository<GifInfo, String> getBaseRepository() {
		return gifInfoDao;
	}

	public GifInfo findPrevious(Date createDate) {
		return gifInfoDao.findPrevious(createDate);
	}

	public GifInfo findNext(Date createDate) {
		return gifInfoDao.findNext(createDate);
	}

	/**
	 * 
	 * @param animeInfoId
	 * @param animeEpisodeId
	 * @param timeStamp
	 * @param continueTime
	 * @param rootPath 不需要
	 * @param localDetailPath 不需要
	 * @param fileName
	 * @param forceUpdateFlg 如果已经存在，是否进行更新
	 * @return
	 */
	public GifInfo createGifInfo(String animeInfoId, String animeEpisodeId, long timeStamp, long continueTime, @Deprecated String rootPath, @Deprecated String localDetailPath, String fileName) {

		GifInfo gifInfo = new GifInfo();

		gifInfo.setAnimeInfoId(animeInfoId);
		gifInfo.setAnimeEpisodeId(animeEpisodeId);
		gifInfo.setContinueTime(continueTime);
		gifInfo.setTimeStamp(timeStamp);
		// gifInfo.setLocalRootPath(rootPath);
		// gifInfo.setLocalDetailPath(localDetailPath);
		gifInfo.setLocalFileName(fileName);
		return gifInfo;
	}

	public GifInfo setTietukuUrl(GifInfo gifInfo, String tietukuUrlId, String tietukuUrlPrefix) {
		logging.info("updateTietukuUrl, gifInfoID：" + gifInfo.getId() + ", animeEpisodeId:" + gifInfo.getAnimeEpisodeId() + ", timeStamp:" + gifInfo.getTimeStamp() + ", tietukuUrlId:" + tietukuUrlId + ", tietukuUrlPrefix:" + tietukuUrlPrefix);

		gifInfo.setTietukuUrlPrefix(tietukuUrlPrefix);
		gifInfo.setTietukuUrlId(tietukuUrlId);
		return gifInfo;
	}

	/**
	 * 获取站长推荐图片
	 * 
	 * @param inDay 几天以内推荐的
	 * @return
	 */
	public Page<GifInfo> getMasterRecommandShotPage(int pageNumber, Integer inDay, int listCount) {
		Map<String, Object> searchParams = new LinkedHashMap<>();
		if (inDay != null) {
			searchParams.put("GT_" + GifInfo.COLUMN_MASTER_RECOMMEND_DATE, DateUtil.seekDate(DateUtil.getCurrentDate(), -inDay));
		}
		searchParams.put("GT_" + GifInfo.COLUMN_MASTER_RECOMMEND_RANK, 0);

		// 排序，分页条件
		List<Order> orders = new ArrayList<>();
		Order order1 = new Order(Direction.DESC, GifInfo.COLUMN_MASTER_RECOMMEND_RANK);
		Order order2 = new Order(Direction.DESC, GifInfo.COLUMN_MASTER_RECOMMEND_DATE);
		orders.add(order1);
		orders.add(order2);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, listCount, orders);

		Page<GifInfo> page = searchPageByParams(searchParams, pageRequest, GifInfo.class);
		List<GifInfo> list = page.getContent();
		list = fillParentData(list);
		return page;
	}

	/**
	 * 获取站长推荐图片
	 * 
	 * @param inDay 几天以内推荐的
	 * @return
	 */
	public List<GifInfo> getMasterRecommandShotList(int pageNumber, Integer inDay, int listCount) {
		// Map<String, Object> searchParams = new LinkedHashMap<>();
		// if (inDay != null) {
		// searchParams.put("GT_" + GifInfo.COLUMN_MASTER_RECOMMEND_DATE, DateUtil.seekDate(DateUtil.getCurrentDate(), -inDay));
		// }
		// searchParams.put("GT_" + GifInfo.COLUMN_MASTER_RECOMMEND_RANK, 0);
		//
		// // 排序，分页条件
		// List<Order> orders = new ArrayList<>();
		// Order order1 = new Order(Direction.DESC, GifInfo.COLUMN_MASTER_RECOMMEND_RANK);
		// Order order2 = new Order(Direction.DESC, GifInfo.COLUMN_MASTER_RECOMMEND_DATE);
		// orders.add(order1);
		// orders.add(order2);
		// PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, listCount, orders);
		//
		// Page<GifInfo> page = searchPageByParams(searchParams, pageRequest, GifInfo.class);
		// List<GifInfo> list = page.getContent();
		// list = fillParentData(list);
		// return list;

		List<GifInfo> list = getMasterRecommandShotPage(pageNumber, inDay, listCount).getContent();
		return list;
	}

	/**
	 * 获得最新截图
	 * 
	 * @param listCount
	 * @return
	 */
	public List<GifInfo> getNewestShotList(int listCount) {
		// 检索条件
		Map<String, Object> searchParams = new HashMap<>();

		// 排序，分页条件
		List<Order> orders = new ArrayList<>();
		Order order = new Order(Direction.DESC, GifInfo.COLUMN_CREATE_DATE);
		orders.add(order);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(1, listCount, orders);

		// 检索
		Page<GifInfo> page = searchPageByParams(searchParams, pageRequest, GifInfo.class);

		List<GifInfo> list = page.getContent();
		list = fillParentData(list);
		return list;
	}

	public List<GifInfo> getPublicLikeShotList(int listCount) {
		Map<String, Object> searchParams = new HashMap<>();

		List<Order> orders = new ArrayList<>();
		Order order = new Order(Direction.DESC, GifInfo.COLUMN_PUBLIC_LIKE_COUNT);
		orders.add(order);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(1, listCount, orders);

		Page<GifInfo> page = searchPageByParams(searchParams, pageRequest, GifInfo.class);
		List<GifInfo> list = page.getContent();
		list = fillParentData(list);
		return page.getContent();
	}

	public GifInfo fillParentData(GifInfo gifInfo) {
		if (gifInfo == null) {
			return gifInfo;
		}
		gifInfo.setAnimeInfo(entityCache.findOne(animeInfoDao, gifInfo.getAnimeInfoId()));
		gifInfo.setAnimeEpisode(entityCache.findOne(animeEpisodeDao, gifInfo.getAnimeEpisodeId()));
		return gifInfo;
	}

	// public Page<GifInfo> searchAllShots(Map<String, Object> searchParams, int pageNumber, int defaultPageSize, String sortType) {
	//
	// // 创建分页对象
	// PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType);
	//
	// return searchAllShots(searchParams, pageRequest);
	// }

	// public Page<GifInfo> searchAllShots(Map<String, Object> searchParams, PageRequest pageRequest) {
	//
	// searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");
	//
	// // Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
	// // Specification<GifInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GifInfo.class);
	// Map<String, BaseSearchFilter> filters = BaseSearchFilter.parse(searchParams);
	// Specification<GifInfo> spec = BaseSpecifications.bySearchFilter(filters.values(), GifInfo.class);
	// Page<GifInfo> page = gifInfoDao.findAll(spec, pageRequest);
	//
	// return page;
	// }

	public GifInfo publicLikeAdd(String id) {
		GifInfo gifInfo = null;
		synchronized (this) {
			gifInfo = findOne(id);
			if (gifInfo != null) {
				gifInfo.setPublicLikeCount(XNumberUtils.add(gifInfo.getPublicLikeCount(), 1L));
				gifInfo = gifInfoDao.save(gifInfo);
			}
		}
		return gifInfo;
	}

	public GifInfo masterLikeAdd(String id) {
		GifInfo gifInfo = null;
		synchronized (this) {
			gifInfo = findOne(id);
			if (gifInfo != null) {
				gifInfo.setPublicLikeCount(XNumberUtils.add(gifInfo.getPublicLikeCount(), 1L));
				gifInfo.setMasterRecommendRank(XNumberUtils.add(gifInfo.getMasterRecommendRank(), 1L));
				gifInfo.setMasterRecommendDate(DateUtil.getCurrentDate());
				gifInfo = gifInfoDao.save(gifInfo);
			}
		}
		return gifInfo;
	}

	public List<GifInfo> findRandom(int range, int number) {
		int from = RandomUtils.nextInt(1, range);
		List<GifInfo> list = gifInfoDao.findRandom(from, number);
		list = fillParentData(list);
		return list;
	}

	public GifInfo findGifByTietukuUrlId(String id) {
		List<GifInfo> list = gifInfoDao.findByTietukuUrlId(id);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public File getGifFile(String urlImageId) {
		XWaitTime aaaa = new XWaitTime(5111100);
		Function<String, File> fun = (tempId) -> {
			GifInfo gifInfo = findGifByTietukuUrlId(tempId);
			logging.info("get gifInfo, " + aaaa.getPastTime() + "");
			if (gifInfo != null) {
				AnimeEpisode animeEpisode = animeEpisodeDao.findOne(gifInfo.getAnimeEpisodeId());
				logging.info("get animeEpisode, " + aaaa.getPastTime() + "");
				AnimeInfo animeInfo = animeInfoDao.findOne(gifInfo.getAnimeInfoId());
				logging.info("get animeInfo, " + aaaa.getPastTime() + "");

				String path = FilePathUtils.getShotFullFilePath(gifInfo.copyTo(new ShotInfo()), animeEpisode, animeInfo).getAbsolutePath();
				path = path.replace(ShotCoreConstants.LOCAL_ROOT_SHOT_PATH, ShotCoreConstants.LOCAL_ROOT_GIF_PATH);
				return new File(path);
			} else {
				return null;
			}
		};
		File file = entityCache.get("imageId_" + urlImageId, fun, urlImageId, XConst.SECOND_10_MIN);
		return file;
	}
}
