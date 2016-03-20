package xie.base.page;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import xie.base.entity.BaseEntity;

public class PageRequestUtil {

	/**
	 * 创建分页请求.
	 */
	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType) || "id".equals(sortType)) {
			sort = new Sort(Direction.DESC, BaseEntity.COLUMN_CREATE_DATE);
		} else if (sortType != null) {
			sort = new Sort(Direction.ASC, sortType);
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType, Direction direction) {

		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(direction, BaseEntity.COLUMN_CREATE_DATE);
		} else if (sortType != null) {
			sort = new Sort(direction, sortType);
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, List<Order> orders) {
		Sort sort = new Sort(orders);

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

}
