package xie.common.exception;

/**
 * 评异常
 */
public class WebException extends Exception {

	private static final long serialVersionUID = -5270843538281219121L;

	public WebException() {
	}

	public WebException(String message) {
		super(message);
	}

	public WebException(Throwable e) {
		super(e);
	}

	public WebException(String message, Throwable e) {
		super(message, e);
	}
}
