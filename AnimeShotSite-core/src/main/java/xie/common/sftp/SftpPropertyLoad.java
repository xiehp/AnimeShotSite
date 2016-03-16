package xie.common.sftp;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import xie.common.exception.WebRuntimeException;
import xie.common.utils.string.StringUtil;

/**
 * 
 * setup.property文件工具类，通过key取值
 * 
 * @version 1.0
 */
public final class SftpPropertyLoad {

	private static final Logger LOG = LoggerFactory.getLogger(SftpClientUtil.class);

	protected String bundleFileName;
	protected Properties props;

	/**
	 * Instantiates a new portal property load.
	 * 
	 * @throws IOException
	 */
	protected SftpPropertyLoad(final String bundleFileName) {

		this.bundleFileName = bundleFileName;

		final Resource res = new ClassPathResource(StringUtil.replace(bundleFileName, ".", "/") + ".properties");
		final EncodedResource encodedResource = new EncodedResource(res, "UTF-8");

		try {
			props = PropertiesLoaderUtils.loadProperties(encodedResource);
		} catch (IOException e) {
			throw new WebRuntimeException(e);
		}
	}

	/** The instance. */
	private static volatile SftpPropertyLoad instance;

	/**
	 * Gets the single instance of PortalPropertyLoad.
	 * 
	 * @return single instance of PortalPropertyLoad
	 * @throws IOException
	 */
	public static SftpPropertyLoad getInstance() {
		if (null == instance) {
			synchronized (SftpPropertyLoad.class) {
				if (null == instance) {
					instance = new SftpPropertyLoad("sftp");
				}
			}
		}

		return instance;
	}

	public String getString(final String name) {
		final String rtn = props.getProperty(name, null);
		if (rtn == null) {
			LOG.info(bundleFileName + ".properties dosn't exist key : " + name);
		}
		return rtn;
	}

	/**
	 * Gets the int.
	 * 
	 * @param name the name
	 * @return the string
	 */
	public int getInt(final String name) {
		return Integer.parseInt(getString(name));
	}
}
