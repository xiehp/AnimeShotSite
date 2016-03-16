package xie.common.exception;

import org.apache.shiro.authc.AuthenticationException;

public class NoPermissionException extends AuthenticationException {

    /**
     * Creates a new NoPermissionException.
     */
    public NoPermissionException() {
        super();
    }

    /**
     * Constructs a new NoPermissionException.
     *
     * @param message the reason for the exception
     */
    public NoPermissionException(String message) {
        super(message);
    }

    /**
     * Constructs a new NoPermissionException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public NoPermissionException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new NoPermissionException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
