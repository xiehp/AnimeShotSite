package xie.sys.dictionary.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import xie.sys.dictionary.dao.PublicDictionaryDao;
import xie.sys.dictionary.entity.PublicDictionary;

@Service
public class PublicDictionaryService {
	
	private volatile  Map<String,List<PublicDictionary>> dictionaryMap;

	@Autowired
	private PublicDictionaryDao publicDictionaryDao;
	
	public Map<String, List<PublicDictionary>> getAllDictionaryMap(){
		buildMap();
		return dictionaryMap;
	}
	
	public void reloadCache(){
		dictionaryMap = null;
		buildMap();
	}
	
	public List<PublicDictionary> findByTypeId(final String typeId) {
		Sort sort = new Sort(Direction.ASC, "sort");
		return publicDictionaryDao.findByTypeId(typeId, sort);
	}
	
	private void buildMap(){
		if(dictionaryMap==null){
			synchronized(PublicDictionaryService.class){
				if(dictionaryMap==null){
					dictionaryMap = new LinkedHashMap<String,List<PublicDictionary>>();
					Iterable<PublicDictionary> publicDictionaryList = publicDictionaryDao.findAll();
			        final Iterator<PublicDictionary> iter =  publicDictionaryList.iterator();
			        while(iter.hasNext()){
			            final PublicDictionary publicDictionary = (PublicDictionary)iter.next();
			            List<PublicDictionary> pkList = (List<PublicDictionary>) dictionaryMap.get(publicDictionary.getTypeId());
			            if(pkList==null){
			            	pkList = new ArrayList<PublicDictionary>();
			                dictionaryMap.put(publicDictionary.getTypeId(),pkList);
			            }
			            
			            pkList.add(publicDictionary);
			        }
				
				}
			}
		}
	}
}
