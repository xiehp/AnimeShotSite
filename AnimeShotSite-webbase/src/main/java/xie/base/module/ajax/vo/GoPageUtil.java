package xie.base.module.ajax.vo;

import xie.common.string.XStringUtils;

/**
 * 
 * 获取GoPageResult工具类
 * 
 * @version 1.0
 */
public class GoPageUtil {
	public static GoPageResult getNullResult() {
		return getResult(null, null, null, null);
	}

	public static GoPageResult getResult(final String goPage) {
		return getResult(null, goPage, null, null);
	}

	public static GoPageResult getResult(final Object data) {
		return getResult(null, null, null, data);
	}

	public static GoPageResult getResult(final String goPage, final Object data) {
		return getResult(null, goPage, null, data);
	}

	private static GoPageResult getResult(String message, final String goPage, final String openerGoPage, final Object data) {
		return getResultByMessage(message == null ? null : new String[] { message }, null, goPage, openerGoPage, data);
	}

	public static GoPageResult getResultByMessage(final String[] alertMessage, final String exception, final String goPage, final String openerGoPage, final Object data) {
		final GoPageResult goPageResult = new GoPageResult();

		goPageResult.setAlertMessage(alertMessage);
		goPageResult.setData(data);
		if (XStringUtils.isBlank(exception)) {
			goPageResult.setException(null);
		} else {
			goPageResult.setException("server occur exception");
		}
		goPageResult.setGoPage(goPage);
		goPageResult.setSuccess(true);

		return goPageResult;
	}
}
