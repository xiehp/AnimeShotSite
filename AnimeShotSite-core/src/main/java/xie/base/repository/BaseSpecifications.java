/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package xie.base.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.utils.Collections3;

import com.google.common.collect.Lists;

/**
 * 转换检索数据
 */
public class BaseSpecifications {

	/**
	 * 将检索数据转换为Specification
	 * 
	 * @param filters 检索数据
	 * @param entityClazz 检索数据的类型
	 * @return Specification
	 */
	public static <T> Specification<T> bySearchFilter(final Collection<BaseSearchFilter> filters, final Class<T> entityClazz) {

		return new Specification<T>() {
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Collections3.isNotEmpty(filters)) {

					List<Predicate> predicates = Lists.newArrayList();
					for (BaseSearchFilter filter : filters) {
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						// logic operator
						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case IN:
							if (filter.value instanceof Collection) {
								Collection<?> collectoin = (Collection<?>) filter.value;
								predicates.add(expression.in(collectoin));
							} else if (filter.value instanceof Object[]) {
								Object[] array = (Object[]) filter.value;
								List<Object> list = Arrays.asList(array);
								predicates.add(expression.in(list));
							} else if (filter.value instanceof String){
								String str = (String) filter.value;
								Object[] array = str.split(str);
								List<Object> list = Arrays.asList(array);
								predicates.add(expression.in(list));
							} else {
								throw new IllegalArgumentException("IN 操作对应的值无法认识，请传入Collection或者Object[]数组或者逗号分隔字符串");
							}
							break;
						}
					}

					if (!predicates.isEmpty()) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}
}
