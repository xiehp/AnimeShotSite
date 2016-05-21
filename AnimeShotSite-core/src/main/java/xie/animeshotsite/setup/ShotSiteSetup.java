package xie.animeshotsite.setup;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import xie.animeshotsite.constants.ShotConstants;
import xie.common.XSpringConstants;

@Component
public class ShotSiteSetup {

	/** 网站图片URL获取方式 腾讯 */
	public static String IMAGE_URL_GET_MODE = ShotConstants.IMAGE_URL_GET_MODE_TIETUKU;

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

}
