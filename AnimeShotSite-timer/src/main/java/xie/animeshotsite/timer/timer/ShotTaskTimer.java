package xie.animeshotsite.timer.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.spring.SpringUtil;
import xie.animeshotsite.timer.base.XTask;
import xie.common.string.XStringUtils;
import xie.common.utils.JsonUtil;

@Component
public class ShotTaskTimer extends TimerTask {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	AnimeEpisodeService animeEpisodeService;

	@Autowired
	ShotTaskService shotTaskService;
	@Autowired
	ShotTaskDao shotTaskDao;

	@Autowired
	EntityManager entityManager;

	@Override
	public void run() {
		
		// entityManager.getEntityManagerFactory().getCache().evictAll();
		// sessionFactory.getCache().evictEntityRegions();
		
		List<ShotTask> list = shotTaskService.findNeedRunTask();
		logger.info("list", list);

		boolean isBegin;
		for (ShotTask shotTask : list) {
			isBegin = false;
			try {
				String taskClass = shotTask.getTaskClass();
				String paramStr = shotTask.getTaskParam();
				Map<String, Object> param = new HashMap<>();
				if (XStringUtils.isNotBlank(paramStr)) {
					param = JsonUtil.fromJsonString(paramStr);
				}

				XTask task = (XTask) Class.forName(taskClass).newInstance();
				task = (XTask) SpringUtil.getBean(Class.forName(taskClass));

				// 更改标志
				shotTask = shotTaskService.beginTask(shotTask);
				isBegin = true;

				task.runTask(param);

				shotTask = shotTaskService.endTask(shotTask, true, null);
				logger.info("process 成功 : " + shotTask.getId());

			} catch (Exception e) {
				shotTask = shotTaskService.endTask(shotTask, false, e.getMessage());
				logger.error("process 失败", e);
			}
		}
	}

}
