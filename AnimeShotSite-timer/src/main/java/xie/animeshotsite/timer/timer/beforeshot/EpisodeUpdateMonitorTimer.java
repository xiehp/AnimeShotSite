package xie.animeshotsite.timer.timer.beforeshot;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.timer.timer.BaseTaskTimer;
import xie.function.collection.CollectKamigami;

import java.util.List;

@Component
@Scope("prototype")
public class EpisodeUpdateMonitorTimer extends BaseTaskTimer {

	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeEpisodeService animeEpisodeService;
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
	protected void taskTimer() {

		List<AutoRunParam> listAnime = autoRunParamService.findAnimeMonitorDownloadLUrlList();
		listAnime.forEach(animeParam -> {
			String animeInfoId = animeParam.getAnimeInfoId();
			String url =animeParam.getValue();
			CollectKamigami.getTorrentUrlList();
		});




	}
}
