package xie.common.utils.props;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import xie.common.constant.XSpringConstants;

@Component
public class XSpringProperties {

	@Value("#{" + XSpringConstants.SPRING_PROPERTIES_ID + "}")
	Properties properties;

	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
