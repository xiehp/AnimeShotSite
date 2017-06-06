package xie.animeshotsite.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.base.repository.BaseRepository;

public interface AutoRunParamDao extends BaseRepository<AutoRunParam, String> {

	List<AutoRunParam> findByAnimeInfoId(String animeInfoId);

	@Query(" select a from AutoRunParam a where a.animeInfoId = ?1 and a.animeEpisodeId is null and a.key = ?2")
	AutoRunParam findByAnimeInfoIdAndKey(String animeInfoId, String key);

	AutoRunParam findByAnimeEpisodeIdAndKey(String animeEpisodeId, String key);

	List<AutoRunParam> findByRefIdAndRefType(String refId, String refType);

	AutoRunParam findByRefIdAndRefTypeAndKey(String refId, String refType, String key);

	/**
	 * 找到监视flag和下载flag同时为1的数据
	 * @return
	 */
	@Query("select a from AutoRunParam a, AutoRunParam b where a.animeInfoId = b.animeInfoId and a.animeEpisodeId is not null and b.animeEpisodeId is null and  a.key = 'video_download_monitor_do_flag' and b.key = 'video_download_monitor_do_flag' and a.value = '1' and b.value = '1' ")
	List<AutoRunParam> findMonitorDonloadLUrlist();
}
