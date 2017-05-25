package xie.common.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xie.common.Constants;
import xie.module.spring.SpringUtils;
import xie.sys.dictionary.entity.PublicDictionary;
import xie.sys.dictionary.service.PublicDictionaryService;

public class DictionaryLoader {

	private static final Logger _log = LoggerFactory.getLogger(DictionaryLoader.class);
	
	protected Map<String,Map<String,String>> typeMap = new HashMap<String,Map<String,String>>();
	protected Map<String,Map<String, PublicDictionary>> typeEntityMap = new HashMap<String,Map<String, PublicDictionary>>();
	
	public static DictionaryLoader instance;
	
	public Map<String, Map<String, String>> getTypeMap() {
		return typeMap;
	}

	public Map<String, Map<String, PublicDictionary>> getTypeEntityMap() {
		return typeEntityMap;
	}
	
	public DictionaryLoader() {
		final PublicDictionaryService publicDictionaryService = SpringUtils.getBean(PublicDictionaryService.class);
		final Map<String,List<PublicDictionary>> dictionaryMap = publicDictionaryService.getAllDictionaryMap();
		
		for(final String typeId : dictionaryMap.keySet()){
			List<PublicDictionary> PublicDictionaryList = dictionaryMap.get(typeId);
			
			Map<String,String> map = new LinkedHashMap<String,String>();
			Map<String,PublicDictionary> vomap = new LinkedHashMap<String,PublicDictionary>();
			
			typeMap.put(typeId, map);
			typeEntityMap.put(typeId, vomap);
			
			for(final PublicDictionary PublicDictionary : PublicDictionaryList){
				map.put(PublicDictionary.getCode(), PublicDictionary.getValue());
				vomap.put(PublicDictionary.getCode(), PublicDictionary);
			}
		}
	}
	
	public static DictionaryLoader getInstance() {
    	if(instance == null) {
	        synchronized(DictionaryLoader.class){
	            if(instance == null) {
	                instance = new DictionaryLoader();
	            }
	        }
    	}
        return instance;
    }
	
	public static void reload(){
    	final PublicDictionaryService dictionaryBO = (PublicDictionaryService) SpringUtils.getBean(PublicDictionaryService.class);
    	_log.info("start load dictionary cache ........");
		dictionaryBO.reloadCache();
    	instance = null;
    	getInstance();
    	_log.info("finish load dictionary cache");
    }
	
	 public static void reload(final String typeId){
		 final PublicDictionaryService dictionaryBO = (PublicDictionaryService) SpringUtils.getBean(PublicDictionaryService.class);
			_log.info("start load dictionary "+typeId+" cache ........");
			final List<PublicDictionary> dictionaryList = dictionaryBO.findByTypeId(typeId);
			
			final Map<String,String> tempmap = new LinkedHashMap<String,String>();
			final Map<String,PublicDictionary> tempvomap = new LinkedHashMap<String,PublicDictionary>(); 
			for(final PublicDictionary PublicDictionary : dictionaryList){
				tempmap.put(PublicDictionary.getCode(), PublicDictionary.getValue());
				tempvomap.put(PublicDictionary.getCode(), PublicDictionary);
			}
			
			final Map<String,String> strMap = DictionaryLoader.getInstance().getMap(typeId);
			final Map<String,PublicDictionary> voMap = DictionaryLoader.getInstance().getEntityMap(typeId);
			
			if(strMap == null){
				DictionaryLoader.getInstance().getTypeMap().put(typeId, tempmap);
			} else{
				strMap.clear();
				strMap.putAll(tempmap);
			}
			
			if(voMap == null){
				DictionaryLoader.getInstance().getTypeEntityMap().put(typeId, tempvomap);
			} else{
				voMap.clear();
				voMap.putAll(tempvomap);
			}

	    	_log.info("finish load dictionary "+typeId+" cache");
	    }
	    
		public Map<String,String> getMap(final String typeId)  {
			return typeMap.get(typeId);
	    }

		public Map<String,PublicDictionary> getEntityMap(final String typeId)  {
			return typeEntityMap.get(typeId);
	    }
		
		public Map<String,String> geValidtMap(final String typeId)  {
			final Map<String,PublicDictionary> vomap = getValidMapEntity(typeId);
			final Map<String,String> map = new LinkedHashMap<String,String>();
			for(final String code : vomap.keySet()){
				final PublicDictionary entity = vomap.get(code);
				map.put(code, entity.getValue());
			}
			return map;
	    }

		public Map<String,PublicDictionary> getValidMapEntity(final String typeId)  {
			final Map<String,PublicDictionary> vomap = getEntityMap(typeId);
			final Map<String,PublicDictionary> map = new LinkedHashMap<String,PublicDictionary>();
			for(final String code : vomap.keySet()){
				final PublicDictionary entity = vomap.get(code);
				if(entity.getDeleteFlag().equals(Constants.FLAG_INT_NO)){
					map.put(code, entity);
				}
			}
			return map;
	    }
	
}
