package xie.animeshotsite.db.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xie.animeshotsite.constants.SysConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.base.user.UserUtils;
import xie.common.date.DateUtil;
import xie.common.json.XJsonUtil;
import xie.common.string.XStringUtils;
import xie.v2i.config.Video2ImageProperties;

@Service
public class ShotTaskService extends BaseService<ShotTask, String> {

	@Autowired
	private ShotTaskDao shotTaskDao;

	@Override
	public BaseRepository<ShotTask, String> getBaseRepository() {
		return shotTaskDao;
	}

	public List<ShotTask> findNeedRunTask(String taskType) {
		// String ramdomId = String.valueOf(RandomUtils.nextInt());
		// List<ShotTask> list1 = shotTaskDao.findByTaskResultAndScheduleTimeIsNull(ramdomId, ShotTask.TASK_RESULT_WAIT);
		// logging.info("找到" + list1.size() + "条计划时间null数据");
		// if (list1.isEmpty()) {
		// List<ShotTask> list2 = shotTaskDao.findByTaskResultAndScheduleTimeLessThan(ramdomId, ShotTask.TASK_RESULT_WAIT, DateUtil.getCurrentDate());
		// logging.info("找到" + list2.size() + "条计划时间到达数据");
		// list1.addAll(list2);
		// }
		// return list1;

		List<ShotTask> list = shotTaskDao.findNeedRunTask(taskType, ShotTask.TASK_RESULT_WAIT, DateUtil.getCurrentDate());
		return list;
	}

	public ShotTask beginTask(String shotTaskId) {
		ShotTask shotTask = shotTaskDao.findById(shotTaskId);
		shotTask = beginTask(shotTask);
		return shotTask;
	}

	/**
	 * 开始任务，设置开始时间，更新任务状态
	 */
	public ShotTask beginTask(ShotTask shotTask) {
		logging.info("任务开始前状态，当前entity对象:{}, ID:{}, 数据版本:{} ", shotTask, shotTask.getId(), shotTask.getVersion());
		shotTask.setTaskResult(ShotTask.TASK_RESULT_PROCESSING);
		shotTask.setStartTime(DateUtil.getCurrentDate());
		shotTask = shotTaskDao.save(shotTask);
		logging.info("任务开始后状态，当前entity对象:{}, ID:{}, 数据版本:{} ", shotTask, shotTask.getId(), shotTask.getVersion());
		return shotTask;
	}

	public ShotTask endTask(String shotTaskId, boolean successFlg, String taskMessage) {
		ShotTask shotTask = shotTaskDao.findById(shotTaskId);
		return endTask(shotTask, successFlg, taskMessage);
	}

	/**
	 * 结束任务，设置结束时间，更新任务状态
	 */
	public ShotTask endTask(ShotTask shotTask, boolean successFlg, String taskMessage) {
		logging.info("任务结束前状态，当前entity对象:{}, ID:{}, 数据版本:{} ", shotTask, shotTask.getId(), shotTask.getVersion());
		shotTask.setTaskResult(successFlg ? ShotTask.TASK_RESULT_SUCCESS : ShotTask.TASK_RESULT_FAIL);
		shotTask.setEndTime(DateUtil.getCurrentDate());
		shotTask.setTaskMessage(taskMessage);
		shotTask = shotTaskDao.save(shotTask);
		logging.info("任务结束后状态，当前entity对象:{}, ID:{}, 数据版本:{} ", shotTask, shotTask.getId(), shotTask.getVersion());
		return shotTask;
	}

	/**
	 * 普通用户增加指定时间截图任务
	 */
	public ShotTask addUserSelfRunSpecifyEpisideTimeTask(String id, Date scheduleTime, Boolean forceUpload, String specifyTimes, String userIp) {
		String userType = UserUtils.hasRole(SysConstants.ROLE_ADMIN) ? SysConstants.TASK_USER_TYPE_ADMIN : SysConstants.TASK_USER_TYPE_UNKNOW;
		return addRunSpecifyEpisideTimeTask(id, scheduleTime, forceUpload, specifyTimes, userIp, userType);
	}

	/**
	 * 增加指定时间截图任务
	 */
	public ShotTask addRunSpecifyEpisideTimeTask(String id, Date scheduleTime, Boolean forceUpload, String specifyTimes, String userIp, String source) {
		if (XStringUtils.isBlank(id)) {
			return null;
		}
		if (XStringUtils.isBlank(specifyTimes)) {
			return null;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(Video2ImageProperties.KEY_id, id);
		paramMap.put(Video2ImageProperties.KEY_specifyTimes, specifyTimes);
		if (forceUpload != null) {
			paramMap.put(Video2ImageProperties.KEY_forceUpload, forceUpload);
		}
		String jsonStr = XJsonUtil.toJsonString(paramMap);

		return createShotTask(scheduleTime, ShotTask.TASK_TYPE_SPECIAL_SHOT, "xie.animeshotsite.timer.task.ShotSpecifyTask", jsonStr);
	}

	/**
	 * 增加指定时间间隔截图任务
	 */
	public ShotTask addRunNormalEpisideTimeTask(String id, Date scheduleTime, Boolean forceUpload, Long startTime, Long endTime, Long timeInterval) {
		if (XStringUtils.isBlank(id)) {
			return null;
		}

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
		String jsonStr = XJsonUtil.toJsonString(paramMap);

		return createShotTask(scheduleTime, ShotTask.TASK_TYPE_SHOT, "xie.animeshotsite.timer.task.ShotEpisodeTask", jsonStr);
	}

	/**
	 * 创建字幕任务
	 */
	public ShotTask addCreateSubtitleTask(String subtitleInfoId, String animeInfoId, Date scheduleTime, Boolean forceUpdate, Boolean forceDelete) {
		if (XStringUtils.isBlank(subtitleInfoId) && XStringUtils.isBlank(animeInfoId)) {
			return null;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(SubtitleInfo.COLUMN_ID, subtitleInfoId);
		paramMap.put(SubtitleInfo.COLUMN_animeInfoId, animeInfoId);
		paramMap.put("forceUpdate", forceUpdate);
		paramMap.put("forceDelete", forceDelete);

		String jsonStr = XJsonUtil.toJsonString(paramMap);

		return createShotTask(scheduleTime, ShotTask.TASK_TYPE_SUBTITLE, "xie.animeshotsite.timer.task.CreateSubtitleTask", jsonStr);
	}

	/**
	 * 创建gif任务
	 * 
	 * @param subtitleInfoId
	 * @param animeInfoId
	 * @param episodeInfoId
	 * @param scheduleTime
	 * @param startTime 开始时间
	 * @param continueTime 持续时间
	 * @return
	 */
	public ShotTask addCreateGifTask(String animeInfoId, String episodeInfoId, Date scheduleTime, long startTime, long continueTime) {

		if (XStringUtils.isBlank(animeInfoId) && XStringUtils.isBlank(episodeInfoId)) {
			return null;
		}

		if (startTime < 0) {
			startTime = 0;
		}
		if (continueTime < 1) {
			continueTime = 1;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(AnimeEpisode.COLUMN_ID, episodeInfoId);
		paramMap.put(AnimeEpisode.COLUMN_ANIME_INFO_ID, animeInfoId);
		paramMap.put("startTime", startTime);
		paramMap.put("continueTime", continueTime);

		String jsonStr = XJsonUtil.toJsonString(paramMap);

		return createShotTask(scheduleTime, ShotTask.TASK_TYPE_GIF, "CreateGifTask", jsonStr);
	}

	private ShotTask createShotTask(Date scheduleTime, String taskType, String taskClass, String paramJsonStr) {
		ShotTask shotTask = new ShotTask();
		shotTask.setTaskType(taskType);
		shotTask.setTaskClass(taskClass);
		shotTask.setTaskParam(paramJsonStr);
		shotTask.setScheduleTime(scheduleTime == null ? new Date() : scheduleTime);
		shotTask.setCreateTime(DateUtil.getCurrentDate());
		shotTask.setTaskResult(ShotTask.TASK_RESULT_WAIT);

		shotTask = shotTaskDao.save(shotTask);
		return shotTask;
	}
}
