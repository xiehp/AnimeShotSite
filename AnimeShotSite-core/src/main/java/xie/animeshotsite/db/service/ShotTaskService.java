package xie.animeshotsite.db.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yjysh.framework.base.repository.BaseRepository;
import com.yjysh.framework.base.service.BaseService;
import com.yjysh.framework.common.utils.DateUtil;
import com.yjysh.framework.common.utils.JsonUtil;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.repository.ShotTaskDao;

@Service
public class ShotTaskService extends BaseService<ShotTask, String> {

	@Autowired
	private ShotTaskDao shotTaskDao;

	@Override
	public BaseRepository<ShotTask, String> getBaseRepository() {
		return shotTaskDao;
	}

	public List<ShotTask> findNeedRunTask() {
		List<ShotTask> list1 = shotTaskDao.findByTaskResultAndScheduleTimeIsNull(ShotTask.TASK_RESULT_WAIT);
		if (list1.isEmpty()) {
			List<ShotTask> list2 = shotTaskDao.findByTaskResultAndScheduleTimeGreaterThan(ShotTask.TASK_RESULT_WAIT, DateUtil.getCurrentDate());
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

	public void addRunSpecifyEpisideTimeTask(String id, Date scheduleTime, Boolean forceUpload) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		String jsonStr = JsonUtil.toJsonString(paramMap);

		ShotTask shotTask = new ShotTask();
		shotTask.setTaskClass("xie.animeshotsite.timer.task.ShotSpecifyTask");
		shotTask.setTaskParam(jsonStr);
		shotTask.setScheduleTime(scheduleTime);
		shotTask.setCreateTime(DateUtil.getCurrentDate());
		shotTask.setTaskResult(ShotTask.TASK_RESULT_WAIT);

		shotTaskDao.save(shotTask);
	}

	public void addRunNormalEpisideTimeTask(String id, Date scheduleTime, Boolean forceUpload) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
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
