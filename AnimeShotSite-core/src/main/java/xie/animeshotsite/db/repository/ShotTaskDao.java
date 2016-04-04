package xie.animeshotsite.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import xie.animeshotsite.db.entity.ShotTask;
import xie.base.repository.BaseRepository;

public interface ShotTaskDao extends BaseRepository<ShotTask, String> {

	@Query(value = " from ShotTask where id <> ?1 and taskResult = ?2 and scheduleTime < ?3")
	List<ShotTask> findByTaskResultAndScheduleTimeLessThan(String randomId, Integer taskResult, Date nowDate);

	@Query(value = " from ShotTask where id <> ?1 and taskResult = ?2 and scheduleTime is null")
	List<ShotTask> findByTaskResultAndScheduleTimeIsNull(String randomId, Integer taskResult);
}
