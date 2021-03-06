package xie.animeshotsite.timer.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.timer.base.XTask;
import xie.common.json.XJsonUtil;
import xie.common.string.XStringUtils;

/**
 *
 */
@Component
@Scope("prototype")
public class ShotTaskTimer extends BaseTaskTimer {

	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private ApplicationContext applicationContext;
	@Resource
	private ShotTaskDao shotTaskDao;
	@Resource
	private ShotTaskService shotTaskService;
	private XTask task;
	private Map<String, Object> taskParam;
	private String taskResult;
	private String taskType;

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Override
	protected void taskTimer() {
		task = null;
		taskParam = new HashMap<>();
		taskResult = null;

		shotTaskDao.count();
		List<ShotTask> list = shotTaskService.findNeedRunTask(taskType);
		if (list.size() > 0) {
			_log.info("找到" + list.size() + "条计划数据" + "， 类型：" + taskType);
		} else {
			_log.debug("找到" + list.size() + "条计划数据" + "， 类型：" + taskType);
		}

		if (list.size() > 0) {
			ShotTask shotTask = list.get(0);
			try {
				String taskClass = shotTask.getTaskClass();
				String paramStr = shotTask.getTaskParam();
				if (XStringUtils.isNotBlank(paramStr)) {
					taskParam = XJsonUtil.fromJsonString(paramStr);
				}

				_log.info("任务类：{}, 任务参数:{}", taskClass, taskParam);

				try {
					task = (XTask) applicationContext.getBean(taskClass);
				} catch (Exception e) {
					task = (XTask) applicationContext.getBean(Class.forName(taskClass));
				}

				// 更改标志
				shotTask = shotTaskService.beginTask(shotTask.getId());

				Thread thread = new Thread(() -> {
					try {
						task.runTask(taskParam);
					} catch (Exception e) {
						taskResult = e.toString();
						e.printStackTrace();
					}
				});
				thread.setDaemon(true);
				thread.start();
				thread.join();

				if (taskResult == null) {
					shotTaskService.endTask(shotTask.getId(), true, null);
					_log.info("process 结束 : " + shotTask.getId());
				} else {
					throw new Exception(taskResult);
				}
			} catch (Exception e) {
				_log.error("process 失败", e);
				shotTaskService.endTask(shotTask.getId(), false, "处理失败， " + StringUtils.substring(e.getMessage(), 0, 512 / 3));
			}
		}
	}
}
