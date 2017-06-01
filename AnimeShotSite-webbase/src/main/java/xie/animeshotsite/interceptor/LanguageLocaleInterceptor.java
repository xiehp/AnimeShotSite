package xie.animeshotsite.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 多语言处理
 */
@Component
public class LanguageLocaleInterceptor extends HandlerInterceptorAdapter {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取当前request语言情况
	 * 
	 * @param request 请求对象
	 * @param response 回复对象
	 * @param handler 提交对象
	 * @return 成功则返回true，失败则返回false
	 * @throws IOException
	 */
	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler) throws IOException {
//		// 获取当前request语言情况
//		Locale locale = request.getLocale();
//		System.out.println("------------ RequestLocale:" + locale);
//
//		// 获取当前cookie中设置的语言情况
//		String CookieSettingLanguage = XCookieUtils.getCookieValue(request, "CookieSettingLanguage");
//		System.out.println("------------ CookieSettingLanguage:" + CookieSettingLanguage);
//
//		RequestContextUtils.getLocale(request);
//		System.out.println("------------ RequestContextUtils:" + RequestContextUtils.getLocale(request));

		return true;
	}

	/**
	 * 
	 * 设置语言
	 * 
	 * @param request 请求对象
	 * @param response 回复对象
	 * @param handler 提交对象
	 * @param modelAndView 可视化对象
	 */
	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) {

	}
}
