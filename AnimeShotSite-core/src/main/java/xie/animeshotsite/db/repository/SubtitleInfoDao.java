package xie.animeshotsite.db.repository;

import java.util.List;

import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.base.repository.BaseRepository;

public interface SubtitleInfoDao extends BaseRepository<SubtitleInfo, String> {

	/** 获取某动画下所有剧集的所有字幕信息 */
	List<SubtitleInfo> findByAnimeInfoIdOrderByLocalFileName(String animeInfoId);

	/** 获取某剧集下所有字幕信息 */
	List<SubtitleInfo> findByAnimeEpisodeIdOrderByLocalFileName(String animeEpisodeId);

	/** 获取某个剧集某语言的字幕信息 */
	SubtitleInfo findByAnimeInfoIdAndAnimeEpisodeIdAndLanguage(String animeInfoId, String animeEpisodeId, String language);

}
