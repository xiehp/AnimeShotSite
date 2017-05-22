package xie.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class YjyControllerAdvice {
	protected static Logger logger = LoggerFactory.getLogger(YjyControllerAdvice.class);

	// 当前使用WebHandlerExceptionResolver
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView processUnauthenticatedException(NativeWebRequest request, Exception e) {
		logger.error(YjyControllerAdvice.class.getSimpleName() + "发现错误的请求：{}", e.getMessage());
		throw new RuntimeException(e.getMessage(), e);
	}
}
