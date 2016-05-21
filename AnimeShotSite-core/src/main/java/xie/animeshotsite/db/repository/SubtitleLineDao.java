package xie.animeshotsite.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import xie.animeshotsite.db.entity.SubtitleLine;
import xie.base.repository.BaseRepository;

@Transactional
public interface SubtitleLineDao extends BaseRepository<SubtitleLine, String> {

	@Query(value = "from SubtitleLine where subtitleInfoId = ?1 order by startTime, layer, lineIndex)")
	List<SubtitleLine> findBySubtitleInfoId(String subtitleInfoId);

	@Query(value = "from SubtitleLine where subtitleInfoId in (?1) order by startTime, layer, lineIndex)")
	List<SubtitleLine> findBySubtitleInfoId(List<String> subtitleInfoIdList);

	SubtitleLine findBySubtitleInfoIdAndLineIndex(String subtitleInfoId, Integer lineIndex);

	/**
	 * 精确查找一张截图对应的所有字幕
	 * 
	 * @param animeEpisodeId
	 * @param startTime
	 * @return
	 */
	@Query(value = "from SubtitleLine where animeEpisodeId = ?1 and (startTime <= ?2 or endTime >= ?2) order by startTime, layer, lineIndex)")
	List<SubtitleLine> findByTime(String animeEpisodeId, Long timeStamp );

	/**
	 * 查找某段时间内的字幕<br>
	 * 1,2 AAA<br>
	 * 2,3 BBB<br>
	 * 2,6 CCC<br>
	 * 3,4 DDD<br>
	 * 4,4.5 E<br>
	 * 5,6 F<br>
	 * 6,7 G<br>
	 * 
	 * 3,5 -> B C D E F<br>
	 * 
	 * @param animeEpisodeId
	 * @param startTime
	 * @param startTime
	 * @return
	 */
	@Query(value = "from SubtitleLine where animeEpisodeId = ?1 and NOT(endTime < ?2 OR startTime > ?3) order by startTime, layer, lineIndex)")
	List<SubtitleLine> findByTime(String animeEpisodeId, Long startTime, Long endTime);

	int countBySubtitleInfoId(String subtitleInfoId);

	void deleteBySubtitleInfoId(String subtitleInfoId);
}
