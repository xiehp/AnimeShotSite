package xie.common.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Maps;

import xie.module.spring.SpringUtils;
import xie.sys.auth.entity.Resource;
import xie.sys.auth.repository.ResourceRepository;

public class ResourceLoader {
	
	private final static Map<String, Resource> identityMap = Maps.newHashMap();
	private final static Map<String, Resource> urlMap = Maps.newHashMap();
	private final static Map<String, Resource> idMap = Maps.newHashMap();
	
	private static ResourceLoader instance;

	private ResourceLoader() {
		initAllResource();
	}
	
	private void initAllResource(){
		ResourceRepository resourceRepository = SpringUtils.getBean(ResourceRepository.class);
		List<Resource> allResourceList = resourceRepository.findAll();
		for (Resource resource : allResourceList) {
			identityMap.put(resource.getIdentity(), resource);
			idMap.put(resource.getId(), resource);
			if(!StringUtils.isEmpty(resource.getUrl())) {
				urlMap.put(resource.getUrl(), resource);
			}
		}
	}
	
	public Map<String, Resource> getIdentityMap(){
		return identityMap;
	}
	public Map<String, Resource> getIdMap(){
		return idMap;
	}
	public Map<String, Resource> getUrlMap(){
		return urlMap;
	}

	public static ResourceLoader getInstance() {
    	if(instance == null) {
	        synchronized(ResourceLoader.class){
	            if(instance == null) {
	                instance = new ResourceLoader();
	            }
	        }
    	}
        return instance;
    }
	
	public void reload(){
		identityMap.clear();
		idMap.clear();
		urlMap.clear();
		initAllResource();
	}
	
}
