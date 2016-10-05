package xie.common.excel;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import xie.common.string.XStringUtils;
import xie.common.utils.string.StringUtil;

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

	/**
	 * 
	 * 替换成所有空白为空格
	 * 
	 * @param input 输入字符串
	 * @return 替换后字符串
	 */
	private static String replaceLinearWhiteSpace(final String input) {
		final Pattern p = Pattern.compile("\\s");
		return p.matcher(input).replaceAll(" ");
	}

	/**
	 * 
	 * 创建一个 header
	 * 
	 * @param response HttpServletResponse
	 * @param name
	 * @param value
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
	 * @param request
	 * @return
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
	 * @param request
	 * @return
	 */
	public static String getRootURL(final HttpServletRequest request) {
		return request.getContextPath() + "/";
	}

	/**
	 * 
	 * @param 根据协议（http/https）修改的 url 的端口
	 * @param protocol 要转化的协议
	 * @param portMap 协议对应的端口，格式<协议，端口>
	 * @return
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
	 * 
	 * @param 修改 url 中的domain name
	 * @param newdomain
	 * @return
	 */
	public static String changeDomain(final String url, final String newdomain) {
		final Pattern p = Pattern.compile("^(https?://)([\\.\\w\\-_]+)(:?\\d*/?.*)$");
		final Matcher m = p.matcher(url);

		String rtn = url;
		if (m.find()) {
			rtn = m.replaceAll("$1" + newdomain + "$3");
		}

		return rtn;
	}

	/**
	 * 
	 * 设定不保留cache
	 * 
	 * @param response
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
	 * @param response
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
	 * @param request
	 * @param response
	 * @param location 要跳转的地址
	 * @throws ServletException
	 * @throws IOException
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

		if (contentType.indexOf("multipart/form-data") == 0) {
			isMultipart = true;
		} else {
			isMultipart = false;
		}
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

		if (loc.indexOf("http://") == 0 || loc.indexOf("https://") == 0) {
			safe = false;
		} else {
			safe = true;
		}
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
	public static String getIpAddr(final HttpServletRequest request) {
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
	public static String[] getIpAddrArray(final HttpServletRequest request) {
		if (request == null) {
			return new String[] { "unknown" };
		}

		String ipAddr = getIpAddr(request);
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
	public static String getIpAddrFirst(final HttpServletRequest request) {
		String[] ips = getIpAddrArray(request);
		return ips[0];
	}

	public static String getRemoteServerName(final HttpServletRequest request) {
		String hostName = request.getHeader("X-Forwarded-Host");
		if (XStringUtils.isBlank(hostName)) {
			hostName = request.getServerName();
		}

		return hostName;
	}

	public static String getRemotePort(final HttpServletRequest request) {
		String port = request.getHeader("X-Forwarded-Port");
		if (XStringUtils.isBlank(port)) {
			port = String.valueOf(request.getServerPort());
		}

		return port;
	}
}
