package xie.sys.auth.repository;

import xie.base.repository.BaseRepository;
import xie.sys.auth.entity.Role;

public interface RoleRepository extends BaseRepository<Role, String> {

	Role findByName(String name);

}
