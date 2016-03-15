package xie.animeshotsite.db.repository;

import java.util.Date;
import java.util.List;

import com.yjysh.framework.base.repository.BaseRepository;

import xie.animeshotsite.db.entity.ShotTask;

public interface ShotTaskDao extends BaseRepository<ShotTask, String> {

	List<ShotTask> findByTaskResultAndScheduleTimeGreaterThan(Integer taskResult, Date scheduleTime);

	List<ShotTask> findByTaskResultAndScheduleTimeIsNull(Integer taskResult);
}
