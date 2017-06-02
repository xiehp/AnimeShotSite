package xie.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import xie.base.module.exception.WebHandlerExceptionResolver;

@ControllerAdvice
public class YjyControllerAdvice {
	protected static Logger _log = LoggerFactory.getLogger(YjyControllerAdvice.class);

	// 当前使用WebHandlerExceptionResolver
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ModelAndView processUnauthenticatedException(
			HttpServletRequest request,
			HttpServletResponse response,
			Exception ex) {

		return WebHandlerExceptionResolver.processException(request, response, null, ex, _log);
	}
}
