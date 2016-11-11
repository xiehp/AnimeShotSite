package xie.common.response.body;

import java.util.LinkedHashMap;
import java.util.Map;

import xie.common.utils.JsonUtil;


/**
 * 
 * 在Controller层使用的返回结果类
 * 
 * @version 1.0
 */
public class GoPageResult extends BaseResult<Object> {
	private static final long serialVersionUID = 3497928554698321251L;
	// js 内容
	protected String js;

	private String code;
	private String message;

	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

	public GoPageResult setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	/*public String getTemp() {
		return temp;
	}
	
	public void setTemp(String temp) {
		this.temp = temp;
	}*/

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> convertToObjectMap() {
		Map<String, Object> map = new LinkedHashMap<>();
		// BeanMapper.copy(this, map);
		String jsonStr = JsonUtil.toJsonString(this);
		map = JsonUtil.fromJsonString(jsonStr, LinkedHashMap.class);
		return map;
	}
}
