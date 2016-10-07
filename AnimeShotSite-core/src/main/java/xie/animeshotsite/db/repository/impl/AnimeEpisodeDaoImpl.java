package xie.animeshotsite.db.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.base.repository.BaseRepositoryPlus;
import xie.common.Constants;
import xie.common.string.XStringUtils;
import xie.module.language.XLanguageUtils;

@Repository
public class AnimeEpisodeDaoImpl extends BaseRepositoryPlus<AnimeEpisode> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 精确地搜索数据库
	 * 
	 * @param animeName 动画名
	 * @param text 字幕文本
	 * @param pageRequest
	 * @return
	 */
	public Page<String> searchEpisodeIdByFullName(String animeName, PageRequest pageRequest) {
		if (XStringUtils.isBlank(animeName)) {
			return createEmptyPage(pageRequest);
		}

		String[] animeNameArray = animeName.split(" ");

		return searchEpisodeIdByFullName(animeNameArray, pageRequest);
	}

	/**
	 * 精确地搜索数据库
	 * 
	 * @param animeNameArray
	 * @param textArray
	 * @param pageRequest
	 * @return
	 */
	public Page<String> searchEpisodeIdByFullName(String[] animeNameArray, PageRequest pageRequest) {
		List<String> animeNameList = new ArrayList<>();
		if (animeNameArray != null) {
			for (String s : animeNameArray) {
				if (XStringUtils.isNotBlank(s)) {
					animeNameList.add(s.trim());
				}
			}
		}
		if (animeNameList.size() == 0) {
			return createEmptyPage(pageRequest);
		}

		Map<String, Object> map = Maps.newHashMap();
		StringBuffer querySql = new StringBuffer(800);

		querySql.append(" from AnimeEpisode episode ");
		querySql.append(" where ");
		querySql.append(AnimeEpisode.COLUMN_DELETE_FLAG + " = " + Constants.FLAG_INT_NO);

		for (int i = 0; i < animeNameList.size(); i++) {
			String oneAnimeName = animeNameList.get(i);
			querySql.append(" and (");
			querySql.append(" fullName like :oneAnimeName" + i);
			map.put("oneAnimeName" + i, "%" + oneAnimeName + "%");

			// 转换成简体 看是否需要匹配
			String oneAnimeNameSC = XLanguageUtils.convertToSC(oneAnimeName);
			if (!oneAnimeNameSC.equals(oneAnimeName)) {
				querySql.append(" or fullName like :oneAnimeNameSC" + i);
				map.put("oneAnimeNameSC" + i, "%" + oneAnimeNameSC + "%");
			}

			// 转换成繁体 看是否需要匹配
			String oneAnimeNameTC = XLanguageUtils.convertToSC(oneAnimeName);
			if (!oneAnimeNameTC.equals(oneAnimeName)) {
				querySql.append(" or fullName like :oneAnimeNameTC" + i);
				map.put("oneAnimeNameTC" + i, "%" + oneAnimeNameTC + "%");
			}

			querySql.append(" ) ");
		}

		String countHql = "select count( episode.id) " + querySql.toString();
		String queryHql = "select episode.id " + querySql.toString() + " order by episode.fullName";

		Page<String> page = getResult(queryHql, countHql, map, pageRequest);
		return page;
	}
}
