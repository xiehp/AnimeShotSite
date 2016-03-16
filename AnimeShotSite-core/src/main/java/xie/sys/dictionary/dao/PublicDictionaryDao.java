package xie.sys.dictionary.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import xie.sys.dictionary.entity.PublicDictionary;

public interface PublicDictionaryDao extends PagingAndSortingRepository<PublicDictionary, String>, JpaSpecificationExecutor<PublicDictionary>  {

	List<PublicDictionary> findByTypeId(String typeId, Sort sort);

}
