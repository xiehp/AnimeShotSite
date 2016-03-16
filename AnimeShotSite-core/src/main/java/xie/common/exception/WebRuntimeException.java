package xie.common.exception;

/**
 * 异常
 */
public class WebRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -7430047488618596583L;

	public WebRuntimeException() {
	}

	public WebRuntimeException(String message) {
		super(message);
	}

	public WebRuntimeException(Throwable e) {
		super(e);
	}

	public WebRuntimeException(String message, Throwable e) {
		super(message, e);
	}
}
