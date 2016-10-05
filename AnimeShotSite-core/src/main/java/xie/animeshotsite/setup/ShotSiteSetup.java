package xie.animeshotsite.setup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import xie.animeshotsite.constants.ShotCoreConstants;
import xie.common.XSpringConstants;
import xie.common.io.XFileWriter;

@Component
public class ShotSiteSetup {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	/** 网站图片URL获取方式 腾讯 */
	public static String IMAGE_URL_GET_MODE = ShotCoreConstants.IMAGE_URL_GET_MODE_TIETUKU;

	@Value("#{" + XSpringConstants.SPRING_PROPERTIES_ID + "}")
	Properties properties;

	/** 是否进行统计 1：统计 其他：不统计 */
	@Value("#{" + XSpringConstants.SPRING_PROPERTIES_ID + "['animesite.search.traffic.statistics']}")
	private String animesiteSearchTrafficStatistics;

	/** 强制指定服务的host，如果不是，则重定向到该host，可以为空 */
	@Value("#{" + XSpringConstants.SPRING_PROPERTIES_ID + "['animesite.server.host']}")
	private String animesiteServerHost;

	/** 网站域名 acgimage.com */
	private String siteDomain;

	@Value("#{" + XSpringConstants.SPRING_PROPERTIES_ID + "['animesite.js.debug']}")
	private String animesiteJsDebug;

	/** 不需要进行网站访问统计的IP，一般为一些爬虫或漏洞检测IP */
	private List<String> excludeIpsRuleList;

	/** 指定哪些贴图库网址转成自己的域名 */
	private String[] tietukuChangeDoman = new String[] {
			"i1.piimg.com", "i2.piimg.com", "i3.piimg.com", "i4.piimg.com",
			"i1.buimg.com", "i2.buimg.com", "i3.buimg.com", "i4.buimg.com"};

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/** 是否进行统计 1：统计 其他：不统计 */
	public String getAnimesiteSearchTrafficStatistics() {
		return animesiteSearchTrafficStatistics;
	}

	/** 强制指定服务的host，如果不是，则重定向到该host，可以为空 */
	public String getAnimesiteServerHost() {
		return animesiteServerHost;
	}

	/** 网站域名 acgimage.com */
	public String getSiteDomain() {
		siteDomain = "acgimage.com";
		return siteDomain;
	}

	public String getAnimesiteJsDebug() {
		return animesiteJsDebug;
	}

	public List<String> getExcludeIpsRuleList() {
		return excludeIpsRuleList;
	}

	public void resetExcludeIpsRuleList(HttpServletRequest httpServletRequest) {
		excludeIpsRuleList = new ArrayList<>();
		try {
			String filePath = httpServletRequest.getServletContext().getRealPath("/WEB-INF/resources/excludeIpsRuleList.txt");
			excludeIpsRuleList = XFileWriter.readList(filePath);
			logger.info("读取排除IP文件[{}]成功，当前数量：{}", filePath, excludeIpsRuleList.size());
		} catch (IOException e) {
			logger.error("读取排除文件发生异常", e);
		}
	}

	public String[] getTietukuChangeDoman() {
		return tietukuChangeDoman;
	}

	public void setTietukuChangeDoman(String[] tietukuChangeDoman) {
		this.tietukuChangeDoman = tietukuChangeDoman;
	}

}
