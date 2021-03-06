package xie.animeshotsite.db.entity.cache;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.GifInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.repository.GifInfoDao;
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

	public static final String CACHE_ID_Previous_GifInfo = "Previous_GifInfo_";
	public static final String CACHE_ID_Next_GifInfo = "Next_GifInfo_";

	@Resource
	private ShotInfoDao shotInfoDao;
	@Resource
	private GifInfoDao gifInfoDao;

	private Map<String, Object> cacheMap = new HashMap<String, Object>();
	private Map<String, XWaitTime> timeoutMap = new LinkedHashMap<String, XWaitTime>();

	/** 每过一段时间清理缓存的过期时间 */
	XWaitTime processExpireTime = new XWaitTime(600000);

	/**
	 * 判断是否存在缓存
	 * 
	 * @param cacheId
	 * @return
	 */
	public boolean contain(String cacheId) {
		if (get(cacheId) == null) {
			// 返回null有两种情况
			// 1.可能是缓存过期
			// 2.数据本身为null
			// 3.数据不存在
			// 因此null的情况，先get后可以清除缓存，此时才能判断是否存在cacheId
			return cacheMap.containsKey(cacheId);
		} else {
			return true;
		}
	}

	public synchronized int clear() {
		int size = cacheMap.size();
		logger.info("准备清除所有缓存，当前缓存个数：" + size);
		cacheMap.clear();
		timeoutMap.clear();
		return size;
	}

	/**
	 * 清除以字符串开头的缓存
	 * 
	 * @param beginStr
	 * @return
	 */
	public int clearBegin(String beginStr) {
		synchronized (this) {
			logger.info("准备清除以{}开头的缓存，当前缓存个数：{}", beginStr, cacheMap.size());

			// 处理缓存
			int count = 0;
			Iterator<Entry<String, Object>> entryIt = cacheMap.entrySet().iterator();
			while (entryIt.hasNext()) {
				Entry<String, Object> entry = entryIt.next();
				if (entry.getKey().startsWith(beginStr)) {
					count++;
					entryIt.remove();
					String cacheId = entry.getKey();
					timeoutMap.remove(cacheId);
				}
			}

			logger.info("清除缓存个数:{}, 剩余缓存个数:{}", count, cacheMap.size());

			return count;
		}
	}

	private int clearExpire() {
		if (!processExpireTime.isTimeout()) {
			return 0;
		}

		synchronized (this) {
			if (!processExpireTime.isTimeout()) {
				return 0;
			}

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

			logger.info("清除缓存个数:{}, 剩余缓存个数:{}", count, cacheMap.size());

			if (cacheMap.size() > 5000) {
				logger.info("缓存个数大于5000，继续清理");
				timeoutMap.keySet().forEach(key -> {
					if (cacheMap.size() > 4000) {
						remove(key);
					}
				});
			}

			logger.info("剩余缓存个数:{}", cacheMap.size());

			processExpireTime.resetNowtime();
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
		clearExpire();

		remove(cacheId);
		cacheMap.put(cacheId, value);
		timeoutMap.put(cacheId, new XWaitTime(timeoutMili));
	}

	public Object remove(String cacheId) {
		synchronized (this) {
			timeoutMap.remove(cacheId);
			Object object = cacheMap.remove(cacheId);
			return object;
		}
	}

	/**
	 * 根据传入的cacheId获取缓存，如果缓存不存在，则调用回调函数
	 * 
	 * @param cacheId
	 * @param fun
	 * @return
	 */
	public <RR> RR get(String cacheId, Supplier<RR> fun) {
		return get(cacheId, fun, 60000);
	}

	/**
	 * 根据传入的cacheId获取缓存，如果缓存不存在，则调用回调函数
	 * 
	 * @param cacheId cacheId
	 * @param fun 回调函数
	 * @param timeoutMili 微妙
	 * @return
	 */
	public <RR> RR get(String cacheId, Supplier<RR> fun, long timeoutMili) {
		if (contain(cacheId)) {
			return get(cacheId);
		} else {
			RR rr = fun.get();
			put(cacheId, rr, timeoutMili);
			return rr;
		}
	}

	public <T, R> R get(String cacheId, Function<T, R> fun, T funParam, long timeoutMili) {
		if (contain(cacheId)) {
			return get(cacheId);
		} else {
			R r = fun.apply(funParam);
			put(cacheId, r, timeoutMili);
			return r;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String cacheId) {
		XWaitTime xWaitTime = timeoutMap.get(cacheId);
		if (xWaitTime == null) {
			return null;
		}

		if (xWaitTime.isTimeout()) {
			remove(cacheId); // 这里不清楚掉，则contain函数中将无法正确判断缓存虽然存在，但是实际已经过期这种情况
			return null;
		}

		return (T) cacheMap.get(cacheId);
	}

	/**
	 * 多个字符串组合成一个cacheId
	 * 
	 * @param cacheIds 多个字符串组合成一个cacheId
	 * @return
	 */
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

	public GifInfo findPreviousGifInfo(Date createDate) {
		String id = CACHE_ID_Previous_GifInfo + createDate.getTime();
		GifInfo gifInfo = get(id);
		if (gifInfo == null) {
			gifInfo = gifInfoDao.findPrevious(createDate);
			put(id, gifInfo);
		}

		return gifInfo;
	}

	public GifInfo findNextGifInfo(Date createDate) {
		String id = CACHE_ID_Next_GifInfo + createDate.getTime();
		GifInfo gifInfo = get(id);
		if (gifInfo == null) {
			gifInfo = gifInfoDao.findNext(createDate);
			put(id, gifInfo);
		}

		return gifInfo;
	}
}
