package xie.sys.auth.service;

import org.springframework.stereotype.Service;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.sys.auth.entity.UserSetting;
import xie.sys.auth.repository.UserSettingRepository;

import javax.annotation.Resource;

@Service
public class UserSettingService extends BaseService<UserSetting, String> {

	@Resource
	private UserSettingRepository userSettingRepository;
	
	@Override
	public BaseRepository<UserSetting, String> getBaseRepository() {
		return userSettingRepository;
	}

	public UserSetting findByUserId(String currentUserId) {
		return userSettingRepository.findByUserId(currentUserId);
	}

}
