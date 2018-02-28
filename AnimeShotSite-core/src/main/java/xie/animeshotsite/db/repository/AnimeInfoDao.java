package xie.animeshotsite.db.repository;

import java.util.List;

import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.base.repository.BaseRepository;

public interface AnimeInfoDao extends BaseRepository<AnimeInfo, String> {

	/** 获取所有需要进行截图的动画列表 */
	List<AnimeInfo> findByProcessAction(Integer processAction);

	List<AnimeInfo> findBySeriesOrderBySort(String series);
}
