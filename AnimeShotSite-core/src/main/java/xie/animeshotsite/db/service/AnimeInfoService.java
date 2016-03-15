package xie.animeshotsite.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yjysh.framework.base.repository.BaseRepository;
import com.yjysh.framework.base.service.BaseService;

import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.repository.AnimeInfoDao;

@Service
public class AnimeInfoService extends BaseService<AnimeInfo, String> {

	@Autowired
	private AnimeInfoDao animeInfoDao;

	@Override
	public BaseRepository<AnimeInfo, String> getBaseRepository() {
		return animeInfoDao;
	}

	/** 获取所有需要进行截图的动画列表 */
	public List<AnimeInfo> findByProcessAction(Integer processAction) {
		return animeInfoDao.findByProcessAction(processAction);
	}
}
