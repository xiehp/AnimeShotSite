package xie.animeshotsite.db.repository;

import java.util.List;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.base.repository.BaseRepository;

public interface AutoRunParamDao extends BaseRepository<AutoRunParam, String> {

	List<AutoRunParam> findByAnimeInfoId(String animeInfoId);

	AutoRunParam findByAnimeInfoIdAndKey(String animeInfoId, String key);

	List<AutoRunParam> findByRefIdAndRefType(String refId, String refType);

	AutoRunParam findByRefIdAndRefTypeAndKey(String refId, String refType, String key);

}
