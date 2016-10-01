package xie.animeshotsite.db.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.base.repository.BaseRepositoryPlus;
import xie.common.Constants;
import xie.common.string.XStringUtils;

@Repository
public class SubtitleLineDaoImpl extends BaseRepositoryPlus<SubtitleLine> {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private SubtitleLineDao subtitleLineDao;

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

		return searchSubtitleLineByTextV2(animeNameArray, textArray, pageRequest);
	}

	@Deprecated
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

	public Page<SubtitleLine> searchSubtitleLineByTextV2(String[] animeNameArray, String[] textArray, PageRequest pageRequest) {
		List<String> animeNameList = new ArrayList<>();
		if (animeNameArray != null) {
			for (String s : animeNameArray) {
				if (XStringUtils.isNotBlank(s)) {
					animeNameList.add(s.trim());
				}
			}
		}

		List<String> textList = new ArrayList<>();
		if (textArray != null) {
			for (String s : textArray) {
				if (XStringUtils.isNotBlank(s)) {
					textList.add(s.trim());
				}
			}
		}

		if (textArray == null || textArray.length == 0) {
			// return createEmptyPage(pageRequest);
		}

		Map<String, Object> map = Maps.newHashMap();

		StringBuffer query = new StringBuffer(400);
		// select subtitle_line.* from subtitle_line, anime_episode where anime_episode.id = subtitle_line.ANIME_EPISODE_ID and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%逆转%' and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%裁判%'and
		// concat(anime_episode.FULL_NAME,subtitle_line.text) like '%成%'and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%堂%'and concat(anime_episode.FULL_NAME,subtitle_line.text) like '%步%';

		query.append(" from SubtitleLine line, AnimeEpisode episode ");
		query.append(" where ");
		query.append(" line.animeEpisodeId = episode.id and ");

		boolean textAndFlg = false;
		boolean animeNameAndFlg = false;

		if (animeNameList != null && animeNameList.size() > 0) {
			query.append(" line.animeEpisodeId in (");

			query.append(" select id from AnimeEpisode where ");

			for (int i = 0; i < animeNameList.size(); i++) {
				String animeName = animeNameList.get(i);
				if (animeNameAndFlg) {
					query.append(" and ");
				}
				query.append(" fullName like :animeName" + i + " ");
				map.put("animeName" + i, "%" + animeName + "%");
				animeNameAndFlg = true;
				textAndFlg = true;
			}
			if (animeNameAndFlg) {
				query.append(" and ");
			}
			query.append(" deleteFlag = " + Constants.FLAG_INT_NO);
			textAndFlg = true;

			query.append(" ) ");
		}

		if (textList != null && textList.size() > 0) {
			for (int i = 0; i < textList.size(); i++) {
				String text = textList.get(i);
				if (textAndFlg) {
					query.append(" and ");
				}
				query.append(" line.text like :text" + i + " ");
				map.put("text" + i, "%" + text + "%");
				textAndFlg = true;
			}
		}

		if (textAndFlg) {
			query.append(" and ");
		}
		query.append(" line.deleteFlag = " + Constants.FLAG_INT_NO);

		// String countHql = "select count(distinct line.id) " + query.toString();
		// String queryHql = "select distinct line " + query.toString() + " order by line.createDate desc";
		String countHql = "select count( line.id) " + query.toString();
		String queryHql = "select line.id " + query.toString() + " order by episode.sort,line.startTime asc";

		Page<String> page = geIdResult(queryHql, countHql, map, pageRequest);
		List<SubtitleLine> list = new ArrayList<>();
		for (String id : page.getContent()) {
			list.add(subtitleLineDao.findOne(id));
		}
		return new PageImpl<SubtitleLine>(list, pageRequest, page.getTotalElements());
	}
}
