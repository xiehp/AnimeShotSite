package xie.base.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import xie.base.page.PageInfo;

public abstract class BaseRepositoryPlus<T> {
	
	public abstract EntityManager getEntityManager();

	@SuppressWarnings("unchecked")
	public PageImpl<T> getResult(String queryHql, String countHql, Map<String,Object> map, PageInfo page){
		Query queryString = getEntityManager().createQuery(queryHql);
		Query queryCount = getEntityManager().createQuery(countHql);
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			queryString.setParameter(key, map.get(key));
			queryCount.setParameter(key, map.get(key));
		}
		
		queryString.setFirstResult(page.getCurrentPageNumber());
		queryString.setMaxResults(page.getPageSize());
		
		Long count = (Long)queryCount.getSingleResult();
		
		List<T> list = queryString.getResultList();
		
		return new PageImpl<T>(list, new PageRequest(page.getPageNumber(),
				page.getPageSize(), new Sort(page.getSortType(), page.getSortColumn())), count);
	}
	
}
