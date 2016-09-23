package xie.animeshotsite.db.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springside.modules.mapper.BeanMapper;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.string.XStringUtils;

@Service
public class AnimeEpisodeService extends BaseService<AnimeEpisode, String> {

	@Autowired
	private AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	private AnimeInfoDao animeInfoDao;
	@Autowired
	private ImageUrlService imageUrlService;
	@Autowired
	private EntityCache entityCache;

	@Override
	public BaseRepository<AnimeEpisode, String> getBaseRepository() {
		return animeEpisodeDao;
	}

	/** 获取所有需要进行截图的动画列表 */
	public List<AnimeEpisode> findByProcessAction(Integer processAction) {
		return animeEpisodeDao.findByProcessAction(processAction);
	}

	/** 获取某动画下所有剧集 */
	public List<AnimeEpisode> findByAnimeInfoId(String animeInfoId) {
		return animeEpisodeDao.findByAnimeInfoIdOrderBySort(animeInfoId);
	}

	/**
	 * 获得最新截图
	 * 
	 * @param listCount
	 * @return
	 */
	public List<AnimeEpisode> getNewestAnimeEpisodeList(int listCount) {
		// 检索条件
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_showFlg", Constants.FLAG_INT_YES);

		// 排序条件
		List<Order> orders = new ArrayList<>();
		Order order = new Order(Direction.DESC, "showDate");
		orders.add(order);
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(1, listCount, orders);

		// 检索
		Page<AnimeEpisode> page = searchPageByParams(searchParams, pageRequest, AnimeEpisode.class);
		List<AnimeEpisode> list = page.getContent();
		for (AnimeEpisode animeEpisode : list) {
			animeEpisode.setAnimeInfo(animeInfoDao.findOne(animeEpisode.getAnimeInfoId()));
		}
		return list;
	}

	public String saveMuti(String param, Integer start, Integer end, Integer extention,
			Map<String, Object> requestMap) {
		String[] paramArray = new String[0];
		if (param != null && param.length() > 0) {
			paramArray = param.split(",");
		}

		if (extention == null || extention < 1) {
			extention = String.valueOf(end).length();
		}

		String pattern = "";
		for (int i = 0; i < extention; i++) {
			pattern += "0";
		}

		// 获得待删除剧集数据
		List<AnimeEpisode> list = findByAnimeInfoId((String) requestMap.get("animeInfoId"));

		// 新建数据
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String firstId = null;
		for (int i = start; i < end + 1; i++) {
			AnimeEpisode AnimeEpisode = new AnimeEpisode();
			// Map<String, Object> newMap = new HashMap<String, Object>();
			// BeanMapper.copy(source, destinationObject);

			String[] formatedParamArray = new String[paramArray.length + 1];
			formatedParamArray[0] = decimalFormat.format(i);
			for (int k = 1; k < formatedParamArray.length; k++) {
				formatedParamArray[k] = paramArray[k - 1];
			}

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.putAll(requestMap);
			XStringUtils.formatStr(paramMap, formatedParamArray);
			BeanMapper.copy(paramMap, AnimeEpisode);
			AnimeEpisode.setId(null);
			AnimeEpisode newAnimeEpisode = save(AnimeEpisode);

			if (firstId == null) {
				firstId = newAnimeEpisode.getId();
			}
		}

		// 删除所有现存剧集数据
		for (AnimeEpisode animeEpisode : list) {
			// delete(animeEpisode);
			// TODO
		}

		return firstId;
	}

	/**
	 * 新增加一个图片title
	 */
	public void saveTitleUrl(AnimeEpisode animeEpisode, String rootPath, String detailPath, String name, String tietukuImageUrlId, String tietukuImageUrlPrefix) {
		ImageUrl imageUrl = imageUrlService.saveImageInfo(rootPath, detailPath, name, tietukuImageUrlId, tietukuImageUrlPrefix);
		animeEpisode.setTitleUrlId(imageUrl.getId());
		animeEpisodeDao.save(animeEpisode);
	}
}
