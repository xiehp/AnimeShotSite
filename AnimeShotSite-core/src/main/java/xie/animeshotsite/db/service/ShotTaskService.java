package xie.animeshotsite.db.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.date.DateUtil;
import xie.common.utils.JsonUtil;
import xie.v2i.config.Video2ImageProperties;

@Service
public class ShotTaskService extends BaseService<ShotTask, String> {

	@Autowired
	private ShotTaskDao shotTaskDao;

	@Override
	public BaseRepository<ShotTask, String> getBaseRepository() {
		return shotTaskDao;
	}

	public List<ShotTask> findNeedRunTask() {
		String ramdomId = String.valueOf(RandomUtils.nextInt());
		List<ShotTask> list1 = shotTaskDao.findByTaskResultAndScheduleTimeIsNull(ramdomId, ShotTask.TASK_RESULT_WAIT);
		logging.info("找到" + list1.size() + "条计划时间null数据");
		if (list1.isEmpty()) {
			List<ShotTask> list2 = shotTaskDao.findByTaskResultAndScheduleTimeLessThan(ramdomId, ShotTask.TASK_RESULT_WAIT, DateUtil.getCurrentDate());
			logging.info("找到" + list2.size() + "条计划时间到达数据");
			list1.addAll(list2);
		}
		return list1;
	}

	public ShotTask beginTask(ShotTask shotTask) {
		shotTask.setTaskResult(ShotTask.TASK_RESULT_PROCESSING);
		shotTask.setStartTime(DateUtil.getCurrentDate());
		return shotTaskDao.save(shotTask);
	}

	public ShotTask endTask(ShotTask shotTask, boolean successFlg, String taskMessage) {
		shotTask.setTaskResult(successFlg ? ShotTask.TASK_RESULT_SUCCESS : ShotTask.TASK_RESULT_FAIL);
		shotTask.setEndTime(DateUtil.getCurrentDate());
		shotTask.setTaskMessage(taskMessage);
		return shotTaskDao.save(shotTask);
	}

	public void addRunSpecifyEpisideTimeTask(String id, Date scheduleTime, Boolean forceUpload, String specifyTimes) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(Video2ImageProperties.KEY_id, id);
		paramMap.put(Video2ImageProperties.KEY_specifyTimes, specifyTimes);
		if (forceUpload != null) {
			paramMap.put(Video2ImageProperties.KEY_forceUpload, forceUpload);
		}
		String jsonStr = JsonUtil.toJsonString(paramMap);

		ShotTask shotTask = new ShotTask();
		shotTask.setTaskClass("xie.animeshotsite.timer.task.ShotSpecifyTask");
		shotTask.setTaskParam(jsonStr);
		shotTask.setScheduleTime(scheduleTime);
		shotTask.setCreateTime(DateUtil.getCurrentDate());
		shotTask.setTaskResult(ShotTask.TASK_RESULT_WAIT);

		shotTaskDao.save(shotTask);
	}

	public void addRunNormalEpisideTimeTask(String id, Date scheduleTime, Boolean forceUpload, Long startTime, Long endTime, Long timeInterval) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(Video2ImageProperties.KEY_id, id);
		if (startTime != null) {
			paramMap.put(Video2ImageProperties.KEY_startTime, startTime);
		}
		if (endTime != null) {
			paramMap.put(Video2ImageProperties.KEY_endTime, endTime);
		}
		if (timeInterval != null) {
			paramMap.put(Video2ImageProperties.KEY_timeInterval, timeInterval);
		}
		if (forceUpload != null) {
			paramMap.put(Video2ImageProperties.KEY_forceUpload, forceUpload);
		}
		String jsonStr = JsonUtil.toJsonString(paramMap);

		ShotTask shotTask = new ShotTask();
		shotTask.setTaskClass("xie.animeshotsite.timer.task.ShotEpisodeTask");
		shotTask.setTaskParam(jsonStr);
		shotTask.setScheduleTime(scheduleTime);
		shotTask.setCreateTime(DateUtil.getCurrentDate());
		shotTask.setTaskResult(ShotTask.TASK_RESULT_WAIT);

		shotTaskDao.save(shotTask);
	}
}
