package xie.animeshotsite.timer.main;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xie.animeshotsite.timer.timer.BaseTaskTimer;
import xie.animeshotsite.timer.timer.ShotTaskTimer;
import xie.animeshotsite.timer.timer.beforeshot.EpisodeFileDownloadTimer;
import xie.animeshotsite.timer.timer.beforeshot.EpisodeUpdateMonitorTimer;
import xie.module.spring.SpringUtil;

//@SpringBootApplication
//@Configuration
//@ComponentScan("xie")
public class DownloadMainTimer {

	private static Logger logger = LoggerFactory.getLogger(DownloadMainTimer.class);

	public static void main(String[] args) {

		// ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(BatchConfiguration.class, args);
		// ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(MainTimer.class, args);
		// System.exit(SpringApplication.exit(configurableApplicationContext));
		// SpringApplication.run(MainTimer.class, args);

		// createTimer(AnimeShotTimer.class);
		System.setProperty("spring.profiles.default", "development");
		// System.setProperty("spring.profiles.default", "productRemote");

		// 截图
		// createTimer(ShotTaskTimer.class, 20 * 1000, ShotTask.TASK_TYPE_SHOT);
		// createTimer(ShotTaskTimer.class, 5 * 1000, ShotTask.TASK_TYPE_SPECIAL_SHOT);
		// createTimer(ShotTaskTimer.class, 20 * 1000, ShotTask.TASK_TYPE_SUBTITLE);
		// createTimer(ShotTaskTimer.class, 20 * 1000, ShotTask.TASK_TYPE_GIF);

		// 自动化处理，监视下载url地址
		createTimer(EpisodeUpdateMonitorTimer.class, 36000 * 1000);
		createTimer(EpisodeFileDownloadTimer.class, 60 * 1000);

		printProfile();

		// logger.info(SpringUtil.getProperty("tomcat.deploy.active.spring.profile"));
		// logger.info(SpringUtil.getCtx().getEnvironment().getDefaultProfiles().length);
		// logger.info(SpringUtil.getCtx().getEnvironment().getActiveProfiles().length);
		// logger.info(SpringUtil.getCtx().getEnvironment().getPropertySources().size());
		//
		// logger.info(SpringUtil.getCtx().getEnvironment().getSystemEnvironment());
		// logger.info(SpringUtil.getCtx().getEnvironment().getSystemProperties());
		//
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("develoment");
		// logger.info(SpringUtil.getBean(TietukuConfig.class));
		// logger.info(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// logger.info(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));
		//
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("test");
		// logger.info(SpringUtil.getBean(TietukuConfig.class));
		// logger.info(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// logger.info(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("production");
		// logger.info(SpringUtil.getBean(TietukuConfig.class));
		// logger.info(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// logger.info(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("ggg");
		// logger.info(SpringUtil.getBean(TietukuConfig.class));
		// logger.info(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// logger.info(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));

		// TietukuPropertiesConfiger tietukuPropertiesConfiger = SpringUtil.getBean(TietukuPropertiesConfiger.class);
		// logger.info(tietukuPropertiesConfiger);
		// logger.info(tietukuPropertiesConfiger.getNowProfile());

		logger.info("主程序执行结束。");
	}

	/***
	 * 
	 * @param classTimerTask
	 * @param period 执行间隔
	 */
	public static void createTimer(Class<? extends TimerTask> classTimerTask, long period, String taskType) {
		ShotTaskTimer shotTaskTimer = (ShotTaskTimer) SpringUtil.getBean(classTimerTask);
		shotTaskTimer.setTaskType(taskType);

		Timer timer = new Timer("timer-" + classTimerTask.getSimpleName() + "_" + taskType + "-" + period);
		timer.schedule(shotTaskTimer, 1000, period);
		logger.info("创建定时器成功：{} {} {}", period, taskType, classTimerTask);
	}

	/***
	 * 
	 * @param classTimerTask
	 * @param period 执行间隔
	 */
	public static void createTimer(Class<? extends TimerTask> classTimerTask, long period) {
		BaseTaskTimer shotTaskTimer = (BaseTaskTimer) SpringUtil.getBean(classTimerTask);

		Timer timer = new Timer();
		timer.schedule(shotTaskTimer, 1000, period);
		logger.info("创建定时器成功：{} {}", period, classTimerTask);
	}

	private static void printProfile() {
		String[] defaultProfiles = SpringUtil.getCtx().getEnvironment().getDefaultProfiles();
		logger.info("当前默认的profile：" + defaultProfiles.length);
		for (String value : defaultProfiles) {
			logger.info(value);
		}

		String[] activeProfiles = SpringUtil.getCtx().getEnvironment().getActiveProfiles();
		logger.info("当前激活的profile：" + activeProfiles.length);
		for (String value : activeProfiles) {
			logger.info(value);
		}
	}
}
