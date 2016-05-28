package xie.base.service;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.mapper.BeanMapper;

import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.repository.BaseSearchFilter;
import xie.base.repository.BaseSpecifications;
import xie.common.Constants;
import xie.common.string.XStringUtils;

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

	public Page<M> searchPageByParams(Map<String, Object> searchParams, int pageNumber, int defaultPageSize, String sortType, Class<M> c) {
		// 创建分页对象
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType);

		Page<M> userPage = searchPageByParams(searchParams, pageRequest, c);

		return userPage;
	}

	public Page<M> searchPageByParams(Map<String, Object> searchParams, PageRequest pageRequest, Class<M> c) {

		searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO);

		// Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// Specification<ShotInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), ShotInfo.class);
		Map<String, BaseSearchFilter> filters = BaseSearchFilter.parse(searchParams);
		Specification<M> spec = BaseSpecifications.bySearchFilter(filters.values(), c);
		Page<M> userPage = getBaseRepository().findAll(spec, pageRequest);

		return userPage;
	}

	public M saveMuti(String param, Integer start, Integer end, Integer extention,
			Map<String, Object> requestMap, Class<M> entityClass) throws InstantiationException, IllegalAccessException {
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

		// 新建数据
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		M firstEntity = null;
		for (int i = start; i < end + 1; i++) {
			M entity = entityClass.newInstance();
			String[] formatedParamArray = new String[paramArray.length + 1];
			formatedParamArray[0] = decimalFormat.format(i);
			for (int k = 1; k < formatedParamArray.length; k++) {
				formatedParamArray[k] = paramArray[k - 1];
			}

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.putAll(requestMap);
			XStringUtils.formatStr(paramMap, formatedParamArray);
			BeanMapper.copy(paramMap, entity);
			M newEntity = save(entity);

			if (firstEntity == null) {
				firstEntity = newEntity;
			}
		}

		return firstEntity;
	}
}
