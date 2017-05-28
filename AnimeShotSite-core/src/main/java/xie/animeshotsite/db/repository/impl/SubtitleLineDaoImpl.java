package xie.animeshotsite.db.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.base.page.PageRequestUtil;
import xie.base.repository.BaseRepositoryPlus;
import xie.common.Constants;
import xie.common.string.XStringUtils;
import xie.module.language.XLanguageUtils;

@Repository
public class SubtitleLineDaoImpl extends BaseRepositoryPlus<SubtitleLine> {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private SubtitleLineDao subtitleLineDao;

	@Resource
	private AnimeEpisodeDaoImpl animeEpisodeDaoImpl;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 
	 * @param searchMode true:精确检索, null或false:全文检索
	 * @param animeName 动画名
	 * @param text 字幕文本
	 * @param pageRequest
	 * @return
	 */
	public Page<SubtitleLine> searchSubtitleLineByText(Boolean searchMode, String animeName, String text, PageRequest pageRequest) {
		if (XStringUtils.isBlank(animeName) && XStringUtils.isBlank(text)) {
			return createEmptyPage(pageRequest);
		}

		if (animeName == null) {
			animeName = "";
		}
		if (text == null) {
			text = "";
		}

		// return searchSubtitleLineByTextV2(animeNameArray, textArray, pageRequest);
		if ((searchMode == null || searchMode == false) && XStringUtils.isNotBlank(text)) {
			// 搜索类型为模糊搜索，并且搜索文本不为空，执行全文检索
			String[] animeNameArray = animeName.split(" ");
			String newText = XLanguageUtils.chineseFullTextChange(text);
			String[] textArray = newText.split(" ");
			return searchSubtitleLineByFullText(animeNameArray, newText, textArray, pageRequest);
		} else {
			String[] animeNameArray = animeName.split(" ");
			String[] textArray = text.split(" ");
			return searchSubtitleLineByTextV3(animeNameArray, textArray, pageRequest);
		}
	}

	/**
	 * 精确地搜索数据库
	 * 
	 * @param animeNameArray
	 * @param textArray
	 * @param pageRequest
	 * @return
	 */
	private Page<SubtitleLine> searchSubtitleLineByTextV2(String[] animeNameArray, String[] textArray, PageRequest pageRequest) {
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

		Page<String> page = getResult(queryHql, countHql, map, pageRequest);
		List<SubtitleLine> list = new ArrayList<>();
		for (String id : page.getContent()) {
			list.add(subtitleLineDao.findOne(id));
		}
		return new PageImpl<SubtitleLine>(list, pageRequest, page.getTotalElements());
	}

	/**
	 * 精确地搜索数据库,先搜episode，再搜subtitleLine
	 * 
	 * @param animeNameArray
	 * @param textArray
	 * @param pageRequest
	 * @return
	 */
	private Page<SubtitleLine> searchSubtitleLineByTextV3(String[] animeNameArray, String[] textArray, PageRequest pageRequest) {
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

		Map<String, Object> map = Maps.newHashMap();

		StringBuffer query = new StringBuffer(800);

		// 剧集全称sql部分
		if (animeNameList != null && animeNameList.size() > 0) {
			Page<String> animeEpisodeIdList = animeEpisodeDaoImpl.searchEpisodeIdByFullName(animeNameArray, null);
			if (animeEpisodeIdList.getContent().size() == 0) {
				return createEmptyPage(pageRequest);
			}

			query.append(" from AnimeEpisode episode, SubtitleLine line ");
			query.append(" where ");
			query.append(" line.animeEpisodeId in :animeEpisodeIdList");
			map.put("animeEpisodeIdList", animeEpisodeIdList.getContent());

			query.append(" and line.animeEpisodeId = episode.id ");
		} else {
			query.append(" from SubtitleLine line, AnimeEpisode episode ");
			query.append(" where ");
			query.append(" line.animeEpisodeId = episode.id ");
		}

		// 字幕文本sql部分
		query.append(" and line.deleteFlag = " + Constants.FLAG_INT_NO);
		if (textList != null && textList.size() > 0) {
			for (int i = 0; i < textList.size(); i++) {
				String text = textList.get(i);
				query.append(" and line.text like :text" + i + " ");
				map.put("text" + i, "%" + text + "%");
			}
		}

		String countHql = "select count(line.id) " + query.toString();
		String queryHql = "select line.id " + query.toString() + " order by episode.sort,line.startTime asc";
		Page<String> page = null;
		synchronized (this) {
			page = getResult(queryHql, countHql, map, pageRequest);
		}
		List<SubtitleLine> list = new ArrayList<>();
		for (String id : page.getContent()) {
			list.add(subtitleLineDao.findOne(id));
		}

		return new PageImpl<SubtitleLine>(list, pageRequest, page.getTotalElements());
	}

	/**
	 * 全文检索方式进行搜索
	 * 
	 * @param animeNameArray
	 * @param text
	 * @param textArray
	 * @param pageRequest
	 * @return
	 */
	private Page<SubtitleLine> searchSubtitleLineByFullText(String[] animeNameArray, String text, String[] textArray, PageRequest pageRequest) {
		if (XStringUtils.isBlank(text)) {
			return createEmptyPage(pageRequest);
		}

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

		Map<String, Object> map = Maps.newHashMap();

		StringBuffer query = new StringBuffer(800);

		query.append(" from " + SubtitleLine.TABLE_NAME + " line ");
		query.append(" where 1=1 ");

		// 字幕文本sql部分
		// 提高搜索速度，去掉该sql query.append(" and line.delete_Flag = " + Constants.FLAG_INT_NO);
		query.append(" and MATCH (line.text) AGAINST (:text)");
		map.put("text", text);

		// 剧集全称sql部分
		if (animeNameList != null && animeNameList.size() > 0) {
			Page<String> animeEpisodeIdList = animeEpisodeDaoImpl.searchEpisodeIdByFullName(animeNameArray, null);
			if (animeEpisodeIdList.getContent().size() == 0) {
				return createEmptyPage(pageRequest);
			}

			query.append(" and line.anime_Episode_Id in (:animeEpisodeIdList)");
			map.put("animeEpisodeIdList", animeEpisodeIdList.getContent());
		}

		String countHql = "select count(line.id) " + query.toString();
		String queryHql = "select line.id " + query.toString();
		// 全文检索默认按照相关度排序
		// queryHql += " order by MATCH (line.text) AGAINST (:text) desc";

		// 20161003 超过2000页现在查询会极慢，一般100页即1000条数据应该已经完全可以满足全文检索的要求，因此最多搜索100页
		int maxSearchPage = 100;
		if (pageRequest.getPageNumber() > maxSearchPage) {
			pageRequest = PageRequestUtil.buildPageRequest(maxSearchPage, pageRequest.getPageSize(), pageRequest.getSort());
		}

		Page<String> page = getResultNativeSql(queryHql, countHql, map, pageRequest);
		List<SubtitleLine> list = new ArrayList<>();
		for (String id : page.getContent()) {
			list.add(subtitleLineDao.findOne(id));
		}

		// 20161003 超过2000页现在查询会极慢，一般100页即1000条数据应该已经完全可以满足全文检索的要求，更多的不需要展示给用户
		long totalElements = page.getTotalElements();
		if (totalElements > maxSearchPage * pageRequest.getPageSize()) {
			totalElements = maxSearchPage * pageRequest.getPageSize();
		}

		return new PageImpl<SubtitleLine>(list, pageRequest, totalElements);
	}
}
