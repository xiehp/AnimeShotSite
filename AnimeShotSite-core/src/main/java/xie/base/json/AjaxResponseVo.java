package xie.base.json;

import java.util.List;
import java.util.Map;

public class AjaxResponseVo {
	public static final String JSON_RESPONSE_KEY_CODE = "code";
	public static final String JSON_RESPONSE_KEY_MESSAGE = "message";
	public static final String JSON_RESPONSE_KEY_SUCCESS = "success";

	/** 是否处理了 */
	private boolean processFlag;
	private boolean logicFlag;
	private String code;
	private List<String> alertMessage;
	private Map<String, Object> data;
}
