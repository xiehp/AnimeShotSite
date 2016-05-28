package xie.animeshotsite.db.repository.impl;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import xie.animeshotsite.db.entity.SubtitleLine;
import xie.base.repository.BaseRepositoryPlus;
import xie.common.Constants;
import xie.common.string.XStringUtils;

@Repository
public class SubtitleInfoDaoImpl extends BaseRepositoryPlus<SubtitleLine> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 
	 * @param animeName 动画名
	 * @param text 字幕文本
	 * @param pageRequest
	 * @return
	 */
	public Page<SubtitleLine> searchSubtitleLineByText(String animeName, String text, PageRequest pageRequest) {
		if (XStringUtils.isBlank(animeName) && XStringUtils.isBlank(text)) {
			return createEmptyPage(pageRequest);
		}

		String[] animeNameArray = animeName.split(" ");
		String[] textArray = text.split(" ");

		return searchSubtitleLineByText(animeNameArray, textArray, pageRequest);
	}

	public Page<SubtitleLine> searchSubtitleLineByText(String[] animeNameArray, String[] textArray, PageRequest pageRequest) {
		if (textArray == null || textArray.length == 0) {
			return createEmptyPage(pageRequest);
		}

		Map<String, Object> map = Maps.newHashMap();

		StringBuffer query = new StringBuffer(400);
		// select subtitle_line.* from subtitle_line, anime_episode where anime_episode.id = subtitle_line.ANIME_EPISODE_ID and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%逆转%' and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%裁判%'and
		// concat(anime_episode.FULL_NAME,subtitle_line.text) like '%成%'and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%堂%'and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%步%';

		query.append(" from SubtitleLine line,AnimeEpisode episode ");
		query.append(" where ");
		query.append(" line.animeEpisodeId = episode.id ");
		query.append(" and line.deleteFlag = " + Constants.FLAG_INT_NO);
		query.append(" and episode.deleteFlag = " + Constants.FLAG_INT_NO);

		for (int i = 0; i < animeNameArray.length; i++) {
			String animeName = animeNameArray[i];
			if (XStringUtils.isNotBlank(animeName)) {
				query.append(" and episode.fullName like :animeName" + i + " ");
				map.put("animeName" + i, "%" + animeName + "%");
			}
		}

		for (int i = 0; i < textArray.length; i++) {
			String text = textArray[i];
			if (XStringUtils.isNotBlank(text)) {
				query.append(" and line.text like :text" + i + " ");
				map.put("text" + i, "%" + text + "%");
			}
		}

		String countHql = "select count(distinct line.id) " + query.toString();
		String queryHql = "select distinct line " + query.toString() + " order by line.createDate desc";
		return getResult(queryHql, countHql, map, pageRequest);
	}

}
