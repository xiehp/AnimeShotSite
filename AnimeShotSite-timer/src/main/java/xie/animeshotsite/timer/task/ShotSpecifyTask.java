package xie.animeshotsite.timer.task;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.spring.SpringUtil;
import xie.animeshotsite.timer.a2i.listener.SaveImageListener;
import xie.animeshotsite.timer.base.XTask;
import xie.animeshotsite.utils.FilePathUtils;
import xie.common.XNumberUtils;
import xie.v2i.app.Video2Image;
import xie.v2i.config.Video2ImageProperties;

@Configuration
@ComponentScan("xie")
@Component
public class ShotSpecifyTask implements XTask {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	AnimeEpisodeService animeEpisodeService;

	public static void main(String[] args) {
		args = new String[3];
		args[0] = "4028e381534fa25f01534fbd571f0006"; // 动画剧集ID
		args[1] = "0"; // timeStamp
		args[2] = "1"; // 是否强制上传图片

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(Video2ImageProperties.KEY_id, "4028e381534fa25f01534fbd571f0006");
		paramMap.put(Video2ImageProperties.KEY_forceUpload, false);
		paramMap.put(Video2ImageProperties.KEY_specifyTimes, "100000,200000");

		ShotSpecifyTask shotSpecifyTime = SpringUtil.getBean(ShotSpecifyTask.class);
		int aaa = shotSpecifyTime.run(args, paramMap);
		System.exit(aaa);
	}

	@Override
	public void runTask(Map<String, Object> paramMap) {
		run(null, paramMap);
	}

	public int run(String[] args, Map<String, Object> paramMap) {
		try {
			logger.info("begin process animeEpisodeId: " + paramMap);
			String animeEpisodeId = (String) paramMap.get(Video2ImageProperties.KEY_id);
			long[] timeStampArray = XNumberUtils.split((String) paramMap.get(Video2ImageProperties.KEY_specifyTimes));
			Boolean forceUpload = (Boolean) paramMap.get(Video2ImageProperties.KEY_forceUpload);

			AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
			AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
			logger.info("begin process : " + animeInfo.getName() + ", " + animeEpisode.getName());
			File animeEpisodeFile = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());

			logger.info("begin process : " + animeEpisodeFile.getAbsolutePath());

			SaveImageListener saveImageListener = new SaveImageListener(animeEpisode.getAnimeInfoId(), animeEpisode.getId(), animeEpisode.getLocalRootPath(), animeEpisode.getLocalDetailPath(), animeEpisode.getNumber());
			File fileMrl = animeEpisodeFile;
			if (forceUpload != null) {
				saveImageListener.setForceUpload(forceUpload);
			}

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
				logger.info("process 成功 : " + animeEpisode.getName());
			} else {
				logger.error("process 失败");
			}

		} catch (Exception e) {
			logger.error("process 失败", e);
		}

		return 0;
	}
}
