package xie.animeshotsite.db.repository;

import java.util.List;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.base.repository.BaseRepository;

public interface AnimeEpisodeDao extends BaseRepository<AnimeEpisode, String> {

	/** 获取所有需要进行截图的动画列表 */
	List<AnimeEpisode> findByProcessAction(Integer processAction);

	/** 获取某动画下所有剧集 */
	List<AnimeEpisode> findByAnimeInfoIdOrderBySort(String animeInfoId);

	long countByShowFlgAndDeleteFlag(Integer showFlg, Integer deleteFlag);

	/** 根据唯一标识符获得某一集剧集 */
	AnimeEpisode findByAnimeInfoIdAndNumber(String animeInfoId, String string);

}
