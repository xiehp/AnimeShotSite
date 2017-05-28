package com.yjysh.framework.user.service;

import java.util.List;

import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.yjysh.framework.common.Constants;
import com.yjysh.framework.spring.SpringContextTestCase;
import com.yjysh.framework.sys.auth.entity.User;
import com.yjysh.framework.sys.auth.entity.UserSetting;
import com.yjysh.framework.sys.auth.repository.UserRepository;
import com.yjysh.framework.sys.auth.repository.UserSettingRepository;
@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext.xml","/applicationContext-framework.xml","/projectContext.xml","/deployContext.xml" })
public class UserServiceTest  extends SpringContextTestCase {

	@javax.annotation.Resource
	private UserRepository userRepository;
	@javax.annotation.Resource
	private UserSettingRepository userSettingRepository;
	
	@Test
	public void test(){
		List<User> userList = userRepository.findAll();
		for (User user : userList) {
			if(user.getUserType() != Constants.USER_TYPE_BACKGROUND) continue;
			UserSetting setting = new UserSetting();
			setting.setShowSidebar(Constants.FLAG_INT_YES);
			setting.setSkinPath(Constants.SKIN_DEFAULT);
			setting.setUserId(user.getId());
			userSettingRepository.save(setting);
		}
	}
}
