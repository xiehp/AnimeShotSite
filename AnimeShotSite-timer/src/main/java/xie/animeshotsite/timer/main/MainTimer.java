package xie.animeshotsite.timer.main;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.spring.SpringUtil;
import xie.animeshotsite.timer.timer.ShotTaskTimer;

//@SpringBootApplication
@Configuration
@ComponentScan("xie")
public class MainTimer {
	public static void main(String[] args) {

		// ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(BatchConfiguration.class, args);
		// ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(MainTimer.class, args);
		// System.exit(SpringApplication.exit(configurableApplicationContext));
		// SpringApplication.run(MainTimer.class, args);

		// createTimer(AnimeShotTimer.class);
		System.setProperty("spring.profiles.default", "development");
		// System.setProperty("spring.profiles.default", "production");
		createTimer(ShotTaskTimer.class, 10000, ShotTask.TASK_TYPE_SHOT);
		createTimer(ShotTaskTimer.class, 10000, ShotTask.TASK_TYPE_SUBTITLE);

		printProfile();

		// System.out.println(SpringUtil.getProperty("tomcat.deploy.active.spring.profile"));
		// System.out.println(SpringUtil.getCtx().getEnvironment().getDefaultProfiles().length);
		// System.out.println(SpringUtil.getCtx().getEnvironment().getActiveProfiles().length);
		// System.out.println(SpringUtil.getCtx().getEnvironment().getPropertySources().size());
		//
		// System.out.println(SpringUtil.getCtx().getEnvironment().getSystemEnvironment());
		// System.out.println(SpringUtil.getCtx().getEnvironment().getSystemProperties());
		//
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("develoment");
		// System.out.println(SpringUtil.getBean(TietukuConfig.class));
		// System.out.println(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// System.out.println(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));
		//
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("test");
		// System.out.println(SpringUtil.getBean(TietukuConfig.class));
		// System.out.println(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// System.out.println(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("production");
		// System.out.println(SpringUtil.getBean(TietukuConfig.class));
		// System.out.println(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// System.out.println(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));
		// SpringUtil.getCtx().getEnvironment().setActiveProfiles("ggg");
		// System.out.println(SpringUtil.getBean(TietukuConfig.class));
		// System.out.println(SpringUtil.getBean(TietukuConfig.class).getTietukuToken());
		// System.out.println(SpringUtil.getCtx().getEnvironment().getPropertySources().get("tietuku").getProperty("token"));

		// TietukuPropertiesConfiger tietukuPropertiesConfiger = SpringUtil.getBean(TietukuPropertiesConfiger.class);
		// System.out.println(tietukuPropertiesConfiger);
		// System.out.println(tietukuPropertiesConfiger.getNowProfile());
	}

	/***
	 * 
	 * @param classTimerTask
	 * @param period 执行间隔
	 */
	public static void createTimer(Class<? extends TimerTask> classTimerTask, long period, String taskType) {
		ShotTaskTimer shotTaskTimer = (ShotTaskTimer) SpringUtil.getBean(classTimerTask);
		shotTaskTimer.setTaskType(taskType);

		Timer timer = new Timer();
		timer.schedule(shotTaskTimer, 1000, period);
	}

	private static void printProfile() {
		String[] defaultProfiles = SpringUtil.getCtx().getEnvironment().getDefaultProfiles();
		System.out.println("当前默认的profile：" + defaultProfiles.length);
		for (String value : defaultProfiles) {
			System.out.println(value);
		}

		String[] activeProfiles = SpringUtil.getCtx().getEnvironment().getActiveProfiles();
		System.out.println("当前激活的profile：" + activeProfiles.length);
		for (String value : activeProfiles) {
			System.out.println(value);
		}
	}
}
