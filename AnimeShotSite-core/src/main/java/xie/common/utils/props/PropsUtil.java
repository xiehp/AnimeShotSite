package xie.common.utils.props;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.utils.PropertiesLoader;

import xie.common.Constants;
import xie.common.utils.SpringUtils;

public class PropsUtil {

	private static Logger _log = LoggerFactory.getLogger(PropsUtil.class);

	protected static PropertiesLoader propertiesLoader = new PropertiesLoader("classpath:/application.properties");
	protected static PropertiesLoader propertiesLoaderTest = new PropertiesLoader("classpath:/application.test.properties");
	protected static PropertiesLoader propertiesLoaderDevelopment = new PropertiesLoader("classpath:/application.development.properties");

	public static String getProperty(String key) {
		String property = null;
		try {
			property = propertiesLoader.getProperty(key);
		} catch (Exception e) {
			_log.error("propertiesLoader not exist [" + key + "]", e.getMessage());
		}

		List<String> nowProfilesList = new ArrayList<String>();
		nowProfilesList.add(Constants.MAVEN_PROFILE_TEST);
		nowProfilesList.add(Constants.MAVEN_PROFILE_DEVELOPMENT);
//		if (SpringUtils.getNowProfilesList() != null) {
//			nowProfilesList = SpringUtils.getNowProfilesList();
//		}
//		_log.info("nowProfilesList:" + nowProfilesList);

		try {
			if (propertiesLoaderTest != null && nowProfilesList.contains(Constants.MAVEN_PROFILE_TEST)) {
				property = propertiesLoaderTest.getProperty(key);
			}
		} catch (Exception e) {
			_log.debug("propertiesLoaderTest not exist [" + key + "]", e.getMessage());
		}

		try {
			if (propertiesLoaderDevelopment != null && nowProfilesList.contains(Constants.MAVEN_PROFILE_DEVELOPMENT)) {
				property = propertiesLoaderDevelopment.getProperty(key);
			}

		} catch (Exception e) {
			_log.debug("propertiesLoaderDevelopment not exist [" + key + "]", e.getMessage());
		}

		return property;
	}

	public static String getProperty(String key, String value) {
		if (propertiesLoader.getProperty(key) == null)
			return value;
		return propertiesLoader.getProperty(key);
	}

}
