package xie.animeshotsite.db.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.base.repository.BaseRepository;
import xie.base.repository.BaseSearchFilter.BaseOperator;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.utils.XAssert;

@Service
public class AutoRunParamService extends BaseService<AutoRunParam, String> {

	@Resource
	private AutoRunParamDao autoRunParamDao;

	@Resource
	private AnimeInfoDao animeInfoDao;

	@Resource
	private AnimeEpisodeDao animeEpisodeDao;

	@Override
	public BaseRepository<AutoRunParam, String> getBaseRepository() {
		return autoRunParamDao;
	}

	/**
	 * 找到一个剧集模板数据
	 */
	public AutoRunParam findEpisodeTemplet(String key) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put(BaseOperator.EQ.getStr("type"), AutoRunParam.TYPE_TEMPLET);
		searchParams.put(BaseOperator.EQ.getStr("animeEpisodeId"), AutoRunParam.TEMPLET_EPISODE);
		searchParams.put(BaseOperator.EQ.getStr("key"), key);
		Page<AutoRunParam> page = searchPageByParams(searchParams, 1, Integer.MAX_VALUE, AutoRunParam.COLUMN_SORT, AutoRunParam.class);
		List<AutoRunParam> list = page.getContent();
		XAssert.maxSize(list, 1);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 找到一个动画模板数据
	 */
	public AutoRunParam findAnimeTemplet(String key) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put(BaseOperator.EQ.getStr("type"), AutoRunParam.TYPE_TEMPLET);
		searchParams.put(BaseOperator.EQ.getStr("animeInfoId"), AutoRunParam.TEMPLET_ANIME);
		searchParams.put(BaseOperator.EQ.getStr("key"), key);
		Page<AutoRunParam> page = searchPageByParams(searchParams, null, AutoRunParam.class);
		List<AutoRunParam> list = page.getContent();
		XAssert.maxSize(list, 1);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 找到一个剧集模板数据
	 */
	public AutoRunParam findEpisodeParam(String animeEpisodeId, String key) {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put(BaseOperator.EQ.getStr("animeEpisodeId"), animeEpisodeId);
		searchParams.put(BaseOperator.EQ.getStr("key"), key);
		Page<AutoRunParam> page = searchPageByParams(searchParams, null, AutoRunParam.class);
		List<AutoRunParam> list = page.getContent();

		XAssert.maxSize(list, 1);

		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 新增或者更新一个自动化参数
	 *
	 * @param id id
	 * @param animeInfoId animeInfoId
	 * @param animeEpisodeId animeEpisodeId
	 * @param refId refId
	 * @param refType refType
	 * @param name name
	 * @param key key
	 * @param value value
	 * 
	 * @return 动化参数
	 */
	public AutoRunParam saveOrUpdateOneAutoRunParam(String id, String animeInfoId, String animeEpisodeId, String refId, String refType, String name, String key, String value) {
		AutoRunParam autoRunParam = null;
		if (id != null) {
			autoRunParam = findOne(id);
		}

		if (autoRunParam == null) {
			AutoRunParam keyTemplet = findEpisodeTemplet(key);
			if (keyTemplet != null) {
				autoRunParam = copyFromTemplet(keyTemplet);
			} else {
				autoRunParam = new AutoRunParam();
			}
		}
		autoRunParam.setAnimeInfoId(animeInfoId);
		autoRunParam.setAnimeEpisodeId(animeEpisodeId);
		autoRunParam.setRefId(refId);
		autoRunParam.setRefType(refType);
		autoRunParam.setName(name);
		autoRunParam.setKey(key);
		autoRunParam.setValue(value);

		autoRunParam = save(autoRunParam);
		return autoRunParam;
	}

	public AutoRunParam copyFromTemplet(AutoRunParam keyTemplet) {
		AutoRunParam newAutoRunParam = new AutoRunParam();
		newAutoRunParam.copyFromWithOutBaseInfo(keyTemplet);
		newAutoRunParam.setId(null);
		newAutoRunParam.setType(null);
		newAutoRunParam.setAnimeInfoId(null);
		newAutoRunParam.setAnimeEpisodeId(null);
		return newAutoRunParam;
	}

	public AutoRunParam saveEpisodeByTemplet(String animeEpisodeId, String key, String value) {

		AutoRunParam autoRunParam = findEpisodeParam(animeEpisodeId, key);
		if (autoRunParam == null) {
			AutoRunParam keyTemplet = findEpisodeTemplet(key);
			if (keyTemplet != null) {
				autoRunParam = copyFromTemplet(keyTemplet);
			} else {
				autoRunParam = new AutoRunParam();
			}
			autoRunParam.setAnimeInfoId(animeEpisodeDao.findOne(animeEpisodeId).getAnimeInfoId());
			autoRunParam.setAnimeEpisodeId(animeEpisodeId);
			autoRunParam.setKey(key);
		}

		autoRunParam.setValue(value);
		autoRunParam = save(autoRunParam);
		return autoRunParam;
	}

	private AutoRunParam saveOrUpdateOneAutoRunParam(AutoRunParam autoRunParam) {
		return saveOrUpdateOneAutoRunParam(
				autoRunParam.getId(),
				autoRunParam.getAnimeInfoId(),
				autoRunParam.getAnimeEpisodeId(),
				autoRunParam.getRefId(),
				autoRunParam.getRefType(),
				autoRunParam.getName(),
				autoRunParam.getKey(),
				autoRunParam.getValue());
	}

	/**
	 * 开始监控该动画，<br/>
	 * 将动画的自动化参数video_download_monitor_do_flag设为1，<br/>
	 * 同时所有不存在video_download_monitor_do_flag的剧集也设为1<br/>
	 * 
	 * @param animeInfoId animeInfoId
	 */
	public AutoRunParam beginMonitor(String animeInfoId) {
		Assert.hasText(animeInfoId, "animeInfoId不能为空");

		// 更新动画的标记
		AutoRunParam autoRunParam = autoRunParamDao.findByAnimeInfoIdAndKey(animeInfoId, "video_download_monitor_do_flag");
		if (autoRunParam == null) {
			autoRunParam = new AutoRunParam();
			AutoRunParam autoRunParamTemplet = autoRunParamDao.findByAnimeInfoIdAndKey("anime_templet", "video_download_monitor_do_flag");
			autoRunParam.copyFromWithOutBaseInfo(autoRunParamTemplet);
			autoRunParam.setAnimeInfoId(animeInfoId);
			autoRunParam.setValue(Constants.FLAG_STR_YES);
			autoRunParam = saveOrUpdateOneAutoRunParam(autoRunParam);
		} else {
			if (!Constants.FLAG_STR_YES.equals(autoRunParam.getValue())) {
				autoRunParam.setAnimeInfoId(animeInfoId);
				autoRunParam.setValue(Constants.FLAG_STR_YES);
				autoRunParam = saveOrUpdateOneAutoRunParam(autoRunParam);
			}
		}

		// 循环更新剧集的标记
		List<AnimeEpisode> list = animeEpisodeDao.findByAnimeInfoIdOrderBySort(animeInfoId);
		list.forEach(animeEpisode -> beginEpisodeMonitor(animeEpisode.getAnimeInfoId(), animeEpisode.getId(), true));

		return autoRunParam;
	}

	/**
	 * 开始监控剧集，<br/>
	 * video_download_monitor_do_flag不为1的剧集设为1<br/>
	 *
	 * @param animeEpisodeId animeEpisodeId
	 * @param beginNewOnly 是否只开始还未生成自动化参数的剧集
	 */
	public AutoRunParam beginEpisodeMonitor(String animeInfoId, String animeEpisodeId, boolean beginNewOnly) {
		Assert.hasText(animeEpisodeId, "animeInfoId不能为空");
		Assert.hasText(animeEpisodeId, "animeEpisodeId不能为空");

		AutoRunParam autoRunParam = autoRunParamDao.findByAnimeEpisodeIdAndKey(animeEpisodeId, "video_download_monitor_do_flag");
		if (autoRunParam == null) {
			autoRunParam = new AutoRunParam();
			AutoRunParam autoRunParamTemplet = autoRunParamDao.findByAnimeEpisodeIdAndKey("episode_templet", "video_download_monitor_do_flag");
			autoRunParam.copyFromWithOutBaseInfo(autoRunParamTemplet);
			autoRunParam.setAnimeInfoId(animeInfoId);
			autoRunParam.setAnimeEpisodeId(animeEpisodeId);
			autoRunParam.setValue(Constants.FLAG_STR_YES);
			autoRunParam = saveOrUpdateOneAutoRunParam(autoRunParam);
		} else {
			// Assert.isTrue(!Constants.FLAG_STR_YES.equals(autoRunParam.getValue()), "该剧集已标记为监视中");
			if (!beginNewOnly) {
				if (!Constants.FLAG_STR_YES.equals(autoRunParam.getValue())) {
					autoRunParam.setValue(Constants.FLAG_STR_YES);
					autoRunParam = saveOrUpdateOneAutoRunParam(autoRunParam);
				}
			}

		}

		return autoRunParam;
	}

	/**
	 * 停止监视，不自动下载视频
	 *
	 * @param id
	 */
	public void stopMonitor(String id) {

		Assert.hasText(id, "id不能为空");

		AutoRunParam autoRunParam = autoRunParamDao.findByAnimeInfoIdAndKey(id, "video_download_monitor_do_flag");
		if (autoRunParam == null) {
			autoRunParam = autoRunParamDao.findByAnimeEpisodeIdAndKey(id, "video_download_monitor_do_flag");
		}

		if (autoRunParam != null) {
			autoRunParam.setValue(Constants.FLAG_STR_NO);
			save(autoRunParam);
		}
	}

	/**
	 * 获取自动运行参数， 如果数据不存在，则用模板数据代替
	 * 
	 * @param animeOrEpisodeId animeOrEpisodeId
	 * @param isAnime isAnime
	 * @param isEpisode isEpisode
	 * @return Map
	 */
	public Map<String, AutoRunParam> getStringAutoRunParamMap(String animeOrEpisodeId, boolean isAnime, boolean isEpisode) {
		Map<String, Object> searchParams = new HashMap<>();
		if (isAnime) {
			searchParams.put("EQ_animeInfoId", "anime_templet");
		}
		if (isEpisode) {
			searchParams.put("EQ_animeEpisodeId", "episode_templet");
		}
		Page<AutoRunParam> autoRunParamTempletPage = searchPageByParams(searchParams, 1, Integer.MAX_VALUE, AutoRunParam.COLUMN_SORT, AutoRunParam.class);
		List<AutoRunParam> autoRunParamTempletList = autoRunParamTempletPage.getContent();
		autoRunParamTempletPage.forEach(templetParam -> templetParam.setId(null));
		Map<String, AutoRunParam> templetMap = autoRunParamTempletList
				.stream()
				.collect(Collectors.toMap(AutoRunParam::getKey, (p) -> p));

		if (isAnime) {
			searchParams.put("EQ_animeInfoId", animeOrEpisodeId);
			searchParams.put("ISNULL_animeEpisodeId", 1);
		}
		if (isEpisode) {
			searchParams.put("EQ_animeEpisodeId", animeOrEpisodeId);
		}
		Page<AutoRunParam> autoRunParamPage = searchPageByParams(searchParams, AutoRunParam.class);
		List<AutoRunParam> autoRunParamList = autoRunParamPage.getContent();
		Map<String, AutoRunParam> map = autoRunParamList.stream().collect(Collectors.toMap(AutoRunParam::getKey, (p) -> p));

		templetMap.putAll(map);
		return templetMap;
	}

	/**
	 * 获得监视中的数据
	 */
	public List<AutoRunParam> findMonitorDonloadLUrlist() {

		return autoRunParamDao.findMonitorDonloadLUrlist();

	}

	/**
	 * 获得视频下载地址监视中的动画列表
	 */
	public List<AutoRunParam> findAnimeMonitorDownloadLUrlList() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("ISNULL_type", 1);
		searchParams.put("EQ_key", "video_download_monitor_do_flag");
		searchParams.put("EQ_value", Constants.FLAG_INT_YES);
		searchParams.put("ISNULL_animeEpisodeId", 1);
		Page<AutoRunParam> page = searchPageByParams(searchParams, 1, Integer.MAX_VALUE, AutoRunParam.COLUMN_SORT, AutoRunParam.class);
		List<AutoRunParam> list = page.getContent();
		return list;
	}


	/**
	 * 获得视频下载地址监视中的剧集列表
	 * 
	 * @param animeInfoId animeInfoId
	 */
	public List<AutoRunParam> findEpisodeMonitorDownloadLUrlist(String animeInfoId) {
		Map<String, Object> searchParams = new HashMap<>();
		// searchParams.put("ISNULL_type", 1);
		searchParams.put("EQ_animeInfoId", animeInfoId);
		searchParams.put("EQ_key", "video_download_monitor_do_flag");
		searchParams.put("EQ_value", Constants.FLAG_INT_YES);
		// searchParams.put("ISNOTNULL_animeEpisodeId", 1);
		Page<AutoRunParam> page = searchPageByParams(searchParams, 1, Integer.MAX_VALUE, AutoRunParam.COLUMN_SORT, AutoRunParam.class);
		List<AutoRunParam> list = page.getContent();
		return list;
	}


	/**
	 * 获得等待下载的剧集列表
	 * 
	 * @param animeInfoId animeInfoId
	 */
	public List<AutoRunParam> findEpisodeWaitDownloadList() {
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_key", "video_download_do_download_flag");
		searchParams.put("EQ_value", 0);
		searchParams.put("ISNULL_type", 1);
		searchParams.put("ISNOTNULL_animeEpisodeId", 1);
		Page<AutoRunParam> page = searchPageByParams(searchParams, null, AutoRunParam.class);
		List<AutoRunParam> list = page.getContent();
		return list;
	}
}
