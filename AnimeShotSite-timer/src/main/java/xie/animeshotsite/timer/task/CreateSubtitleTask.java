package xie.animeshotsite.timer.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.repository.SubtitleInfoDao;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.animeshotsite.timer.base.XBaseTask;
import xie.common.string.XStringUtils;
import xie.module.spring.SpringUtil;
import xie.v2i.config.Video2ImageProperties;

@Component
public class CreateSubtitleTask extends XBaseTask {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SubtitleInfoService subtitleInfoService;
	@Autowired
	SubtitleInfoDao subtitleInfoDao;
	@Autowired
	SubtitleLineService subtitleLineService;
	@Autowired
	SubtitleLineDao subtitleLineDao;

	public static void main(String[] args) throws Exception {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(Video2ImageProperties.KEY_id, "4028e381534fa25f01534fbd571f0006");
		paramMap.put(Video2ImageProperties.KEY_forceUpload, false);
		paramMap.put(Video2ImageProperties.KEY_timeInterval, 60000);
		paramMap.put(Video2ImageProperties.KEY_startTime, 0);
		paramMap.put(Video2ImageProperties.KEY_endTime, 0);

		CreateSubtitleTask shotSpecifyTime = SpringUtil.getBean(CreateSubtitleTask.class);
		int aaa = shotSpecifyTime.run(paramMap);
		System.exit(aaa);
	}

	@Override
	public void runTask(Map<String, Object> paramMap) throws Exception {
		try {
			logger.info("开始处理字幕文件，参数：{}", paramMap);
			// XStringUtils.println(paramMap);
			run(paramMap);
		} catch (Exception e) {
			logger.error("process 失败", e);
			throw e;
		} finally {
			logger.info("process 结束");
		}
	}

	private int run(Map<String, Object> paramMap) throws Exception {
		String subtitleInfoId = (String) paramMap.get(SubtitleInfo.COLUMN_ID);
		String animeInfoId = (String) paramMap.get(SubtitleInfo.COLUMN_animeInfoId);
		Boolean forceUpdate = (Boolean) paramMap.get("forceUpdate");
		if (forceUpdate == null) {
			forceUpdate = false;
		}
		Boolean forceDelete = (Boolean) paramMap.get("forceDelete");
		if (forceDelete == null) {
			forceDelete = false;
		}

		if (XStringUtils.isNotBlank(subtitleInfoId)) {
			SubtitleInfo subtitleInfo = subtitleInfoDao.findOne(subtitleInfoId);
			if (forceUpdate || subtitleLineDao.countBySubtitleInfoId(subtitleInfo.getId()) == 0) {
				subtitleLineService.saveSubtitleLine(subtitleInfo, forceDelete);
			} else {
				logger.info("subtitleInfoId: {1}, 已存在字幕，跳过执行", subtitleInfoId);
			}
		} else if (XStringUtils.isNotBlank(animeInfoId)) {
			List<SubtitleInfo> list = subtitleInfoDao.findByAnimeInfoIdOrderByLocalFileName(animeInfoId);
			for (SubtitleInfo subtitleInfo : list) {
				if (!forceUpdate && subtitleLineDao.countBySubtitleInfoId(subtitleInfo.getId()) > 0) {
					logger.info("subtitleInfoId: {1}, 已存在字幕，跳过执行", subtitleInfoId);
					continue;
				}

				subtitleLineService.saveSubtitleLine(subtitleInfo, forceDelete);
			}
		} else {
			logger.error("字幕处理参数错误，无法执行。");
		}

		return 0;
	}
}
