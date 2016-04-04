package xie.base.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.repository.BaseSearchFilter;
import xie.base.repository.BaseSpecifications;
import xie.common.Constants;

public abstract class BaseService<M, ID extends Serializable> {

	protected Logger logging = LoggerFactory.getLogger(this.getClass());

	public abstract BaseRepository<M, ID> getBaseRepository();

	public M save(M t) {
		return getBaseRepository().save(t);
	}

	public M update(M m) {
		return getBaseRepository().save(m);
	}

	public void delete(ID id) {
		getBaseRepository().delete(id);
	}

	public void delete(M m) {
		getBaseRepository().delete(m);
	}

	public M findOne(ID id) {
		return getBaseRepository().findOne(id);
	}

	public M findById(ID id) {
		return getBaseRepository().findById(id);
	}

	public List<M> findAll() {
		return getBaseRepository().findAll();
	}

	public List<M> findAll(Sort sort) {
		return getBaseRepository().findAll(sort);
	}

	public Page<M> findAll(Pageable pageable) {
		return getBaseRepository().findAll(pageable);
	}

	public Page<M> searchAllShots(Map<String, Object> searchParams, int pageNumber, int defaultPageSize, String sortType, Class<M> c) {

		// 创建分页对象
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType);

		Map<String, BaseSearchFilter> filters = BaseSearchFilter.parse(searchParams);
		Specification<M> spec = BaseSpecifications.bySearchFilter(filters.values(), c);
		Page<M> userPage = getBaseRepository().findAll(spec, pageRequest);

		return userPage;
	}

	public Page<M> searchAllShots(Map<String, Object> searchParams, PageRequest pageRequest, Class<M> c) {

		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO + "");

		// Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// Specification<ShotInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), ShotInfo.class);
		Map<String, BaseSearchFilter> filters = BaseSearchFilter.parse(searchParams);
		Specification<M> spec = BaseSpecifications.bySearchFilter(filters.values(), c);
		Page<M> userPage = getBaseRepository().findAll(spec, pageRequest);

		return userPage;
	}
}
