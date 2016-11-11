package xie.common.response.body;

import java.io.Serializable;

/**
 * 
 * 在Controller层使用的返回结果类
 * 
 * @version 1.0
 */
public class BaseResult<T extends Object> implements Serializable {
	private static final long serialVersionUID = -5718084560573378145L;
	// 提示信息
	protected String[] alertMessage;
	
	// 异常信息
	protected String exception;
	// 返回的数据
	protected T data;
	// 请求是否成功
	protected boolean success = true;
	//要调整的页面
	protected String goPage;

	public String[] getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(final String alertMessage[]) {
		this.alertMessage = alertMessage;
	}

	public String getException() {
		return exception;
	}

	public void setException(final String exception) {
		this.exception = exception;
	}

	public T getData() {
		return data;
	}

	public void setData(final T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getGoPage() {
		return goPage;
	}

	public void setGoPage(final String goPage) {
		this.goPage = goPage;
	}

	public BaseResult<T> setSuccess(final boolean success) {
		this.success = success;
		return this;
	}
}
