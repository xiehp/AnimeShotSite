package xie.animeshotsite.timer.timer.beforeshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.timer.base.XTask;
import xie.common.constant.XConst;
import xie.common.json.XJsonUtil;
import xie.common.string.XStringUtils;

@Component
@Scope("prototype")
public class EpisodeUpdateMonitorTimer extends TimerTask {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private ShotTaskService shotTaskService;
	@Resource
	private ShotTaskDao shotTaskDao;
	@Resource
	private ApplicationContext applicationContext;

	@Resource
	AutoRunParamService autoRunParamService;
	
	String taskType;

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Override
	public void run() {
		try {
			taskTimer();
		} catch (Exception e) {
			logger.error("执行发生异常，暂停10分钟。");
			try {
				Thread.sleep(XConst.SECOND_10_MIN * 1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void taskTimer() {
		shotTaskDao.count();
		List<ShotTask> list = shotTaskService.findNeedRunTask(taskType);
		if (list.size() > 0) {
			logger.info("找到" + list.size() + "条计划数据" + "， 类型：" + taskType);
		} else {
			logger.debug("找到" + list.size() + "条计划数据" + "， 类型：" + taskType);
		}

		if (list.size() > 0) {
			ShotTask shotTask = list.get(0);
			try {
				String taskClass = shotTask.getTaskClass();
				String paramStr = shotTask.getTaskParam();
				Map<String, Object> param = new HashMap<>();
				if (XStringUtils.isNotBlank(paramStr)) {
					param = XJsonUtil.fromJsonString(paramStr);
				}

				logger.info("任务类：{}, 任务参数:{}", taskClass, param);

				XTask task = null;
				try {
					task = (XTask)applicationContext.getBean(taskClass);
				} catch (Exception e) {
					task = (XTask) applicationContext.getBean(Class.forName(taskClass));
				}

				// 更改标志
				shotTask = shotTaskService.beginTask(shotTask.getId());

				task.runTask(param);

				shotTask = shotTaskService.endTask(shotTask.getId(), true, null);
				logger.info("process 结束 : " + shotTask.getId());
			} catch (Exception e) {
				logger.error("process 失败", e);
				shotTask = shotTaskService.endTask(shotTask.getId(), false, "处理失败， "+StringUtils.substring(e.getMessage(), 0, 512/3) );
			}
		}
	}
}
