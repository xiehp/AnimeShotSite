package xie.base.module.ajax.vo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import xie.base.module.exception.CodeApplicationException;

/**
 * 异常信息处理
 *
 * <pre>
 * Pattern : Value Object
 * Thread Safe : No
 *
 * Change History
 *
 * Name                 Date                    Description
 * -------              -------                 -----------------
 * 020185              2014-3-31            Create the class
 *
 * </pre>
 *
 * @author 020185
 * @version 1.0
 */
public class RequestMessage {

	private static final ThreadLocal<List<String>> THRAED_SEESION = new ThreadLocal<List<String>>();

	private static List<String> get() {
		List<String> list = THRAED_SEESION.get();
		if (list == null) {
			list = new ArrayList<>();
			THRAED_SEESION.set(list);
		}

		return list;
	}

	public static boolean isNull() {
		return THRAED_SEESION.get() == null;
	}

	public static boolean isEmpty() {
		return THRAED_SEESION.get() == null || THRAED_SEESION.get().size() == 0;
	}

	public static void addMessage(String code) {
		get().add(code);
	}

	public static void addMessage(HttpServletRequest request, CodeApplicationException e) {
		addMessage(e.getMessage());
	}

}
