package xie.animeshotsite.db.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;

@Service
public class AutoRunParamService extends BaseService<AutoRunParam, String> {

	@Resource
	private AutoRunParamDao autoRunParamDao;

	@Override
	public BaseRepository<AutoRunParam, String> getBaseRepository() {
		return autoRunParamDao;
	}
}
