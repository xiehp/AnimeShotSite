package xie.base.listener;

import java.io.File;
import java.net.URLClassLoader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import xie.common.java.XExtClasspathLoader;

public class LoadLibListener implements ServletContextListener {

	// Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// logger.info("begin load lib");
		System.out.println("load lib start.");
		String loadFlg = (String) sce.getServletContext().getInitParameter("webapp.webinf.lib.loadFlg");
		if ("1".equals(loadFlg)) {
			String libPathStr = (String) sce.getServletContext().getInitParameter("webapp.webinf.lib");
			// logger.info("load lib path:" + libPathStr);
			System.out.println("load lib path:" + libPathStr);
			if (libPathStr != null && !"".equals(libPathStr)) {
				// logger.info("load lib path:" + new File(libPathStr).getAbsolutePath());
				System.out.println("load lib path:" + new File(libPathStr).getAbsolutePath());
			}

			URLClassLoader urlClassLoader = (URLClassLoader) getClass().getClassLoader().getParent().getParent();
			XExtClasspathLoader loader = new XExtClasspathLoader();
			loader.setJarFileDir(libPathStr);
			loader.setDirNotExistThrowFlg(false);
			loader.setClassLoader(urlClassLoader);
			loader.loadClasspath();
			System.out.println("load lib end.");
		} else {
			System.out.println("webapp.webinf.lib.loadFlg not set 1, load skip.");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
