package xie.animeshotsite.timer.timer.beforeshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.timer.timer.BaseTaskTimer;
import xie.common.Constants;
import xie.common.string.XStringUtils;
import xie.function.collection.CollectKamigami;
import xie.module.spring.SpringUtil;

@Component
@Scope("prototype")
public class EpisodeUpdateMonitorTimer extends BaseTaskTimer {

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
		List<AutoRunParam> animeMonitorFlagList = autoRunParamService.findAnimeMonitorDownloadLUrlList();
		_log.info("获取到当前监视中的动画数量：{}", animeMonitorFlagList.size());

		for (AutoRunParam monitorFlagParam : animeMonitorFlagList) {
			String animeInfoId = monitorFlagParam.getAnimeInfoId();
			Map<String, AutoRunParam> animeAutoRunParamMap = autoRunParamService.getStringAutoRunParamMap(animeInfoId, true, false);

			String url = animeAutoRunParamMap.get("video_download_monitor_page_url").getValue();
			String reg = animeAutoRunParamMap.get("video_download_monitor_torrent_url_reg").getValue();
			Assert.hasText(url, "自动运行参数video_download_monitor_page_url不能为空, animeInfoId:" + animeInfoId);

			List<String> urlList = new ArrayList<>();
			if (url.contains("kamigami.org")) {
				// 诸神
				if (XStringUtils.isNotBlank(reg)) {
					urlList = CollectKamigami.getTorrentUrlList(url, reg);
				} else {
					urlList = CollectKamigami.getTorrentUrlList(url);
				}
			} else if (url.contains("comicat.org")) {
				// TODO comicat.org
			}

			_log.info("获取到url：{}", urlList);
			updateEpisodeParamByUrlList(animeInfoId, reg, urlList);
		}
	}

	/**
	 * 根据搜索到的下载地址列表，将下载地址更新到动画剧集的自动化参数中
	 */
	public void updateEpisodeParamByUrlList(String animeInfoId, String reg, List<String> urlList) {

		List<AutoRunParam> episodeMonitorFlagList = autoRunParamService.findEpisodeMonitorDownloadLUrlist(animeInfoId);
		Map<String, AutoRunParam> episodeMonitorFlagMap = episodeMonitorFlagList.stream().collect(Collectors.toMap(AutoRunParam::getAnimeEpisodeId, (p) -> p));

		// 根据reg获得集数，作为key放入map
		Pattern pattern = Pattern.compile(reg);
		Map<String, String> urlMap = new HashMap<>();
		for (String str : urlList) {
			Matcher matcher = pattern.matcher(str);
			if (matcher.find()) {
				String findStr = matcher.group(1);
				System.out.println(findStr);
				urlMap.put(findStr, str);
			} else {
				_log.error("没有在下载链接中找到需要的信息, url:{}, reg:{}", str, reg);
			}
		}

		// 遍历下载url列表，获得每个url对应的剧集信息
		for (String number : urlMap.keySet()) {
			AnimeEpisode animeEpisode = animeEpisodeDao.findByAnimeInfoIdAndNumber(animeInfoId, number);
			if (animeEpisode != null) {
				String animeEpisodeId = animeEpisode.getId();
				_log.debug("找到剧集[{}]，开始更新, animeInfoId:{}, animeEpisodeId:{}, number:{}, url:{}", animeEpisode.getFullName(), animeInfoId, animeEpisodeId, number, urlMap.get(number));

				AutoRunParam episodeMonitorFlagParam = episodeMonitorFlagMap.get(animeEpisodeId);
				// 剧集的下载地址监视状态为1的情况下，更新数据
				if (episodeMonitorFlagParam != null && Constants.FLAG_STR_YES.equals(episodeMonitorFlagParam.getValue())) {
					// 将种子下载url地址放入自动运行参数，
					autoRunParamService.saveEpisodeByTemplet(animeEpisodeId, "video_download_do_download_url", urlMap.get(number));

					// 开始下载状态(video_download_do_download_flag)更新为0
					autoRunParamService.saveEpisodeByTemplet(animeEpisodeId, "video_download_do_download_flag", "0");

					// 同时将监视状态(video_download_monitor_do_flag)更新为2，
					autoRunParamService.saveEpisodeByTemplet(animeEpisodeId, "video_download_monitor_do_flag", "2");
				} else {
					_log.debug("当前剧集[{}]为不监视下载地址，跳过更新。", animeEpisode.getFullName());
				}
			} else {
				_log.info("没有找到剧集, animeInfoId:{}, number:{}", animeInfoId, number);
			}
		}

	}

	public static void main(String[] arg) {
		System.setProperty("spring.profiles.default", "development");
		System.setProperty("spring.profiles.default", "productRemote");
		EpisodeUpdateMonitorTimer episodeUpdateMonitorTimer = SpringUtil.getBean(EpisodeUpdateMonitorTimer.class);
		episodeUpdateMonitorTimer.run();
	}
}
