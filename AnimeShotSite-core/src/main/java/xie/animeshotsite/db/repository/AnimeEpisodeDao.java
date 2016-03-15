package xie.animeshotsite.db.repository;

import java.util.List;

import com.yjysh.framework.base.repository.BaseRepository;

import xie.animeshotsite.db.entity.AnimeEpisode;

public interface AnimeEpisodeDao extends BaseRepository<AnimeEpisode, String> {

	/** 获取所有需要进行截图的动画列表 */
	List<AnimeEpisode> findByProcessAction(Integer processAction);

	/** 获取某动画下所有剧集 */
	List<AnimeEpisode> findByAnimeInfoIdOrderBySort(String animeInfoId);
}
