package xie.animeshotsite.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;

@Service
public class AutoRunParamService extends BaseService<AutoRunParam, String> {

	@Autowired
	private AutoRunParamDao autoRunParamDao;

	@Override
	public BaseRepository<AutoRunParam, String> getBaseRepository() {
		return autoRunParamDao;
	}
}
