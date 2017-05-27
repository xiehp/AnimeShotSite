package xie.animeshotsite.db.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.base.repository.BaseRepository;

public interface ShotInfoDao extends BaseRepository<ShotInfo, String> {

	ShotInfo findByAnimeEpisodeIdAndTimeStamp(String animeEpisodeId, Long timeStamp);

	/**
	 * 获得某个时间戳之前的一条记录
	 */
	@Query(value = "from ShotInfo where animeEpisodeId = ?1 and timeStamp = (select MAX(s.timeStamp) from ShotInfo s where s.animeEpisodeId = ?1 and s.timeStamp < ?2)")
	ShotInfo findPrevious(String animeInfoId, Long timeStamp);

	/**
	 * 获得某个时间戳之后的一条记录
	 */
	@Query(value = "from ShotInfo where animeEpisodeId = ?1 and timeStamp = (select MIN(s.timeStamp) from ShotInfo s where s.animeEpisodeId = ?1 and s.timeStamp > ?2)")
	ShotInfo findNext(String animeInfoId, Long timeStamp);

	/**
	 * 随机获取一些记录
	 */
	@Query(nativeQuery = true, value = "select * from " + ShotInfo.TABLE_NAME + " limit ?1,?2")
	List<ShotInfo> findRandom(Integer from, Integer number);

	/**
	 * 指定剧集中随机获取一些记录
	 */
	@Query(nativeQuery = true, value = "select * from " + ShotInfo.TABLE_NAME + " limit ?1,?2")
	List<ShotInfo> findRandomByAnimeEpisodeId(String animeEpisodeId, int from, int number);

	/**
	 * 获得某个截图在剧集中的位置
	 */
	@Query("SELECT COUNT(id) FROM ShotInfo where animeEpisodeId = ?1 and timeStamp <= ?2 and deleteFlag = ?3 order by timeStamp")
	int getRowNumber(String animeEpisodeId, long timeStamp, int deleteFlag);

	/**
	 * 获得剧集分组后，每剧集的图片数量
	 * 
	 * @return animeInfoId, animeEpisodeId, count, maxTimeStamp
	 */
	@Query("select animeInfoId, animeEpisodeId, count(animeEpisodeId) as count, max(timeStamp) as maxTimeStamp from ShotInfo where animeInfoId = ?1 group by animeEpisodeId")
	List<Object[]> countRowNumberGroupByAnimeEpisodeId(String animeInfoId);

	/**
	 * 获得剧集的图片数量
	 */
	int countByAnimeEpisodeId(String animeEpisodeId);

}
