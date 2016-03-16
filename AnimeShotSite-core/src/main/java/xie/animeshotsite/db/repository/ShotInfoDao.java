package xie.animeshotsite.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.base.repository.BaseRepository;

public interface ShotInfoDao extends BaseRepository<ShotInfo, String> {

	ShotInfo findByAnimeEpisodeIdAndTimeStamp(String animeEpisodeId, Long timeStamp);

	@Query(value = "from ShotInfo where animeEpisodeId = ?1 and timeStamp = (select MAX(s.timeStamp) from ShotInfo s where s.animeEpisodeId = ?1 and s.timeStamp < ?2)")
	ShotInfo findPrevious(String animeInfoId, Long timeStamp);

	@Query(value = "from ShotInfo where animeEpisodeId = ?1 and timeStamp = (select MIN(s.timeStamp) from ShotInfo s where s.animeEpisodeId = ?1 and s.timeStamp > ?2)")
	ShotInfo findNext(String animeInfoId, Long timeStamp);

	@Query(nativeQuery = true, value = "select * from " + ShotInfo.TABLE_NAME + " limit ?1,?2")
	List<ShotInfo> findRandom(Integer from, Integer number);
}
