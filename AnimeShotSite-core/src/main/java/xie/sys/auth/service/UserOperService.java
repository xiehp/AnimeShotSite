package xie.sys.auth.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.sys.auth.entity.UserOper;
import xie.sys.auth.repository.UserOperRepository;

@Service
public class UserOperService extends BaseService<UserOper, String> {
	
	@Resource
	private UserOperRepository userOperRepository;

	@Override
	public BaseRepository<UserOper, String> getBaseRepository() {
		return userOperRepository;
	}}
