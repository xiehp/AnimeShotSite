package xie.animeshotsite.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import xie.animeshotsite.db.entity.GifInfo;
import xie.base.repository.BaseRepository;

public interface GifInfoDao extends BaseRepository<GifInfo, String> {

	GifInfo findByAnimeEpisodeIdAndTimeStamp(String animeEpisodeId, Long timeStamp);

	/**
	 * 获得某个时间戳之前的一条记录
	 */
	@Query(value = "from GifInfo where createDate = (select MAX(s.createDate) from GifInfo s where createDate < ?1)")
	GifInfo findPrevious(Date createDate);

	/**
	 * 获得某个时间戳之后的一条记录
	 */
	@Query(value = "from GifInfo where createDate = (select MIN(s.createDate) from GifInfo s where createDate > ?1)")
	GifInfo findNext(Date createDate);

	/**
	 * 随机获取一些记录
	 */
	@Query(nativeQuery = true, value = "select * from " + GifInfo.TABLE_NAME + " limit ?1,?2")
	List<GifInfo> findRandom(Integer from, Integer number);

	/**
	 * 获得某个截图在剧集中的位置
	 */
	@Query("SELECT COUNT(id) FROM GifInfo where animeEpisodeId = ?1 and timeStamp <= ?2 and deleteFlag = ?3 order by timeStamp")
	int getRowNumber(String animeEpisodeId, long timeStamp, int deleteFlag);
}
