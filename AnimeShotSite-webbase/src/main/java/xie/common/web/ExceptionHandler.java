package xie.common.web;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.assertj.core.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import xie.common.Constants;
import xie.common.utils.props.PropsKeys;
import xie.common.utils.props.PropsUtil;
import xie.common.utils.string.GetterUtil;

@Component
public class ExceptionHandler implements HandlerExceptionResolver {
	
	private static Logger _log = LoggerFactory.getLogger(ExceptionHandler.class);
	
	private static boolean LogFlag = GetterUtil.getBoolean(PropsUtil.getProperty(PropsKeys.SYSTEM_ERROR_LOG_ENABLED), false);
	
	public ModelAndView resolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
		String uri = request.getRequestURI();
		//非异步请求，直接跳转到错误页面
		if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request  
                .getHeader("X-Requested-With")!= null && request  
                .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {  
			_log.error("url: {} ,resolveException error: {}", uri, ex);
			ModelAndView modelAndView = new ModelAndView("error/500");
			modelAndView.addObject("exception", ex);
			return modelAndView;
		}else{
			try {  
				Map<String, Object> result = Maps.newHashMap();
				result.put("code", Constants.SUCCESS_CODE);
                PrintWriter writer = response.getWriter();  
                writer.write(result.toString());  
                writer.flush();  
            } catch (Exception e) {  
            	_log.error("url: {} ,resolveException error: {}", uri, ex);
            }  
            return null; 
		}
		
//		String uri = request.getRequestURI();
//		
//		if(ex instanceof SocketException){
//			_log.error("resolveException SocketException:"+uri);
//			return null;
//		}else if(ex.getClass().getCanonicalName().endsWith("ClientAbortException")){
//			//浏览器访问一半手动终止时，会出ClientAbortException
//			_log.error("resolveException ClientAbortException:"+uri);
//			return null;
//		}
//		
//		_log.error("url: {} ,resolveException error: {}", ex);
		
		
	}

}
