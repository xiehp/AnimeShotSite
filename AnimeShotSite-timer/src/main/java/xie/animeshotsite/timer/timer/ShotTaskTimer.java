package xie.animeshotsite.timer.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yjysh.framework.common.utils.JsonUtil;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.timer.base.XTask;
import xie.common.string.XStringUtils;

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

	@Override
	public void run() {
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
