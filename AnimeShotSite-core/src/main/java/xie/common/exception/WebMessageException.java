package xie.common.exception;

/**
 * 错误信息
 */
public class WebMessageException extends WebException {

	private static final long serialVersionUID = -5270843538281219121L;

	public WebMessageException() {
	}

	public WebMessageException(String message) {
		super(message);
	}

	public WebMessageException(Throwable e) {
		super(e);
	}

	public WebMessageException(String message, Throwable e) {
		super(message, e);
	}
}
