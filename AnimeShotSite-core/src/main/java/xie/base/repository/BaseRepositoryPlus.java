package xie.base.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public abstract class BaseRepositoryPlus<T> {

	public abstract EntityManager getEntityManager();

	@SuppressWarnings("unchecked")
	public PageImpl<T> getResult(String queryHql, String countHql, Map<String, Object> map, PageRequest pageRequest) {
		Query queryString = getEntityManager().createQuery(queryHql);
		Query queryCount = getEntityManager().createQuery(countHql);

		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			queryString.setParameter(key, map.get(key));
			queryCount.setParameter(key, map.get(key));
		}

		int first = pageRequest.getPageNumber() * pageRequest.getPageSize();
		queryString.setFirstResult(first);
		queryString.setMaxResults(pageRequest.getPageSize());

		Long count = (Long) queryCount.getSingleResult();
		List<T> list = queryString.getResultList();

		return new PageImpl<T>(list, pageRequest, count);
	}

	@SuppressWarnings("unchecked")
	public PageImpl<String> geIdResult(String queryHql, String countHql, Map<String, Object> map, PageRequest pageRequest) {
		Query queryString = getEntityManager().createQuery(queryHql);
		Query queryCount = getEntityManager().createQuery(countHql);

		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			queryString.setParameter(key, map.get(key));
			queryCount.setParameter(key, map.get(key));
		}

		int first = pageRequest.getPageNumber() * pageRequest.getPageSize();
		queryString.setFirstResult(first);
		queryString.setMaxResults(pageRequest.getPageSize());

		Long count = (Long) queryCount.getSingleResult();
		List<String> list = queryString.getResultList();

		return new PageImpl<String>(list, pageRequest, count);
	}

	public PageImpl<T> createEmptyPage(PageRequest pageRequest) {
		List<T> list = new ArrayList<>();
		return new PageImpl<T>(list, pageRequest, 0);
	}
}
