package xie.animeshotsite.timer.main;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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

//		createTimer(AnimeShotTimer.class);
		createTimer(ShotTaskTimer.class);
	}

	public static void createTimer(Class<? extends TimerTask> classTimerTask) {

		// ApplicationContext context = new AnnotationConfigApplicationContext(MainTimer.class);
		// TimerTask timerTask = (TimerTask) context.getBean(classTimerTask);

		TimerTask timerTask = (TimerTask) SpringUtil.getBean(classTimerTask);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, 1000, 200000);

	}
}
