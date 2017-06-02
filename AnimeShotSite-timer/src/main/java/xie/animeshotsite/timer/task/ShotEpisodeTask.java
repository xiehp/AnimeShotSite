package xie.animeshotsite.timer.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.timer.a2i.listener.SaveImageListener;
import xie.animeshotsite.timer.base.XBaseTask;
import xie.animeshotsite.utils.FilePathUtils;
import xie.common.number.XNumberUtils;
import xie.module.spring.SpringUtil;
import xie.v2i.app.Video2Image;
import xie.v2i.config.Video2ImageProperties;

@Component
public class ShotEpisodeTask extends XBaseTask {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	AnimeInfoService animeInfoService;
	@Resource
	AnimeInfoDao animeInfoDao;

	@Resource
	AnimeEpisodeService animeEpisodeService;

	public static void main(String[] args) throws Exception {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(Video2ImageProperties.KEY_id, "4028e381534fa25f01534fbd571f0006");
		paramMap.put(Video2ImageProperties.KEY_forceUpload, false);
		paramMap.put(Video2ImageProperties.KEY_timeInterval, 60000);
		paramMap.put(Video2ImageProperties.KEY_startTime, 0);
		paramMap.put(Video2ImageProperties.KEY_endTime, 0);

		ShotEpisodeTask shotSpecifyTime = SpringUtil.getBean(ShotEpisodeTask.class);
		int aaa = shotSpecifyTime.run(paramMap);
		System.exit(aaa);
	}

	@Override
	public void runTask(Map<String, Object> paramMap) throws Exception {
		run(paramMap);
	}

	private int run(Map<String, Object> paramMap) throws Exception {
		try {
			logger.info("begin process param: " + paramMap);
			String animeEpisodeId = (String) paramMap.get(Video2ImageProperties.KEY_id);
			logger.info("begin process animeEpisodeId: {}", animeEpisodeId);
			Long startTime = XNumberUtils.getLongValue(paramMap.get(Video2ImageProperties.KEY_startTime));
			Long endTime = XNumberUtils.getLongValue(paramMap.get(Video2ImageProperties.KEY_endTime));
			Long interval = XNumberUtils.getLongValue(paramMap.get(Video2ImageProperties.KEY_timeInterval));

			Boolean forceUpload = (Boolean) paramMap.get(Video2ImageProperties.KEY_forceUpload);

			logger.info("开始获取剧集信息");
			AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
			String aninmeInfoId = animeEpisode.getAnimeInfoId();
			logger.info("获得剧集信息成功, 动画信息ID:{}, 剧集名称:{}.", aninmeInfoId, animeEpisode.getFullName());
			AnimeInfo animeInfo = animeInfoDao.findById(aninmeInfoId);
			if (animeInfo != null) {
				logger.info("获得动画信息成功,animeInfo:{}.", animeInfo);
				logger.info("begin process : 动画ID:{}, 动画名:{}.", animeInfo.getId(), animeInfo.getName());
			} else {
				logger.info("获得动画信息失败.");
			}

			File animeEpisodeFile = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());
			logger.info("begin process : " + animeEpisodeFile.getAbsolutePath());
			if (!animeEpisodeFile.exists()) {
				logger.error("文件不存在：" + animeEpisodeFile.getAbsolutePath());
				throw new FileNotFoundException("文件不存在：" + animeEpisodeFile.getAbsolutePath());
			}

			logger.info("开始生成截图监听器");
			// SaveImageListener saveImageListener = new SaveImageListener(animeEpisode.getAnimeInfoId(), animeEpisode.getId(), animeEpisode.getLocalRootPath(), animeEpisode.getLocalDetailPath(), animeEpisode.getNumber());
			SaveImageListener saveImageListener = new SaveImageListener(animeEpisode);
			File fileMrl = animeEpisodeFile;
			if (forceUpload != null) {
				saveImageListener.setForceUpload(forceUpload);
			}
			logger.info("结束生成截图监听器，{}", saveImageListener);

			Video2Image video2Image = new Video2Image(fileMrl.getAbsolutePath(), saveImageListener);
			video2Image.setRunMode(Video2ImageProperties.RUN_MODE_INTERVAL);
			if (interval != null) {
				video2Image.setTimeInterval(interval);
			}
			if (startTime != null) {
				video2Image.setStartTime(startTime);
			}
			if (endTime != null) {
				video2Image.setEndTime(endTime);
			}
			if (animeEpisode.getWidth() != null && animeEpisode.getHeight() != null) {
				video2Image.setSize(animeEpisode.getWidth(), animeEpisode.getHeight());
			}
			video2Image.run();

			while (!video2Image.isClosed()) {
				Thread.sleep(5000);
			}

			if (video2Image.isProcessSuccess()) {
				logger.info("process 成功 : " + animeEpisode.getFullName());
			} else {
				logger.error("process 失败");
			}

			saveImageListener.close();
		} catch (Exception e) {
			logger.error("process 失败", e);
			throw e;
		}

		return 0;
	}
}
