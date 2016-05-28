package xie.animeshotsite.db.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import xie.animeshotsite.db.repository.impl.SubtitleInfoDaoImpl;
import xie.animeshotsite.utils.FilePathUtils;
import xie.base.repository.BaseRepository;
import xie.base.service.BaseService;
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
	private SubtitleInfoDaoImpl subtitleInfoDaoImpl;

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

	public void saveSubtitleLine(SubtitleInfo subtitleInfo, Boolean forceDelete) {
		File file = null;
		try {
			// 创建字幕数据
			file = FilePathUtils.getCommonFilePath(subtitleInfo.getLocalRootPath(), subtitleInfo.getLocalDetailPath(), subtitleInfo.getLocalFileName());
			Subtitle subtitle = SubtitleFactory.createSubtitle(file);
			if (subtitle != null) {
				List<XSubtitleLine> list = subtitle.getSubtitleLineList();
				if (list != null && list.size() > 0) {
					// 删除老的数据
					if (forceDelete) {
						logging.error("删除老的数据");
						deleteBySubtitleInfoId(subtitleInfo.getId());
					}

					// 保存新数据
					for (XSubtitleLine xSubtitleLine : list) {
						saveSubtitleLine(xSubtitleLine, subtitleInfo);
					}

					logging.error("创建字幕数据成功，文件：{}", file.getAbsolutePath());
				}
			}
		} catch (IOException e) {
			logging.error("创建字幕数据失败, {}", file == null ? "" : file.getAbsolutePath());
			logging.error("创建字幕数据失败", e);
		}
	}

	public SubtitleLine saveSubtitleLine(XSubtitleLine xSubtitleLine, SubtitleInfo subtitleInfo) {
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
		subtitleLine.setStartTime(xSubtitleLine.getStartTime());
		subtitleLine.setEndTime(xSubtitleLine.getEndTime());
		subtitleLine.setText(xSubtitleLine.getText());
		subtitleLine.setStyle(xSubtitleLine.getStyle());
		subtitleLine.setName(xSubtitleLine.getName());
		// subtitleLine.setMarginL(xSubtitleLine.getMarginL());
		// subtitleLine.setMarginR(xSubtitleLine.getMarginR());
		// subtitleLine.setMarginV(xSubtitleLine.getMarginV());

		subtitleLine = getBaseRepository().save(subtitleLine);
		return subtitleLine;
	}

	/**
	 * 查找某段时间内的字幕<br>
	 * 同时将总体时间少于时间段一半的字幕去除掉<br>
	 * 
	 * @param animeEpisodeId
	 * @param defaultShowLanage
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
			if (duplicateRemoveSet.contains(subtitleStartTime + subtitleText + subtitleEndTime)) {
				continue;
			}

			// 判断是否在合适的时间段中
			if (!isInTime(shotStartTime, shotEndTime, subtitleStartTime, subtitleEndTime)) {
				continue;
			}

			newList.add(subtitleLine);
			duplicateRemoveSet.add(subtitleStartTime + subtitleText + subtitleEndTime);
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
	 */
	public Page<SubtitleLine> searchSubtitleLineByText(String animeName, String keyword, PageRequest pageRequest) {
		Page<SubtitleLine> page = subtitleInfoDaoImpl.searchSubtitleLineByText(animeName, keyword, pageRequest);
		return page;
	}

	/**
	 * 填入动画信息和剧集信息
	 */
	public void fillParentData(List<SubtitleLine> subtitleLineList) {
		for (SubtitleLine subtitleLine : subtitleLineList) {
			fillParentData(subtitleLine);
		}
	}

	/**
	 * 填入动画信息和剧集信息
	 */
	public void fillParentData(SubtitleLine subtitleLine) {
		if (subtitleLine != null) {
			AnimeInfo animeInfo = entityCache.findOne(animeInfoDao, subtitleLine.getAnimeInfoId());
			subtitleLine.setAnimeInfo(animeInfo);

			AnimeEpisode animeEpisode = entityCache.findOne(animeEpisodeDao, subtitleLine.getAnimeEpisodeId());
			subtitleLine.setAnimeEpisode(animeEpisode);
		}
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
}
