package xie.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import xie.common.string.XStringUtils;
import xie.common.utils.string.StringUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSTL: ${s} ==== <c:out value="${s}" escapeXml="false"></c:out> ==== out.print(s) ==== <%=s%> JSTL: <c:out value="${s}"></c:out> ==== encodeHtml(s) JavaScript alert: must use javaScriptEncode(s) This is a 1-sentence description of this class. This is high level description of this class's
 * capability.
 * 
 * <pre>
 * Pattern : Value Object
 * Thread Safe : No
 * 
 * Change History
 * 
 * Name                 Date                    Description
 * -------              -------                 -----------------
 * 020117              2011-11-16            Create the class
 * 
 * </pre>
 * 
 * @author 020117
 * @version 1.0
 */
public class XSSHttpUtil {
	private static final Logger LOG = LoggerFactory.getLogger(XSSHttpUtil.class);
	private static final Pattern whiteSpacePattern = Pattern.compile("\\s");

	/**
	 * 
	 * 替换成所有空白为空格
	 * 
	 * @param input 输入字符串
	 * @return 替换后字符串
	 */
	private static String replaceLinearWhiteSpace(final String input) {
		return whiteSpacePattern.matcher(input).replaceAll(" ");
	}

	/**
	 * 
	 * 创建一个 header
	 * 
	 * @param response HttpServletResponse
	 * @param name name
	 * @param value value
	 */
	public static void addHeader(final HttpServletResponse response,
			final String name, final String value) {
		final String strippedName = replaceLinearWhiteSpace(name);
		final String strippedValue = replaceLinearWhiteSpace(value);
		response.addHeader(strippedName, strippedValue);
	}

	/**
	 * 
	 * 获取网站地址
	 * 
	 * @param request request
	 */
	public static String getTotalRootURL(final HttpServletRequest request) {
		final String totalUrl = request.getRequestURL().toString();
		return totalUrl.substring(0,
				totalUrl.lastIndexOf(request.getServletPath()))
				+ "/";
	}

	/**
	 * 
	 * 获取站点根目录
	 * 
	 * @param request request
	 */
	public static String getRootURL(final HttpServletRequest request) {
		return request.getContextPath() + "/";
	}

	/**
	 * 
	 * @param url 根据协议（http/https）修改的 url 的端口
	 * @param protocol 要转化的协议
	 * @param portMap 协议对应的端口，格式<协议，端口>
	 */
	public static String changeProtocol(final String url, final String protocol, final Map<String, String> portMap) {
		final Pattern p = Pattern.compile("^(https?)(://[\\.\\w\\-_]+)(:?\\d*)(.*)$");
		final Matcher m = p.matcher(url);

		String rtn = url;
		if (m.find()) {
			rtn = m.replaceAll(protocol + "$2" + getPortByProtocol(protocol, portMap) + "$4");
		}

		return rtn;
	}

	public static String changeToHttp(final String url) {
		if (url == null) {
			return null;
		}
		if (url.startsWith("//")) {
			return url.replaceFirst("//", "http://");
		} else {
			return url.replaceFirst("https://", "http://");
		}
	}

	public static String changeToHttps(final String url) {
		if (url == null) {
			return null;
		}
		if (url.startsWith("//")) {
			return url.replaceFirst("//", "https://");
		} else {
			return url.replaceFirst("http://", "https://");
		}
	}

	public static String changeToHttpOrHttps(String url) {
		if (url == null) {
			return null;
		}
		if (url.startsWith("http")) {
			url = url.replaceFirst("http://", "//");
			url = url.replaceFirst("https://", "//");
		}
		return url;
	}

	/**
	 * 获得url中的主机名
	 * 
	 * @param urlStr urlStr
	 * @return null，未找到或格式不正确
	 */
	public static String getHost(final String urlStr) {
		try {
			URL url = new URL(urlStr);
			return url.getHost();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * 将贴图库的url改为本站url
	 * 
	 * @param url url
	 * @param newDomain newDomain
	 * @param canChangeDomain canChangeDomain
	 */
	public static String changeTietukuDomain(String url, String newDomain, String[] canChangeDomain) {
		if (XStringUtils.isBlank(url)) {
			return url;
		}

		// 由于cdn的https收费，图片链接改为http
		// 20180125 改为跟随页面
		url = XSSHttpUtil.changeToHttps(url);

		if (XStringUtils.isBlank(newDomain)) {
			return url;
		}

		// 可以替换的贴图库图片服务器，新加的由于对应域名需要ca证书，并且几个图片服务器之间不能互通，因此不转换直接使用原地址
		if (!XStringUtils.containWith(url.toLowerCase(), canChangeDomain)) {
			return url;
		}

		// 改变域名
		url = url.replaceAll("\\.[a-z0-9]+\\.[a-z]+", "." + newDomain);

		return url;
	}

	/**
	 * 将贴图库url中的domain改为指定domain
	 * 
	 * @param url url
	 * @param domainConvertMap domainConvertMap
	 */
	public static String convertTietukuDomain(String url, final Map<String, String> domainConvertMap) {
		if (XStringUtils.isBlank(url)) {
			return url;
		}

		// 由于cdn的https收费，图片链接改为http
		// 20180125 改为跟随页面
		url = XSSHttpUtil.changeToHttps(url);

		if (domainConvertMap == null || domainConvertMap.size() == 0) {
			return url;
		}

		String host = getHost(url);
		if (host == null) {
			return url;
		}

		String toHost = domainConvertMap.get(host);
		if (XStringUtils.isBlank(toHost)) {
			return url;
		}
		toHost = toHost.trim();

		// 改变域名
		url = url.replaceFirst("[a-z0-9]+\\.[a-z0-9]+\\.[a-z]+", toHost);

		return url;
	}

	private static String getPortByProtocol(final String protocol, final Map<String, String> portMap) {
		final String rtn;
		String port = portMap.get(protocol);
		if (("http".equals(protocol) && "80".equals(port)) || ("https".equals(protocol) && "443".equals(port))) {
			rtn = "";
		} else {
			rtn = ":" + port;
		}
		return rtn;
	}

	/**
	 * @param url 中的domain name
	 * @param newDomain newDomain
	 *
	 * @return changeDomain
	 * @deprecated 未完成
	 */
	public static String changeDomain(final String url, final String newDomain) {
		final Pattern p = Pattern.compile("^(https?://)([\\.\\w\\-_]+)(:?\\d*/?.*)$");
		final Matcher m = p.matcher(url);

		String rtn = url;
		if (m.find()) {
			rtn = m.replaceAll("$1" + newDomain + "$3");
		}

		return rtn;
	}

	/**
	 * @param url 中的host name
	 * @param newHost
	 * @return 修改过的host
	 */
	public static String changeHost(final String url, final String newHost) {
		final Pattern p = Pattern.compile("^(https?://)([\\.\\w\\-_]+)(:?\\d*/?.*)$");
		final Matcher m = p.matcher(url);

		String rtn = url;
		if (m.find()) {
			rtn = m.replaceAll("$1" + newHost + "$3");
		}

		return rtn;
	}

	/**
	 * @param url url
	 * @param newHostAndPort newHostAndPort
	 *
	 * @return 修改过的host和port
	 * @deprecated 未完成
	 */
	@Deprecated
	public static String changeHostAndPort(final String url, final String newHostAndPort) {
		final Pattern p = Pattern.compile("^(https?://)([\\.\\w\\-_]+)(:?\\d*/?.*)$");
		final Matcher m = p.matcher(url);

		String rtn = url;
		if (m.find()) {
			rtn = m.replaceAll("$1" + newHostAndPort + "$3");
		}

		return rtn;
	}

	/**
	 * 
	 * 设定不保留cache
	 * 
	 * @param response response
	 */
	public static void setNoCacheHeaders(final HttpServletResponse response) {
		// HTTP 1.1
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");

		// HTTP 1.0
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
	}

	/**
	 * 
	 * 设定保留cache
	 * 
	 * @param response response
	 */
	public static void setCacheHeaders(final HttpServletResponse response) {
		// HTTP 1.1
		response.setHeader("Cache-Control", "");

		// HTTP 1.0
		response.setHeader("Pragma", "");
		response.setDateHeader("Expires", 1000000000);
	}

	/**
	 * 
	 * 跳转
	 * 
	 * @param request request
	 * @param response response
	 * @param location 要跳转的地址
	 * @throws ServletException ServletException
	 * @throws IOException IOException
	 */
	public static void sendForward(final HttpServletRequest request,
			final HttpServletResponse response, final String location)
			throws Exception {
		final String loc = StringUtil.removeNullTrim(location);
		if (loc.indexOf("WEB-INF") == 0) {
			LOG.error("Forward failed: Bad forward location: " + loc);
			throw new Exception("Forward failed: Bad forward location: "
					+ loc);
		}
		final RequestDispatcher dispatcher = request
				.getRequestDispatcher(response.encodeURL(loc));
		dispatcher.forward(request, response);
	}

	/**
	 * 
	 * 重定向
	 * 
	 * @param response
	 * @param location 地址
	 * @throws IOException
	 */
	public static void sendRedirect(final HttpServletResponse response,
			final String location) throws IOException {
		final String loc = StringUtil.removeNullTrim(location);
		if (!isSafeURL(loc)) {
			LOG.error("Redirect failed: Bad redirect location: " + loc);
			// throw new SysException("Redirect failed: Bad redirect location: "
			// + loc);
		}
		response.sendRedirect(response.encodeURL(loc));
	}

	/**
	 * 
	 * 是否是mutipart请求
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isMultipartContent(final HttpServletRequest request) {
		final String contentType = StringUtil.removeNullTrim(
				request.getContentType()).toLowerCase(Locale.getDefault());

		final boolean isMultipart;

		isMultipart = contentType.indexOf("multipart/form-data") == 0;
		return isMultipart;
	}

	/**
	 * 
	 * 是不是Https://
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isSafeURL(final String url) {
		final String loc = StringUtil.removeNullTrim(url);
		final boolean safe;

		safe = loc.indexOf("http://") != 0 && loc.indexOf("https://") != 0;
		return safe;
	}

	private static String getParam(final HttpServletRequest request,
			final String paramName) {
		String value = StringUtil.removeNull(request.getParameter(paramName));

		if (StringUtil.isNull(value)) {
			value = StringUtil.removeNull(request.getHeader(paramName))
					.toLowerCase(Locale.getDefault());
		}

		return value;
	}

	/**
	 * 获取要放回的错误信息数据库格式
	 * 
	 * @param request
	 * @return
	 */
	public static final String HEADER_ACCEPT_ERROR_DATATYPE = "Accept-Error-DataType";
	public static final String HEADER_ACCEPT_DATATYPE = "Accept-DataType";
	public static final String CLIENT_DATATYPE_JSON = "json";
	public static final String CLIENT_DATATYPE_XML = "xml";
	public static final String CLIENT_DATATYPE_JSONP = "jsonp";
	public static final String CLIENT_DATATYPE_WEB = "web";

	public static final String HEADER_ACCEPT_CLIENT_TYPE = "Client-Type";
	public static final String CLIENT_TYPE_JQUERY = "jquery";
	public static final String CLIENT_TYPE_ANDROID = "android";
	public static final String CLIENT_TYPE_IOS = "ios";

	public static String getResponseErrorDataType(
			final HttpServletRequest request) {

		String errorDataType = getParam(request,
				HEADER_ACCEPT_ERROR_DATATYPE);

		if ("".equals(errorDataType)) {
			errorDataType = getResponseDataType(request);
		}

		return errorDataType;
	}

	/**
	 * 获取客户端需要的数据格式
	 * 
	 * @param request
	 * @return
	 */
	public static String getResponseDataType(final HttpServletRequest request) {
		String dataType = getParam(request,
				HEADER_ACCEPT_DATATYPE);

		if ("".equals(dataType)) {
			final String accept = StringUtil.removeNull(
					request.getHeader(HttpHeaders.ACCEPT)).toLowerCase(
							Locale.getDefault());

			if (accept.indexOf(',') < 0
					&& accept.indexOf(MediaType.APPLICATION_JSON_VALUE) == 0) {
				dataType = CLIENT_DATATYPE_JSON;
			} else if (accept.indexOf(',') < 0
					&& accept.indexOf(MediaType.APPLICATION_XML_VALUE) == 0) {
				dataType = CLIENT_DATATYPE_XML;
			}
		}

		return "".equals(dataType) ? CLIENT_DATATYPE_WEB : dataType;
	}

	/**
	 * 获取客户端类型：IOS，安卓，jquery
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientType(final HttpServletRequest request) {
		return getParam(request, HEADER_ACCEPT_CLIENT_TYPE);
	}

	/**
	 * 获取请求头信息中的客户端IP地址，可能为多个IP用逗号分隔的字符串
	 * 
	 * @param request
	 * @return
	 */
	public static String getForwardedRemoteIpAddr(final HttpServletRequest request) {
		if (request == null) {
			return "unknown";
		}

		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = "unknown";
		}

		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}

		return ip;
	}

	/**
	 * 客户端ip中可能含有代理ip，获得客户端IP以及代理IP
	 * 
	 * @param request
	 * @return
	 */
	public static String[] getForwardedRemoteIpAddrArray(final HttpServletRequest request) {
		if (request == null) {
			return new String[] { "unknown" };
		}

		String ipAddr = getForwardedRemoteIpAddr(request);
		if (ipAddr == null) {
			ipAddr = "unknown";
		}

		String[] ips = ipAddr.split(",");
		for (int i = 0; i < ips.length; i++) {
			ips[i] = ips[i].trim();
		}
		if (ips.length == 0) {
			ips = new String[] { "unknown" };
		}

		return ips;
	}

	/**
	 * 获得客户端IP的第一个
	 * 
	 * @param request
	 * @return
	 */
	public static String getFirstForwardedRemoteIpAddr(final HttpServletRequest request) {
		String[] ips = getForwardedRemoteIpAddrArray(request);
		return ips[0];
	}

	public static String getForwardedServerName(final HttpServletRequest request) {
		String hostName = request.getHeader("X-Forwarded-Host");
		if (XStringUtils.isBlank(hostName)) {
			hostName = request.getServerName();
		}

		return hostName;
	}

	public static String getForwardedServerPort(final HttpServletRequest request) {
		String port = request.getHeader("X-Forwarded-Port");
		if (XStringUtils.isBlank(port)) {
			port = String.valueOf(request.getServerPort());
		}

		return port;
	}

	public static String getForwardedRemoteProto(final HttpServletRequest request) {
		String proto = request.getHeader("X-Forwarded-Proto");
		if (XStringUtils.isBlank(proto)) {
			proto = String.valueOf(request.getScheme());
		}

		return proto;
	}
}
