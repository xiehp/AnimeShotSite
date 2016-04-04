package xie.common.utils.props;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springside.modules.utils.PropertiesLoader;

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

		try {
			if (propertiesLoaderTest != null) {
				property = propertiesLoaderTest.getProperty(key);
			}

		} catch (Exception e) {
			_log.debug("propertiesLoaderTest not exist [" + key + "]", e.getMessage());
		}

		try {
			if (propertiesLoaderDevelopment != null) {
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
