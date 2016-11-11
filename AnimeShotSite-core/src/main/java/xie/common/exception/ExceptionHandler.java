package xie.common.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import xie.common.Constants;
import xie.common.response.body.GoPageResult;
import xie.common.utils.props.PropsKeys;
import xie.common.utils.props.PropsUtil;
import xie.common.utils.string.GetterUtil;



@Component
public class ExceptionHandler implements HandlerExceptionResolver {

	private Logger _log = LoggerFactory.getLogger(this.getClass());

	private static boolean LogFlag = GetterUtil.getBoolean(PropsUtil.getProperty(PropsKeys.SYSTEM_ERROR_LOG_ENABLED), false);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String url = request.getRequestURL().toString();
		_log.error("url: {} ,resolveException error: {}", url, ex);

		// 非异步请求，直接跳转到错误页面
		if (!(request.getHeader("accept").indexOf("application/json") > -1
				|| (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
			ModelAndView modelAndView = new ModelAndView("error/500");
			modelAndView.addObject("exception", ex);
			return modelAndView;
		} else {
			GoPageResult goPageResult = new GoPageResult();
			goPageResult.setSuccess(false);
			goPageResult.setAlertMessage(new String[] { ex.getMessage() });
			goPageResult.setException(ex.getClass().toString());
			goPageResult.setGoPage("");
			goPageResult.setCode(Constants.FAIL_CODE);
			goPageResult.setMessage(ex.getMessage());

			MappingJackson2JsonView mappingJackson2JsonView = new MappingJackson2JsonView();
			ModelAndView modelAndView = new ModelAndView(mappingJackson2JsonView);
			Map<String, Object> goPageResultMap = goPageResult.convertToObjectMap();
			modelAndView.addAllObjects(goPageResultMap);
			// modelAndView.addObject(JsonView.class.getName(), MappingJackson2JsonView.class);

			// MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(goPageResult);
			// modelAndView.addObject(goPageResult);
			// modelAndView.addObject("fff", "fff");
			// modelAndView.addObject("bbb", "bbb");
			// modelAndView.addObject("vvv", "vvv");
			return modelAndView;
		}

		// String uri = request.getRequestURI();
		//
		// if(ex instanceof SocketException){
		// _log.error("resolveException SocketException:"+uri);
		// return null;
		// }else if(ex.getClass().getCanonicalName().endsWith("ClientAbortException")){
		// //浏览器访问一半手动终止时，会出ClientAbortException
		// _log.error("resolveException ClientAbortException:"+uri);
		// return null;
		// }
		//
		// _log.error("url: {} ,resolveException error: {}", ex);

	}

}
