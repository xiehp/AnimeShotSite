package xie.animeshotsite.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.repository.SubtitleInfoDao;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;

@Service
public class SubtitleInfoService extends BaseService<SubtitleInfo, String> {

	@Autowired
	private SubtitleInfoDao subtitleInfoDao;
	@Autowired
	private SubtitleLineDao subtitleLineDao;

	@Override
	public BaseRepository<SubtitleInfo, String> getBaseRepository() {
		return subtitleInfoDao;
	}

	/** 获取某动画下所有剧集的所有字幕 */
	public List<SubtitleInfo> findByAnimeInfoId(String animeInfoId) {
		return subtitleInfoDao.findByAnimeInfoIdOrderByLocalFileName(animeInfoId);
	}

	/** 获取某剧集下所有字幕 */
	public List<SubtitleInfo> findByAnimeEpisodeId(String animeEpisodeId) {
		return subtitleInfoDao.findByAnimeEpisodeIdOrderByLocalFileName(animeEpisodeId);
	}

}
