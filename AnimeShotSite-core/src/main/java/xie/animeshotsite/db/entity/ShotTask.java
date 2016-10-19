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

	public static final String COLUMN_TASK_TYPE = "taskType";

	/** 任务类型 截图 */
	public static final String TASK_TYPE_SHOT = "SHOT";
	/** 任务类型 指定截图 */
	public static final String TASK_TYPE_SPECIAL_SHOT = "SPECIAL_SHOT";
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

	/**
	 * 获取 任务类型.
	 *
	 * @return 任务类型
	 */
	public String getTaskType() {
		return taskType;
	}

	/**
	 * 设置 任务类型.
	 *
	 * @param taskType 任务类型
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	/**
	 * 获取 执行任务的java class.
	 *
	 * @return 执行任务的java class
	 */
	public String getTaskClass() {
		return taskClass;
	}

	/**
	 * 设置 执行任务的java class.
	 *
	 * @param taskClass 执行任务的java class
	 */
	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}

	/**
	 * 获取 参数.
	 *
	 * @return 参数
	 */
	public String getTaskParam() {
		return taskParam;
	}

	/**
	 * 设置 参数.
	 *
	 * @param taskParam 参数
	 */
	public void setTaskParam(String taskParam) {
		this.taskParam = taskParam;
	}

	/**
	 * 获取 创建时间.
	 *
	 * @return 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置 创建时间.
	 *
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取 准备执行时间.
	 *
	 * @return 准备执行时间
	 */
	public Date getScheduleTime() {
		return scheduleTime;
	}

	/**
	 * 设置 准备执行时间.
	 *
	 * @param scheduleTime 准备执行时间
	 */
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	/**
	 * 获取 实际开始时间.
	 *
	 * @return 实际开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 设置 实际开始时间.
	 *
	 * @param startTime 实际开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获取 实际结束时间.
	 *
	 * @return 实际结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 设置 实际结束时间.
	 *
	 * @param endTime 实际结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取 0:未开始 1:成功 2:运行中 3:失败.
	 *
	 * @return 0:未开始 1:成功 2:运行中 3:失败
	 */
	public Integer getTaskResult() {
		return taskResult;
	}

	/**
	 * 设置 0:未开始 1:成功 2:运行中 3:失败.
	 *
	 * @param taskResult 0:未开始 1:成功 2:运行中 3:失败
	 */
	public void setTaskResult(Integer taskResult) {
		this.taskResult = taskResult;
	}

	/**
	 * 获取 执行结果信息.
	 *
	 * @return 执行结果信息
	 */
	public String getTaskMessage() {
		return taskMessage;
	}

	/**
	 * 设置 执行结果信息.
	 *
	 * @param taskMessage 执行结果信息
	 */
	public void setTaskMessage(String taskMessage) {
		this.taskMessage = taskMessage;
	}

	/**
	 * 获取 执行结果信息.
	 *
	 * @return 执行结果信息
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置 执行结果信息.
	 *
	 * @param status 执行结果信息
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

}
