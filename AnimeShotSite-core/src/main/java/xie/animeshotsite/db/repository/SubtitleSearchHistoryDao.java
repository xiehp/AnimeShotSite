package xie.animeshotsite.db.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import xie.animeshotsite.db.entity.SubtitleSearchHistory;
import xie.base.repository.BaseRepository;

import java.util.HashMap;
import java.util.List;

@Transactional
public interface SubtitleSearchHistoryDao extends BaseRepository<SubtitleSearchHistory, String> {
  /** 获得搜索次数最多的20个 */
  @Query("select searchText, count(id) as searchCount, min(createDate) as firstDate, max(createDate) as createDate from SubtitleSearchHistory group by searchText order by searchCount desc ")
  List<Object[]> searchTop();

  /** 获得最近搜索的20个 */
  @Query("select searchText, count(id) as searchCount, min(createDate) as firstDate, max(createDate) as createDate from SubtitleSearchHistory group by searchText order by createDate desc ")
  List<Object[]> searchCurrent();

  /** 根据cookie id搜索的20个 */
  @Query("select searchText, count(id) as searchCount, min(createDate) as firstDate, max(createDate) as createDate from SubtitleSearchHistory where cookieId = ?1 group by searchText order by createDate desc ")
  List<Object[]> searchCurrentByCookieId(String cookieId);
}
