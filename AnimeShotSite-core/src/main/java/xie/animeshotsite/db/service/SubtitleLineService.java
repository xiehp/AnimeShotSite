package xie.animeshotsite.db.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.entity.SubtitleLine;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.SubtitleInfoDao;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.animeshotsite.db.repository.impl.SubtitleLineDaoImpl;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.common.modules.baidutranslate.BaiduLanguageMapping;
import xie.common.string.XStringUtils;
import xie.module.language.XLanguageUtils;
import xie.subtitle.Subtitle;
import xie.subtitle.SubtitleFactory;
import xie.subtitle.line.XSubtitleLine;

@Service
public class SubtitleLineService extends BaseService<SubtitleLine, String> {

	@Autowired
	private SubtitleLineDao subtitleLineDao;
	@Autowired
	private SubtitleInfoDao subtitleInfoDao;
	@Autowired
	private AnimeInfoDao animeInfoDao;
	@Autowired
	private AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	private EntityCache entityCache;
	@Autowired
	private SubtitleLineDaoImpl subtitleInfoDaoImpl;

	@Override
	public BaseRepository<SubtitleLine, String> getBaseRepository() {
		return subtitleLineDao;
	}

	public List<SubtitleLine> findBySubtitleInfoId(String subtitleInfoId) {
		return subtitleLineDao.findBySubtitleInfoId(subtitleInfoId);
	}

	public void deleteBySubtitleInfoId(String subtitleInfoId) {
		subtitleLineDao.deleteBySubtitleInfoId(subtitleInfoId);
	}

	/**
	 * 创建字幕数据
	 * 
	 * @param subtitleInfo
	 * @param forceDelete
	 */
	public void saveSubtitleLine(SubtitleInfo subtitleInfo, Boolean forceDelete) {
		File file = null;
		try {
			// 创建字幕数据
			file = FilePathUtils.getCommonFilePath(subtitleInfo.getLocalRootPath(), subtitleInfo.getLocalDetailPath(), subtitleInfo.getLocalFileName());
			logging.info("开始创建字幕数据, {}, forceDelete:{}", file, forceDelete);
			Subtitle subtitle = SubtitleFactory.createSubtitle(file, subtitleInfo.getFilterRemove(), subtitleInfo.getFilterInclude());
			if (subtitle != null && subtitle.getSubtitleLineList() != null) {
				List<XSubtitleLine> list = subtitle.getSubtitleLineList();
				logging.info("读取字幕文件成功，开始写入数据库。 字幕行数：{}", list.size());
				if (list != null && list.size() > 0) {
					// 删除老的数据
					if (forceDelete) {
						Date startDate = new Date();
						logging.warn("删除老的数据, " + startDate);
						deleteBySubtitleInfoId(subtitleInfo.getId());
						logging.warn("删除老的数据成功, " + new Date() + ", 耗时：" + (new Date().getTime() - startDate.getTime()));
					}

					// 保存新数据
					Date startDate = new Date();
					List<SubtitleLine> listSubtitleLine = new ArrayList<>();
					for (XSubtitleLine xSubtitleLine : list) {
						SubtitleLine subtitleLine = convertSubtitleLine(xSubtitleLine, subtitleInfo);
						listSubtitleLine.add(subtitleLine);
					}
					if (listSubtitleLine.size() > 0) {
						listSubtitleLine = getBaseRepository().save(listSubtitleLine);
					} else {
						logging.error("没有有效字幕行，放弃保存数据库");
					}

					logging.info("创建字幕数据成功，文件：{}，耗时：{}", file.getAbsolutePath(), DateUtil.formatTime(new Date().getTime() - startDate.getTime(), 3));

					// 更新subtitleInfo已录入字段
					if (!Constants.FLAG_INT_YES.equals(subtitleInfo.getSubInStatus())) {
						subtitleInfo = subtitleInfoDao.findById(subtitleInfo.getId());
						subtitleInfo.setSubInStatus(Constants.FLAG_INT_YES);
						subtitleInfoDao.save(subtitleInfo);
					}
				}
			}
		} catch (IOException e) {
			logging.error("创建字幕数据失败, {}", file == null ? "" : file.getAbsolutePath());
			logging.error("创建字幕数据失败", e);
		}
	}

	/**
	 * 将原始字幕行转换为entity<br>
	 * 如果数据库中已有该字幕行，则重新设置该字幕行。<br>
	 * 
	 * @param xSubtitleLine
	 * @param subtitleInfo
	 * @return
	 */
	private SubtitleLine convertSubtitleLine(XSubtitleLine xSubtitleLine, SubtitleInfo subtitleInfo) {
		Integer lineIndex = xSubtitleLine.getLineIndex();
		SubtitleLine subtitleLine = subtitleLineDao.findBySubtitleInfoIdAndLineIndex(subtitleInfo.getId(), lineIndex);
		if (subtitleLine == null) {
			subtitleLine = new SubtitleLine();
		}
		subtitleLine.setAnimeInfoId(subtitleInfo.getAnimeInfoId());
		subtitleLine.setAnimeEpisodeId(subtitleInfo.getAnimeEpisodeId());
		subtitleLine.setSubtitleInfoId(subtitleInfo.getId());

		subtitleLine.setLanguage(subtitleInfo.getLanguage());
		subtitleLine.setLineIndex(xSubtitleLine.getLineIndex());
		try {
			subtitleLine.setLayer(Integer.valueOf(xSubtitleLine.getLayer()));
		} catch (Exception e) {
			subtitleLine.setLayer(0);
			e.printStackTrace();
		}
		if (subtitleInfo.getOffsetTime() == null) {
			subtitleLine.setStartTime(xSubtitleLine.getStartTime());
			subtitleLine.setEndTime(xSubtitleLine.getEndTime());
		} else {
			if (subtitleInfo.getOffsetTimeStart() == null || xSubtitleLine.getStartTime() >= subtitleInfo.getOffsetTimeStart()) {
				subtitleLine.setStartTime(xSubtitleLine.getStartTime() + subtitleInfo.getOffsetTime());
				subtitleLine.setEndTime(xSubtitleLine.getEndTime() + subtitleInfo.getOffsetTime());
			} else {
				subtitleLine.setStartTime(xSubtitleLine.getStartTime());
				subtitleLine.setEndTime(xSubtitleLine.getEndTime());
			}
		}
		subtitleLine.setText(xSubtitleLine.getText());
		subtitleLine.setStyle(xSubtitleLine.getStyle());
		subtitleLine.setName(xSubtitleLine.getName());
		// subtitleLine.setMarginL(xSubtitleLine.getMarginL());
		// subtitleLine.setMarginR(xSubtitleLine.getMarginR());
		// subtitleLine.setMarginV(xSubtitleLine.getMarginV());

		return subtitleLine;
	}

	/**
	 * 查找某段时间内的字幕<br>
	 * 同时将总体时间少于时间段一半的字幕去除掉<br>
	 * 
	 * @param animeEpisodeId
	 * @param showLanage
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<SubtitleLine> findByTimeRemoveDuplicate(String animeEpisodeId, List<String> showLanage, Long shotStartTime, Long shotEndTime) {
		List<SubtitleLine> list = subtitleLineDao.findByTime(animeEpisodeId, shotStartTime, shotEndTime);

		if (shotEndTime <= shotStartTime) {
			return list;
		}

		Long subtitleStartTime;
		Long subtitleEndTime;
		String subtitleText;

		Set<String> duplicateRemoveSet = new HashSet<String>();
		List<SubtitleLine> newList = new ArrayList<SubtitleLine>();
		for (SubtitleLine subtitleLine : list) {
			subtitleStartTime = subtitleLine.getStartTime();
			subtitleEndTime = subtitleLine.getEndTime();
			subtitleText = subtitleLine.getText();
			SubtitleInfo subtitleInfo = subtitleInfoDao.findById(subtitleLine.getSubtitleInfoId());

			// 判断该语言是否需要显示
			if (showLanage != null && !showLanage.contains(subtitleInfo.getLanguage())) {
				continue;
			}

			// 去除时间和文本同时一样的字幕
			String duplicateKey = subtitleStartTime + subtitleText + subtitleEndTime + subtitleInfo.getLanguage();
			if (duplicateRemoveSet.contains(duplicateKey)) {
				continue;
			}

			// 判断是否在合适的时间段中
			if (!isInTime(shotStartTime, shotEndTime, subtitleStartTime, subtitleEndTime)) {
				continue;
			}

			newList.add(subtitleLine);
			duplicateRemoveSet.add(duplicateKey);
		}

		return newList;
	}

	/**
	 * 判断是否在合适的时间段中
	 * 
	 * @param startTime
	 * @param endTime
	 * @param calcStartTime
	 * @param calcEndTime
	 * @return
	 */
	private boolean isInTime(Long startTime, Long endTime, Long calcStartTime, Long calcEndTime) {

		boolean defaultResult = true;

		if (startTime == null || endTime == null || calcStartTime == null || calcEndTime == null) {
			return defaultResult;
		}

		long refTime = endTime - startTime; // 参照持续时间
		long calcTime = calcEndTime - calcStartTime; // 计算持续时间

		Long crossTime = null; // 相交时间
		Long notCrossTime = null; // 非相交时间
		if (calcStartTime < startTime) {
			if (calcEndTime <= endTime) {
				// 1.计算时间段仅相交在参照开始时间
				crossTime = calcEndTime - startTime;
			} else if (calcEndTime > endTime) {
				// 4.计算时间段同范围大于参照时间段
				return true;
			}
		} else if (calcStartTime >= startTime) {
			if (calcEndTime <= endTime) {
				// 2.计算时间段在参照时间段内
				return true;
			} else if (calcEndTime > endTime) {
				// 3.计算时间段仅相交在参照结束时间
				crossTime = endTime - calcStartTime;
			}
		}

		// 什么条件下不展示
		if (crossTime != null) {
			notCrossTime = calcTime - crossTime;
			// 1. 相交时间小于非相交时间， 同时相交时间不满参照持续时间80%，
			if (crossTime < notCrossTime && (float) crossTime < (float) refTime * 0.8) {
				return false;
			}
		}

		return defaultResult;
	}

	/**
	 * 检索字幕文本<br>
	 * 当检索文本中包含空格，则做分割，作为and条件合并<br>
	 * 
	 * @param searchMode true:精确检索, null或false:全文检索
	 */
	public Page<SubtitleLine> searchSubtitleLineByText(Boolean searchMode, String animeName, String keyword, PageRequest pageRequest) {
		Page<SubtitleLine> page = subtitleInfoDaoImpl.searchSubtitleLineByText(searchMode, animeName, keyword, pageRequest);
		return page;
	}

	/**
	 * 填入动画信息和剧集信息
	 */
	public SubtitleLine fillParentData(SubtitleLine subtitleLine) {
		if (subtitleLine != null) {
			AnimeInfo animeInfo = entityCache.findOne(animeInfoDao, subtitleLine.getAnimeInfoId());
			subtitleLine.setAnimeInfo(animeInfo);

			AnimeEpisode animeEpisode = entityCache.findOne(animeEpisodeDao, subtitleLine.getAnimeEpisodeId());
			subtitleLine.setAnimeEpisode(animeEpisode);
		}

		return subtitleLine;
	}

	/**
	 * 根据客户端显示语言和当前剧集
	 * 
	 * @param list
	 * @param actualShowLanage
	 * @param localeLanguage
	 * @return
	 */
	public List<SubtitleLine> convertChinese(List<SubtitleLine> list, List<String> actualShowLanage, String localeLanguage) {
		boolean cs2ctFlg = false;
		boolean ct2csFlg = false;
		if (Constants.LANGUAGE_ZH_CN.equals(localeLanguage) && !XStringUtils.existIgnoreCase(actualShowLanage, SubtitleInfo.LANGUAGE_CHS)) {
			ct2csFlg = true;
		}
		if (Constants.LANGUAGE_ZH_TW.equals(localeLanguage) && !XStringUtils.existIgnoreCase(actualShowLanage, SubtitleInfo.LANGUAGE_CHT)) {
			cs2ctFlg = true;
		}

		return convertChinese(list, cs2ctFlg, ct2csFlg);
	}

	/**
	 * 简繁互转
	 * 
	 * @param list
	 * @param cs2ctFlg 是否简体转成繁体
	 * @param ct2csFlg 是否繁体转成简体
	 * @return
	 */
	public List<SubtitleLine> convertChinese(List<SubtitleLine> list, boolean cs2ctFlg, boolean ct2csFlg) {
		if (list == null) {
			return null;
		}

		String language = null;
		String text = null;
		for (SubtitleLine line : list) {
			if (line == null) {
				continue;
			}

			language = line.getLanguage();
			text = line.getText();
			if (language == null || text == null) {
				continue;
			}

			if (cs2ctFlg && SubtitleInfo.LANGUAGE_CHS.equals(language)) {
				line.setText(XLanguageUtils.convertToTC(text));
			}
			if (ct2csFlg && SubtitleInfo.LANGUAGE_CHT.equals(language)) {
				line.setText(XLanguageUtils.convertToSC(text));
			}
		}

		return list;
	}

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		SubtitleLineService subtitleLineService = new SubtitleLineService();
		System.out.println(subtitleLineService.isInTime(5L, 10L, 4L, 5L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 4L, 6L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 4L, 7L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 4L, 8L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 4L, 9L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 9L, 10L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 9L, 11L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 9L, 12L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 9L, 222L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 8L, 222L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 7L, 222L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 6L, 222L));
		System.out.println(subtitleLineService.isInTime(5L, 10L, 5L, 222L));
		;
	}

	/**
	 * 创建字幕对应的百度的翻译签名
	 */
	public List<String> createBaiduTranslateSign(List<SubtitleLine> subtitleLineList, String appId, String key, String salt) {
		List<String> list = new ArrayList<>();
		subtitleLineList.forEach(line -> {
			String queryText = line.getText();
			String sign = BaiduLanguageMapping.getBaiduSign(appId, queryText, salt, key);
			list.add(sign);
		});
		
		return list;
	}
}
