package xie.base.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import xie.common.java.XExtClasspathLoader;

public class LoadLibListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String libPathStr = (String) sce.getServletContext().getInitParameter("webapp.webinf.lib");
		XExtClasspathLoader loader = new XExtClasspathLoader();
		loader.setJarFileDir(libPathStr);
		loader.setDirNotExistThrowFlg(false);
		loader.loadClasspath();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
