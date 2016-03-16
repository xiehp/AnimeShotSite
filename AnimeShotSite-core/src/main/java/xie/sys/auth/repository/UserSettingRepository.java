package xie.sys.auth.repository;

import xie.base.repository.BaseRepository;
import xie.sys.auth.entity.UserSetting;


public interface UserSettingRepository extends BaseRepository<UserSetting, String> {

	UserSetting findByUserId(String currentUserId);

}
