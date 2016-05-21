package xie.animeshotsite.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * Spring组件工具类
 *
 * <pre>
 * Pattern : Value Object
 * Thread Safe : No
 *
 * Change History
 *
 * Name                 Date                    Description
 * -------              -------                 -----------------
 * 020191              2014-3-31            Create the class
 *
 * </pre>
 *
 * @author 020191
 * @version 1.0
 */
public final class SpringUtil implements BeanFactoryAware {
	public final static String WEB_TYPE = "W";
	public final static String BEAN_TYPE = "B";

	public final static String BEAN_CONTENT_TYPE = "B";

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(SpringUtil.class);

	/** The locator. */
	private static volatile BeanFactoryLocator locator;

	/** The bfr. */
	private static BeanFactoryReference bfr;

	/** The factory. */
	private static ClassPathXmlApplicationContext ctx;

	/** The factory. */
	private static BeanFactory factory;
	
	/**
	 * 
	 * 根据对象名获得对象
	 * 
	 * @param beanRef 对象名
	 * @return Object对象
	 */
	@SuppressWarnings("unchecked")
	public static <X> X getBean(final String beanRef) {
		return (X) getBeanFactory().getBean(beanRef);
	}

	/**
	 * 
	 * 根据对象类型获得对象
	 * 
	 * @param clazz 对象类型
	 * @return <X> X 对象
	 */
	public static <X> X getBean(final Class<X> clazz) {
		return getBeanFactory().getBean(clazz);
	}

	public static String getProperty(String key) {
		return ctx.getEnvironment().getProperty(key);
	}

	public static ClassPathXmlApplicationContext getCtx() {
		return ctx;
	}

	public static List<String> getNowProfilesList() {
		getBeanFactory();

		List<String> nowProfilesList = new ArrayList<String>();
		String[] activeProfiles = SpringUtil.getCtx().getEnvironment().getActiveProfiles();
		if (activeProfiles != null && activeProfiles.length > 0) {
			for (String profile : activeProfiles) {
				nowProfilesList.add(profile);
			}
			return nowProfilesList;
		}

		String[] defaultProfiles = SpringUtil.getCtx().getEnvironment().getDefaultProfiles();
		if (defaultProfiles != null && defaultProfiles.length > 0) {
			for (String profile : defaultProfiles) {
				nowProfilesList.add(profile);
			}
			return nowProfilesList;
		}

		return nowProfilesList;
	}

	/**
	 * 
	 * 初始化Factory
	 */
	private static BeanFactory getBeanFactory() {
		if (factory == null) {
			synchronized (SpringUtil.class) {
				if (factory == null) {
					// if (BEAN_TYPE.equalsIgnoreCase(BEAN_CONTENT_TYPE)) {
					// try {
					// if (null == locator) {
					// locator = ContextSingletonBeanFactoryLocator.getInstance("classpath*:beanRefContext.xml");
					// }
					// if (null == bfr) {
					// bfr = locator.useBeanFactory("beanfactory");
					// }
					// if (null == factory) {
					// factory = bfr.getFactory();
					// }
					// } catch (Exception e) {
					// LOG.error(e.getMessage(), e);
					// }
					// } else {
					// // factory = SpringWebUtil.getBeanFactory();
					// }

					LOG.warn("未发现自动设置bean，开始手动加载applicationContext.xml");
					ctx = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
					factory = ctx.getBeanFactory();
					LOG.info("手动加载结束，xml：" + factory + ", ctx.getApplicationName():" + ctx.getApplicationName() + ", ctx.getDisplayName():" + ctx.getDisplayName());
				}
			}
		}

		return factory;
	}

	@Override
	public void setBeanFactory(final BeanFactory factory) throws BeansException {
		SpringUtil.factory = factory;

		LOG.info("由系统自动设置BeanFactory：" + factory);
	}
}
