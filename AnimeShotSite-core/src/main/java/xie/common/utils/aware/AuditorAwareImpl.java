package xie.common.utils.aware;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;

import xie.sys.auth.service.realm.ShiroRDbRealm.ShiroUser;

/** 
 * JPA AuditorAware实现
 * 实现获取当前系统操作的用户 名称
 *
 *  
 */
public class AuditorAwareImpl implements AuditorAware<String>{
	
	private Logger _log = LoggerFactory.getLogger(AuditorAwareImpl.class);

	/**
	 * 返回当前登陆用户的ID 
	 * @return 
	 * @see org.springframework.data.domain.AuditorAware#getCurrentAuditor() 
	 */
	public String getCurrentAuditor() {
		String userId = null;
		try{
			if(SecurityUtils.getSubject() == null || SecurityUtils.getSubject().getPrincipal() == null){
				userId = "未登录用户";
			}else{
				ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
				userId = user.id;
			}
		}catch(Exception e){
			userId = "未登录用户";
			_log.error("获取当前用户id失败！");
		}
		return userId;
	}
}

