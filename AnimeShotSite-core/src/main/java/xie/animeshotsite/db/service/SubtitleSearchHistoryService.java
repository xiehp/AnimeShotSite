package xie.animeshotsite.db.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.SubtitleSearchHistory;
import xie.animeshotsite.db.repository.SubtitleSearchHistoryDao;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;

@Service
public class SubtitleSearchHistoryService extends BaseService<SubtitleSearchHistory, String> {

	@Resource
	private SubtitleSearchHistoryDao subtitleSearchHistoryDao;

	@Override
	public BaseRepository<SubtitleSearchHistory, String> getBaseRepository() {
		return subtitleSearchHistoryDao;
	}

	public SubtitleSearchHistory saveHistory(String keyword, int count, String cookieId, String animeInfoId, String animeEpisodeId) {
		SubtitleSearchHistory history = new SubtitleSearchHistory();
		history.setSearchText(keyword);
		history.setSearchCount(count);
		history.setCookieId(cookieId);

		history.setAnimeInfoId(animeInfoId);
		history.setAnimeEpisodeId(animeEpisodeId);
		return subtitleSearchHistoryDao.save(history);
	}

	public List<SubtitleSearchHistory> searchCurrentTop(int limit) {
		List<Object[]> list = subtitleSearchHistoryDao.searchCurrent();
		return getSubtitleSearchHistories(list);
	}

	public List<SubtitleSearchHistory> searchCurrentByCookieId(int limit, String sessionId) {
		List<Object[]> list = subtitleSearchHistoryDao.searchCurrentByCookieId(sessionId);
		return getSubtitleSearchHistories(list);
	}

	private List<SubtitleSearchHistory> getSubtitleSearchHistories(List<Object[]> list) {
		List<SubtitleSearchHistory> listHistory = new ArrayList<>();

		int count = list.size() > 100 ? 100 : list.size();
		for (int i = 0; i < count; i++) {
			Object[] x = list.get(i);
			SubtitleSearchHistory history = new SubtitleSearchHistory();
			history.setSearchText((String) x[0]);
			history.setSearchCount(((Long) x[1]).intValue());
			history.setFirstDate((Date) x[2]);
			history.setCreateDate((Date) x[3]);
			listHistory.add(history);
		}

		return listHistory;
	}

	public List<SubtitleSearchHistory> searchTop(int limit) {
		List<Object[]> list = subtitleSearchHistoryDao.searchTop();
		return getSubtitleSearchHistories(list);
	}
}
