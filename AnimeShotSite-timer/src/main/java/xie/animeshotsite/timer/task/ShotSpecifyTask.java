package xie.animeshotsite.timer.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
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
public class ShotSpecifyTask extends XBaseTask {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	AnimeInfoService animeInfoService;
	@Resource
	AnimeEpisodeService animeEpisodeService;
	@Resource
	ApplicationContext applicationContext;

	public static void main(String[] args) throws Exception {
		args = new String[3];
		args[0] = "4028e381534fa25f01534fbd571f0006"; // 动画剧集ID
		args[1] = "0"; // timeStamp
		args[2] = "1"; // 是否强制上传图片

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(Video2ImageProperties.KEY_id, "4028e381534fa25f01534fbd571f0006");
		paramMap.put(Video2ImageProperties.KEY_forceUpload, false);
		paramMap.put(Video2ImageProperties.KEY_specifyTimes, "100000,200000");

		// ShotSpecifyTask shotSpecifyTime = SpringUtil.getBean(ShotSpecifyTask.class);
		ShotSpecifyTask shotSpecifyTime = SpringUtil.getBean(ShotSpecifyTask.class);
		int aaa = shotSpecifyTime.run(args, paramMap);
		System.exit(aaa);
	}

	@Override
	public void runTask(Map<String, Object> paramMap) throws Exception {
		run(null, paramMap);
	}

	public int run(String[] args, Map<String, Object> paramMap) throws Exception {
		try {
			logger.info("begin process animeEpisodeId: " + paramMap);
			String animeEpisodeId = (String) paramMap.get(Video2ImageProperties.KEY_id);
			long[] timeStampArray = XNumberUtils.split((String) paramMap.get(Video2ImageProperties.KEY_specifyTimes));
			Boolean forceUpload = (Boolean) paramMap.get(Video2ImageProperties.KEY_forceUpload);

			AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
			AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
			logger.info("begin process : " + animeInfo.getName() + ", " + animeEpisode.getFullName());
			File animeEpisodeFile = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());

			logger.info("begin process : " + animeEpisodeFile.getAbsolutePath());
			if (!animeEpisodeFile.exists()) {
				logger.error("文件不存在：" + animeEpisodeFile.getAbsolutePath());
				throw new FileNotFoundException("文件不存在：" + animeEpisodeFile.getAbsolutePath());
			}

//			SaveImageListener saveImageListener = new SaveImageListener(animeEpisode.getAnimeInfoId(), animeEpisode.getId(), animeEpisode.getLocalRootPath(), animeEpisode.getLocalDetailPath(), animeEpisode.getNumber());
			SaveImageListener saveImageListener = new SaveImageListener(animeEpisode);
			File fileMrl = animeEpisodeFile;
			if (forceUpload != null) {
				saveImageListener.setForceUpload(forceUpload);
			}
			saveImageListener.setPerHourLimitCount(300);

			Video2Image video2Image = new Video2Image(fileMrl.getAbsolutePath(), saveImageListener);
			video2Image.setRunMode(Video2ImageProperties.RUN_MODE_SPECIAL);
			video2Image.setSpecifyTimes(timeStampArray);
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
