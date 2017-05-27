package xie.base.controller;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.base.entity.IdEntity;
import xie.base.module.ajax.vo.GoPageResult;
import xie.base.service.BaseService;
import xie.common.java.XReflectionUtils;
import xie.common.string.XStringUtils;

public abstract class BaseManagerController<M extends IdEntity, ID extends Serializable> extends BaseController {

	protected abstract BaseService<M, ID> getBaseService();

	@Resource
	protected AutoRunParamService autoRunParamService;

	@Resource
	protected AutoRunParamDao autoRunParamDao;

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/updateOneColumn")
	@ResponseBody
	public GoPageResult updateOneColumn(@RequestParam ID id, @RequestParam String columnName, @RequestParam String columnValue, HttpServletRequest request) {

		GoPageResult goPageResult = null;
		try {
			M baseEntity = getBaseService().findOne(id);

			String methodName = "set" + XStringUtils.upperFirstLetter(columnName);

			if (XStringUtils.isEmpty(columnValue)) {
				columnValue = null;
			}
			invokeMethod(baseEntity, methodName, columnValue);

			baseEntity = getBaseService().save(baseEntity);

			goPageResult = goPageUtil.createSuccess(request);

		} catch (Exception e) {
			_log.error("updateOneColumn发生错误", e);
			goPageResult = goPageUtil.createFail(request);
		}

		return goPageResult;
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/updateOneAutoRunParam")
	@ResponseBody
	public GoPageResult updateOneAutoRunParam(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String animeInfoId,
			@RequestParam(required = false) String animeEpisodeId,
			@RequestParam(required = false) String refId,
			@RequestParam(required = false) String refType,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String key,
			@RequestParam(required = false) String value,
			HttpServletRequest request) {

		GoPageResult goPageResult = null;
		try {
			AutoRunParam autoRunParam = null;
			if (id != null) {
				autoRunParam = autoRunParamService.findOne(id);
			}

			if (autoRunParam == null) {
				autoRunParam = new AutoRunParam();
				autoRunParam.setAnimeInfoId(animeInfoId);
				autoRunParam.setAnimeEpisodeId(animeEpisodeId);
				autoRunParam.setRefId(refId);
				autoRunParam.setRefType(refType);
			}
			autoRunParam.setName(name);
			autoRunParam.setKey(key);
			autoRunParam.setValue(value);

			autoRunParam = autoRunParamService.save(autoRunParam);

			goPageResult = goPageUtil.createSuccess(request);

		} catch (Exception e) {
			_log.error("updateOneColumn发生错误", e);
			goPageResult = goPageUtil.createFail(request);
		}

		return goPageResult;
	}

	@RequiresPermissions(value = "userList:add")
	@RequestMapping(value = "/deleteOneAutoRunParam")
	@ResponseBody
	public GoPageResult deleteOneAutoRunParam(
			@RequestParam String id,
			HttpServletRequest request) {

		GoPageResult goPageResult = null;
		try {
			autoRunParamService.delete(id);
			goPageResult = goPageUtil.createSuccess(request);

		} catch (Exception e) {
			_log.error("updateOneColumn发生错误", e);
			goPageResult = goPageUtil.createFail(request);
		}

		return goPageResult;
	}

	private void invokeMethod(Object baseEntity, String methodName, String columnValue) {
		Class<?>[] typeClasses = new Class<?>[] { String.class, Integer.class, Long.class, Date.class };
		Class<?> typeClass = null;
		Method setColumnMethod = null;
		for (Class<?> type : typeClasses) {
			if (setColumnMethod != null) {
				break;
			}

			typeClass = type;
			setColumnMethod = XReflectionUtils.findMethod(baseEntity.getClass(), methodName, typeClass);
		}

		if (setColumnMethod == null) {
			throw new RuntimeException("未找到方法：" + baseEntity + ", " + methodName);
		}

		Object value = XStringUtils.convert(columnValue, typeClass);
		XReflectionUtils.invokeMethod(setColumnMethod, baseEntity, value);
	}

}
