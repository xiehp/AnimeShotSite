package xie.sys.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import xie.base.repository.BaseRepository;
import xie.sys.auth.entity.UserRole;

public interface UserRoleRepository extends BaseRepository<UserRole, String>{
		
	@Query("select ur.roleId from UserRole ur where ur.userId=?1")
	List<String> findRoleIdByUserId(String id);
	
	List<UserRole> findByUserId(String userId);

}
