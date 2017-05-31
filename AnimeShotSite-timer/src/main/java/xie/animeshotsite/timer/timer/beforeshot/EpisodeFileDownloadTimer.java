package xie.animeshotsite.timer.timer.beforeshot;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.timer.timer.BaseTaskTimer;
import xie.module.spring.SpringUtil;

@Component
@Scope("prototype")
public class EpisodeFileDownloadTimer extends BaseTaskTimer {

	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private AnimeEpisodeDao animeEpisodeDao;
	@Resource
	private ShotTaskService shotTaskService;
	@Resource
	private ShotTaskDao shotTaskDao;
	@Resource
	private ApplicationContext applicationContext;

	@Resource
	AutoRunParamService autoRunParamService;

	@Resource
	AutoRunParamDao autoRunParamDao;

	@Override
	protected void taskTimer() throws Exception {
		List<AutoRunParam> doDownloadFlagList = autoRunParamService.findEpisodeWaitDownloadList();

		for (AutoRunParam doDownloadFlagParam : doDownloadFlagList) {
			String animeEpisodeId = doDownloadFlagParam.getAnimeEpisodeId();
			AutoRunParam doDownloadUrlParam = autoRunParamService.findEpisodeParam(animeEpisodeId, "video_download_do_download_url");
			if (doDownloadUrlParam != null) {
				String url = doDownloadUrlParam.getValue();
				if (url.endsWith(".torrent")) {
					downloadTorrent(url);
				} else {
					_log.error("非种子文件，animeEpisodeId:{}, key:{}, url:{}", animeEpisodeId, "video_download_do_download_url", url);
					downloadVideo(url);
				}
			} else {
				_log.error("没有找到url参数，animeEpisodeId:{}, key:{}", animeEpisodeId, "video_download_do_download_url");
			}

		}
	}

	public void downloadTorrent(String url) {

	}

	public void downloadVideo(String url) {

	}

	public static void main(String[] arg) {
		System.setProperty("spring.profiles.default", "development");
		System.setProperty("spring.profiles.default", "productRemote");
		EpisodeFileDownloadTimer episodeUpdateMonitorTimer = SpringUtil.getBean(EpisodeFileDownloadTimer.class);
		episodeUpdateMonitorTimer.run();
	}
}
