package xie.sys.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import xie.base.repository.BaseRepository;
import xie.sys.auth.entity.User;

public interface UserRepository extends BaseRepository<User, String> {

	User findByLoginNameAndDeleteFlag(String loginName, Integer deleteFlag);
	
	@Query(value="select distinct u.loginName from User u where u.loginName like %?1%")
	List<String> getLoginNameList(String loginName);
}
