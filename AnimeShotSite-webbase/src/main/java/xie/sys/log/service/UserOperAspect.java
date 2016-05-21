package xie.sys.log.service;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;

import xie.base.entity.BaseEntity;
import xie.base.vo.BaseVo;
import xie.common.Constants;
import xie.common.utils.IpUtils;
import xie.common.utils.props.PropsKeys;
import xie.common.utils.props.PropsUtil;
import xie.common.web.util.RequestUtil;
import xie.sys.auth.entity.UserOper;
import xie.sys.auth.service.UserOperService;

@Aspect
@Component
public class UserOperAspect {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Integer logFlag = Integer.valueOf(PropsUtil.getProperty(PropsKeys.SYSTEM_LOG_FLAG));
	
	private final List<String> baseClassType = Lists.newArrayList("java.lang.String","java.lang.Integer",
			"java.lang.Float",
			"java.lang.Boolean",
			"java.lang.StringBuffer",
			"java.lang.Long",
			"java.lang.Byte",
			"java.lang.Character"
			);
	
	
	
	@Autowired
	private UserOperService userOperService;
	
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void controllerAspect(){
		
	}
	
	@Before("controllerAspect()")
	public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException{
		
		if(Constants.FLAG_INT_YES.equals(logFlag)) {
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.
					getRequestAttributes()).getRequest();
			String ipAddress = IpUtils.getIpAddr(request);
			Object[] arguments = joinPoint.getArgs();
			
			Class targetClass = Class.forName(className);
			Method[] methods = targetClass.getMethods();
	        String url = "";
	        String resourceName="";
	         for (Method method : methods) {
	             if (method.getName().equals(methodName)) {
	                Class[] clazzs = method.getParameterTypes();
	              
	                 if (clazzs.length == arguments.length) {
	                	//先获取类上的RequestMapping注解的value值
	                	url = getControllerRequestMappingValue(className);
	                	//再拼方法上的requestMapping注解的value值,这样得到一个完整的资源url请求
	                	url += method.getAnnotation(RequestMapping.class).value()[0];
	                	//可能会在拼接的时候url出现两个//,需要去掉一个
	                	url=url.replace("//", "/");
	                    break;
	                }
	            }
	        }
	        
			String params = getParams(arguments);
			UserOper userOper = new UserOper();
			userOper.setClassName(className);
			userOper.setMethodName(methodName);
			userOper.setIpAddress(ipAddress);
			userOper.setUrl(url);
			userOper.setParams(params);
			userOperService.save(userOper);
		}
	}
	
	private String getControllerRequestMappingValue(String controllerName){
		try {
			return Class.forName(controllerName).getAnnotation(RequestMapping.class).value()[0]+"/";
		} catch (Exception e) {
			return "";
		}
	}
	
	private String getParams(Object[] arguments){
			StringBuffer params = new StringBuffer();
	        for (Object obj : arguments) {
	        	
	        	if(obj == null) {
	        		continue;
	        	}
	        	
				if(baseClassType.contains(obj.getClass().getName())){
					params.append("_"+obj+",");
				}
				if(obj instanceof HttpServletRequest){
					params.append("_"+RequestUtil.getAllParams((HttpServletRequest)obj)+",");
				}
				
				if(obj instanceof BaseEntity){
					params.append("_"+obj+",");
				}
				
				if(obj instanceof BaseVo){
					params.append("_"+obj+",");
				}
			}
	        
	        if(params.length()>1024){
	        	String date_ = Long.valueOf(new Date().getTime()).toString();
	        	log.info("{}:{}",date_,params);
	        	return "请求参数过大，请参考日志文件，查看标识"+date_;
	        }else{
	        	return params.toString();
	        }
	}
	

}
