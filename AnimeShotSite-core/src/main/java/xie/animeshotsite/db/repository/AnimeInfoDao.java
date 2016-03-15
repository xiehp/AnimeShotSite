package xie.animeshotsite.db.repository;

import java.util.List;

import com.yjysh.framework.base.repository.BaseRepository;

import xie.animeshotsite.db.entity.AnimeInfo;

public interface AnimeInfoDao extends BaseRepository<AnimeInfo, String> {

	/** 获取所有需要进行截图的动画列表 */
	List<AnimeInfo> findByProcessAction(Integer processAction);
}
