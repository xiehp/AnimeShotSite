package xie.animeshotsite.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.base.entity.BaseEntity;

@Entity
@Table(name = ShotTask.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class ShotTask extends BaseEntity {

	private static final long serialVersionUID = 2981238492982330522L;

	public static final String TABLE_NAME = "shot_task";
	public static final String ENTITY_NAME = ShotTask.class.getSimpleName();

	/** 任务类型 截图 */
	public static final String TASK_TYPE_SHOT = "SHOT";
	/** 任务类型 字幕 */
	public static final String TASK_TYPE_SUBTITLE = "SUBTITLE";
	/** 任务类型 gif动画 */
	public static final String TASK_TYPE_GIF = "GIF";

	/** 任务状态 未执行 */
	public static final Integer TASK_RESULT_WAIT = 0;
	/** 任务状态 成功 */
	public static final Integer TASK_RESULT_SUCCESS = 1;
	/** 任务状态 执行中 */
	public static final Integer TASK_RESULT_PROCESSING = 2;
	/** 任务状态 失败 */
	public static final Integer TASK_RESULT_FAIL = 3;

	/** 任务类型 */
	private String taskType;

	/** 执行任务的java class */
	private String taskClass;

	/** 参数 */
	private String taskParam;

	/** 创建时间 */
	private Date createTime;

	/** 准备执行时间 */
	private Date scheduleTime;

	/** 实际开始时间 */
	private Date startTime;

	/** 实际结束时间 */
	private Date endTime;

	/** 0:未开始 1:成功 2:运行中 3:失败 */
	private Integer taskResult;

	/** 执行结果信息 */
	private String taskMessage;

	/** 执行结果信息 */
	private Integer status;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskClass() {
		return taskClass;
	}

	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}

	public String getTaskParam() {
		return taskParam;
	}

	public void setTaskParam(String taskParam) {
		this.taskParam = taskParam;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(Integer taskResult) {
		this.taskResult = taskResult;
	}

	public String getTaskMessage() {
		return taskMessage;
	}

	public void setTaskMessage(String taskMessage) {
		this.taskMessage = taskMessage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
