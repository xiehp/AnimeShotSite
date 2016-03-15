package xie.animeshotsite.timer.timer;

import java.io.File;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.timer.a2i.listener.SaveImageListener;
import xie.animeshotsite.utils.FilePathUtils;
import xie.v2i.app.Video2Image;

@Component
public class AnimeShotTimer extends TimerTask {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	AnimeEpisodeService animeEpisodeService;

	@Override
	public void run() {
		List<AnimeEpisode> list = animeEpisodeService.findByProcessAction(AnimeInfo.PROCESS_ACTION_FULL);
		logger.info("list", list);

		for (AnimeEpisode animeEpisode : list) {
			try {
				AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
				logger.info("begin process : " + animeInfo.getName() + ", " + animeEpisode.getName());
				File animeEpisodeFile = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());

				logger.info("begin process : " + animeEpisodeFile.getAbsolutePath());

				SaveImageListener saveImageListener = new SaveImageListener(animeEpisode.getAnimeInfoId(), animeEpisode.getId(), animeEpisode.getLocalRootPath(), animeEpisode.getLocalDetailPath(), animeEpisode.getNumber());
				File fileMrl = animeEpisodeFile;
				Video2Image video2Image = new Video2Image(fileMrl.getAbsolutePath(), saveImageListener);
				if (animeEpisode.getWidth() != null && animeEpisode.getHeight() != null) {
					video2Image.setSize(animeEpisode.getWidth(), animeEpisode.getHeight());
				}
				video2Image.run();

				while (!video2Image.isClosed()) {
					Thread.sleep(5000);
				}

				if (video2Image.isProcessSuccess()) {
					logger.info("process 成功 : " + animeEpisode.getName());

					animeEpisode = animeEpisodeService.findOne(animeEpisode.getId());
					animeEpisode.setProcessAction(AnimeEpisode.PROCESS_ACTION_NO);
					animeEpisodeService.save(animeEpisode);
				} else {
					logger.error("process 失败");
				}

			} catch (Exception e) {
				logger.error("process 失败", e);
			}
		}
	}

}
