package xie.animeshotsite.db.repository;

import java.util.Date;
import java.util.List;

import xie.animeshotsite.db.entity.ShotTask;
import xie.base.repository.BaseRepository;

public interface ShotTaskDao extends BaseRepository<ShotTask, String> {

	List<ShotTask> findByTaskResultAndScheduleTimeLessThan(Integer taskResult, Date scheduleTime);

	List<ShotTask> findByTaskResultAndScheduleTimeIsNull(Integer taskResult);
}
