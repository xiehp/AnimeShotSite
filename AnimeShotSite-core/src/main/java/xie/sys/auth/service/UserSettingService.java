package xie.sys.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.sys.auth.entity.UserSetting;
import xie.sys.auth.repository.UserSettingRepository;

@Service
public class UserSettingService extends BaseService<UserSetting, String> {

	@Autowired
	private UserSettingRepository userSettingRepository;
	
	@Override
	public BaseRepository<UserSetting, String> getBaseRepository() {
		return userSettingRepository;
	}

	public UserSetting findByUserId(String currentUserId) {
		return userSettingRepository.findByUserId(currentUserId);
	}

}
