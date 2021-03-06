package xie.base.service;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.mapper.BeanMapper;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.base.entity.BaseEntity;
import xie.base.entity.IdEntity;
import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepository;
import xie.base.repository.BaseSearchFilter;
import xie.base.repository.BaseSearchParams;
import xie.base.repository.BaseSpecifications;
import xie.common.Constants;
import xie.common.string.XStringUtils;

public abstract class BaseService<M extends IdEntity, ID extends Serializable> {

	protected Logger logging = LoggerFactory.getLogger(this.getClass());

	@Resource
	protected EntityCache entityCache;

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

	public M findOneCache(String id) {
		return entityCache.findOne((BaseRepository<M, String>)getBaseRepository(), id.toString());
	}

	public M findOne(ID id, boolean useCache) {
		if (useCache) {
			return entityCache.findOne((BaseRepository<M, String>)getBaseRepository(), id.toString());
		} else {
			return getBaseRepository().findOne(id);
		}
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

	public Page<M> searchPageByParams(Map<String, Object> searchParams, Class<M> c) {
		return searchPageByParams(searchParams, null, c);
	}

	/**
	 * 分页检索，根据检索条件，搜索数据库，返回对应分页数据
	 * 
	 * @param searchParams 检索条件, EQ_Param1=value1,GT_Param2=value2的特殊格式
	 * @param pageNumber 页数从1开始
	 * @param defaultPageSize 每页显示条数
	 * @param sortType 排序条件，用于单条件排序
	 * @param c 返回的数据类型
	 * @return 分页数据
	 */
	public Page<M> searchPageByParams(Map<String, Object> searchParams, int pageNumber, int defaultPageSize, String sortType, Class<M> c) {
		return searchPageByParams(searchParams, pageNumber, defaultPageSize, sortType, null, c);
	}

	/**
	 * 分页检索，根据检索条件，搜索数据库，返回对应分页数据
	 * 
	 * @param searchParams 检索条件, EQ_Param1=value1,GT_Param2=value2的特殊格式
	 * @param pageNumber 页数从1开始
	 * @param defaultPageSize 每页显示条数
	 * @param sortType 排序条件，用于单条件排序
	 * @param c 返回的数据类型
	 * @return 分页数据
	 */
	public Page<M> searchPageByParams(Map<String, Object> searchParams, int pageNumber, int defaultPageSize, String sortType, Direction direction, Class<M> c) {
		if (searchParams == null) {
			searchParams = new HashMap<>();
		}

		if (pageNumber < 1) {
			pageNumber = 1;
		}

		// 创建分页对象
		PageRequest pageRequest = PageRequestUtil.buildPageRequest(pageNumber, defaultPageSize, sortType, direction);

		// 检索
		Page<M> page = searchPageByParams(searchParams, pageRequest, c);

		// 页数不对， 并且有数据，直接定位到最后一页
		if (pageNumber > page.getTotalPages() && page.getTotalPages() > 0) {
			return searchPageByParams(searchParams, page.getTotalPages(), defaultPageSize, sortType, direction, c);
		}

		return page;
	}

	/**
	 * 分页检索，根据检索条件，搜索数据库，返回对应分页数据
	 * 
	 * @param searchParams 检索条件, EQ_Param1=value1,GT_Param2=value2的特殊格式
	 * @param pageRequest 含有页数，每页条数，排序方式的数据
	 * @param c 返回的数据类型
	 * @return 分页数据
	 */
	public Page<M> searchPageByParams(Map<String, Object> searchParams, PageRequest pageRequest, Class<M> c) {

		if (searchParams == null) {
			searchParams = new LinkedHashMap<>();
		}
		if (!searchParams.containsKey("EQ_" + BaseEntity.COLUMN_DELETE_FLAG)) {
			searchParams.put("EQ_" + BaseEntity.COLUMN_DELETE_FLAG, Constants.FLAG_INT_NO);
		}

		// Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// Specification<ShotInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), ShotInfo.class);
		Map<String, BaseSearchFilter> filters = BaseSearchFilter.parse(searchParams);
		Specification<M> spec = BaseSpecifications.bySearchFilter(filters.values(), c);
		Page<M> page = null;
		if (pageRequest == null) {
			List<M> list = getBaseRepository().findAll(spec);
			page = new PageImpl<>(list);
		} else {
			page = getBaseRepository().findAll(spec, pageRequest);
		}

		return page;
	}

	/**
	 * 根据特定的格式同时保存多条数据
	 * 
	 * @param param 其他参数填充值，顺序必须固定
	 * @param start 数字填充值开始值
	 * @param end 数字填充值结束值
	 * @param extention 数字填充值扩展的位数
	 * @param requestMap 前台提交的数据，其中如果需要进行填充，使用[[0-N]]作为标记，[[0]]表示使用数字填充值进行填充， [[1-N]]为使用其他参数填充值
	 * @param entityClass 生成的实体类
	 * @return 首个生成的实体数据
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
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

			// 根据原始参数生成本条数据的特定参数
			String[] formatedParamArray = new String[paramArray.length + 1];
			formatedParamArray[0] = decimalFormat.format(i); // 第一个参数为固定参数，为start到end的数字
			for (int k = 1; k < formatedParamArray.length; k++) {
				formatedParamArray[k] = paramArray[k - 1];
			}

			// 使用特定参数格式化每个字段
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.putAll(requestMap);
			XStringUtils.formatStr(paramMap, formatedParamArray);

			// 将生成好的参数拷贝到entity中，保存到数据库
			BeanMapper.copy(paramMap, entity);
			M newEntity = save(entity);

			if (firstEntity == null) {
				firstEntity = newEntity;
			}
		}

		return firstEntity;
	}

	/**
	 * 填充Entity的扩展数据
	 */
	public List<M> fillParentData(List<M> list) {
		if (list == null || list.size() == 0) {
			return list;
		}
		for (M entity : list) {
			fillParentData(entity);
		}
		return list;
	}

	/**
	 * 填充Entity的扩展数据
	 */
	public M fillParentData(M entity) {
		return entity;
	}

	/**
	 * 随机获得数据
	 * 
	 * @param range 范围，-1或小于0时，自动获得最大值
	 * @param number
	 * @param addSearchParams EQ_COLUMN,Value格式的搜索条件，可以为null
	 * @return
	 */
	public List<M> findRandom(int range, int number, Class<M> clazz, Map<String, Object> addSearchParams) {
		if (range == 0 || number == 0 || clazz == null) {
			logging.warn("错误的参数，range:{}，number:{}，clazz:{}", range, number, clazz);
			return new ArrayList<>();
		}

		// 搜索条件
		BaseSearchParams baseSearchParams = new BaseSearchParams();
		baseSearchParams.EQ(BaseEntity.COLUMN_DELETE_FLAG, Constants.FLAG_INT_NO);
		Map<String, Object >searchParams = baseSearchParams.getParams();
		searchParams.putAll(addSearchParams);

		// 获得开始点
		int from = 0;
		if (range < 0) {
			Page<M> page = searchPageByParams(searchParams, 1, 1, null, clazz);
			int totalElement = (int) page.getTotalElements();
			if (totalElement > 0) {
				from = RandomUtils.nextInt(1, totalElement) + 1;
			} else {
				logging.warn("搜出数据总数为0，range:{}，number:{}，clazz:{}，参数：{}", range, number, clazz, searchParams);
				return new ArrayList<>();
			}
		} else {
			from = RandomUtils.nextInt(1, range) + 1;
		}

		Page<M> animeEpisodePage = searchPageByParams(searchParams, from, number, null, clazz);
		List<M> list = animeEpisodePage.getContent();
		list = fillParentData(list);

		if (list.size() == 0) {
			logging.warn("搜出0条数据，from:{}，number:{}，clazz:{}，参数：{}", from, number, clazz, searchParams);
		}
		return list;
	}
}
