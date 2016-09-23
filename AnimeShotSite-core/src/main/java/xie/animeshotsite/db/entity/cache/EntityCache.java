package xie.animeshotsite.db.entity.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.base.entity.IdEntity;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.utils.XWaitTime;

@Component
public class EntityCache {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String CACHE_ID_Previous_ShotInfo = "Previous_ShotInfo_";
	public static final String CACHE_ID_Next_ShotInfo = "Next_ShotInfo_";

	@Autowired
	private ShotInfoDao shotInfoDao;

	private Map<String, Object> cacheMap = new HashMap<String, Object>();
	private Map<String, XWaitTime> timeoutMap = new LinkedHashMap<String, XWaitTime>();

	XWaitTime processExpireTime = new XWaitTime(600000);

	public boolean contain(String id) {
		return cacheMap.containsKey(id);
	}

	public int clear() {
		int size = cacheMap.size();
		logger.info("准备清除所有缓存，当前缓存个数：" + size);
		cacheMap.clear();
		timeoutMap.clear();
		return size;
	}

	private int clearExpire() {
		synchronized (processExpireTime) {
			logger.info("准备清除过期缓存，当前缓存个数：{}", cacheMap.size());

			// 处理过期对象
			int count = 0;
			Iterator<Entry<String, XWaitTime>> entryIt = timeoutMap.entrySet().iterator();
			while (entryIt.hasNext()) {
				Entry<String, XWaitTime> entry = entryIt.next();
				if (entry.getValue().isTimeout()) {
					count++;
					entryIt.remove();
					String cacheId = entry.getKey();
					cacheMap.remove(cacheId);
				} else {
					break;
				}
			}
			processExpireTime.resetNowtime();

			logger.info("清除缓存个数:{}, 剩余缓存个数:{}", count, cacheMap.size());

			return count;
		}
	}

	public void put(String cacheId, Object value) {
		put(cacheId, value, 60000);
	}

	/**
	 * 
	 * @param cacheId
	 * @param value
	 * @param timeoutMili 微妙
	 */
	public void put(String cacheId, Object value, long timeoutMili) {
		remove(cacheId);
		cacheMap.put(cacheId, value);
		timeoutMap.put(cacheId, new XWaitTime(timeoutMili));

		if (processExpireTime.isTimeout()) {
			clearExpire();

			if (processExpireTime.isTimeout()) {
				processExpireTime.resetNowtime();
			}
		}
	}

	public Object remove(String cacheId) {
		synchronized (processExpireTime) {
			timeoutMap.remove(cacheId);
			Object object = cacheMap.remove(cacheId);
			return object;
		}
	}

	public <T> T get(String cacheId) {
		XWaitTime xWaitTime = timeoutMap.get(cacheId);
		if (xWaitTime == null) {
			return null;
		}

		if (timeoutMap.get(cacheId).isTimeout()) {
			remove(cacheId);
			return null;
		}

		return (T) cacheMap.get(cacheId);

	}

	public <T> T get(String... cacheIds) {
		String cacheId = "";
		for (int i = 0; i < cacheIds.length; i++) {
			cacheId += cacheIds[i];
		}

		return get(cacheId);

	}

	public <T extends IdEntity> T findOne(BaseService<T, String> service, String id) {
		return findOne(service.getBaseRepository(), id);
	}

	public <T> T findOne(BaseRepository<T, String> dao, String id) {
		String cacheId = dao.getClass().getSimpleName() + id;
		// System.out.println(cacheId + " nowSize:" + cacheMap.size());
		T value = get(cacheId);
		if (value != null) {
			return value;
		}

		value = dao.findOne(id);
		put(cacheId, value);
		return value;
	}

	public ShotInfo findPreviousShotInfo(String animeEpisodeId, long timeStamp) {
		String id = CACHE_ID_Previous_ShotInfo + animeEpisodeId + "_" + timeStamp;
		ShotInfo shotInfo = get(id);
		if (shotInfo == null) {
			shotInfo = shotInfoDao.findPrevious(animeEpisodeId, timeStamp);
			put(id, shotInfo);
		}

		return shotInfo;
	}

	public ShotInfo findNextShotInfo(String animeEpisodeId, long timeStamp) {
		ShotInfo shotInfo = get(CACHE_ID_Next_ShotInfo + animeEpisodeId + "_" + timeStamp);
		if (shotInfo == null) {
			shotInfo = shotInfoDao.findNext(animeEpisodeId, timeStamp);
			put(CACHE_ID_Next_ShotInfo + animeEpisodeId + "_" + timeStamp, shotInfo);
		}

		return shotInfo;
	}
}
