package xie.animeshotsite.db.entity.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.utils.XWaitTime;

@Component
public class EntityCache {

	private Map<String, Object> cacheMap = new HashMap<String, Object>();
	private Map<String, XWaitTime> timeoutMap = new HashMap<String, XWaitTime>();

	public boolean contain(String id) {
		return cacheMap.containsKey(id);
	}

	public void clear() {
		cacheMap.clear();
		timeoutMap.clear();
	}

	public void put(String cacheId, Object value) {
		cacheMap.put(cacheId, value);
		timeoutMap.put(cacheId, new XWaitTime(60000));
	}

	public <T> T get(String cacheId) {
		XWaitTime xWaitTime = timeoutMap.get(cacheId);
		if (xWaitTime == null) {
			return null;
		}

		if (timeoutMap.get(cacheId).isTimeout()) {
			return null;
		}

		return (T) cacheMap.get(cacheId);

	}

	public <T> T findOne(BaseService<T, String> service, String id) {
		return findOne(service.getBaseRepository(), id);
	}

	public <T> T findOne(BaseRepository<T, String> dao, String id) {
		String cacheId = dao.getClass().getSimpleName() + id;
		System.out.println(cacheId + " nowSize:" + cacheMap.size());
		T value = get(cacheId);
		if (value != null) {
			return value;
		}

		value = dao.findOne(id);
		put(cacheId, value);
		return value;
	}
}
