package xie.animeshotsite.timer.timer.beforeshot;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.gudy.azureus2.core3.download.DownloadManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.entity.SubtitleInfo;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AutoRunParamDao;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.repository.SubtitleInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.timer.module.vuze.VuzeDownload;
import xie.animeshotsite.timer.timer.BaseTaskTimer;
import xie.animeshotsite.utils.FilePathUtils;
import xie.common.exception.XException;
import xie.common.io.XFileUtils;
import xie.common.string.XStringUtils;
import xie.common.string.XUrlUtils;
import xie.function.mkvtool.MkvextractCmdUtils;
import xie.module.download.XDownloadUtils;
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
	private SubtitleInfoService subtitleInfoService;
	@Resource
	private SubtitleInfoDao subtitleInfoDao;
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
	@Resource
	EntityCache entityCache;

	/*
	 * (non-Javadoc)
	 * 
	 * @see xie.animeshotsite.timer.timer.BaseTaskTimer#taskTimer()
	 */
	@Override
	protected void taskTimer() throws Exception {
		List<AutoRunParam> doDownloadFlagList = autoRunParamService.findEpisodeDownloadList();

		for (AutoRunParam doDownloadFlagParam : doDownloadFlagList) {
			String animeEpisodeId = doDownloadFlagParam.getAnimeEpisodeId();
			String doDownloadFlagValue = doDownloadFlagParam.getValue();
			try {
				AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
				// 判断video_download_do_download_flag 状态 ：等待中 下载中 下载完成 已终止
				if ("0".equals(doDownloadFlagValue)) {
					_log.info("开始处理剧集，animeEpisode：{}， 下载状态：{}", animeEpisode.getFullName(), doDownloadFlagValue);

					AutoRunParam doDownloadUrlParam = autoRunParamService.findEpisodeParam(animeEpisodeId, "video_download_do_download_url");
					if (doDownloadUrlParam != null) {
						String url = doDownloadUrlParam.getValue();
						if (url.endsWith(".torrent")) {
							// TODO 还要处理各种异常错误
							// 下载bt文件
							String fileName = XUrlUtils.getFileName(url);
							File torrentFile = FilePathUtils.getAnimeDetailFolderWithTorrent(null, animeEpisode, fileName);
							_log.info("下载种子文件，url:{}， 保存到:{}" + url, torrentFile.getAbsolutePath());
							torrentFile = downloadTorrent(url, torrentFile);
							autoRunParamService.saveEpisodeByTemplet(animeEpisodeId, "video_download_torrent_file_path", torrentFile.getAbsolutePath());

							// 交给vuze下载文件
							addBtTask(torrentFile, torrentFile.getParentFile().getAbsoluteFile());
							autoRunParamService.updateEpisodeValue(animeEpisodeId, "video_download_do_download_flag", "1");
						} else {
							String log = String.format("非种子文件，animeEpisodeId:%s, key:%s, url:%s", animeEpisodeId, "video_download_do_download_url", url);
							_log.error(log);
							autoRunParamService.updateEpisodeValue(animeEpisodeId, "video_download_do_download_flag", "3", log);
							downloadVideo(url);
						}
					} else {
						String log = String.format("没有找到url参数，animeEpisodeId:%s, key:%s", animeEpisodeId, "video_download_do_download_url");
						_log.error(log);
						autoRunParamService.updateEpisodeValue(animeEpisodeId, "video_download_do_download_flag", "3", log);
					}
				}

				// 下载中
				else if ("1".equals(doDownloadFlagValue)) {
					// 查看bt状态
					File torrentFile = entityCache.get("video_download_do_download_url" + animeEpisodeId, () -> {
						AutoRunParam _torrentFileParam = autoRunParamService.findEpisodeParam(animeEpisodeId, "video_download_torrent_file_path");
						return new File(_torrentFileParam.getValue());
					}, 3600000);

					// 更新下载百分比
					int percent = VuzeDownload.getInstance().getPercentDoneExcludingDND(torrentFile);
					autoRunParamService.saveEpisodeByTemplet(animeEpisodeId, "video_download_completed_percent", String.valueOf(percent));

					if (percent == 1000) {
						// 更新下载标记为下载完成
						_log.info("下载完成，torrentFile:{}", torrentFile.getAbsolutePath());
						autoRunParamService.updateEpisodeValue(animeEpisodeId, "video_download_do_download_flag", "2");
					}
				}

				// 下载结束
				else if ("2".equals(doDownloadFlagValue)) {
					_log.info("开始处理剧集，animeEpisode：{}， 下载状态：{}", animeEpisode.getFullName(), doDownloadFlagValue);

					// 获得视频文件地址
					File videoFile = null;
					AutoRunParam videoFilePathParam = autoRunParamService.findEpisodeParam(animeEpisodeId, "video_download_video_file_path");
					if (videoFilePathParam == null || XStringUtils.isBlank(videoFilePathParam.getValue())) {
						File torrentFile = entityCache.get("video_download_do_download_url" + animeEpisodeId, () -> {
							AutoRunParam _torrentFileParam = autoRunParamService.findEpisodeParam(animeEpisodeId, "video_download_torrent_file_path");
							return new File(_torrentFileParam.getValue());
						}, 3600000);

						videoFile = VuzeDownload.getInstance().getVideoFile(torrentFile);
						if (videoFile == null) {
							throw new XException("未在DM中找到" + torrentFile + "对应的下载文件");
						}

						// 将文件地址存放到自动运行参数中
						autoRunParamService.saveEpisodeByTemplet(animeEpisodeId, "video_download_video_file_path", videoFile.getAbsolutePath());

						// 从vuze中删除下载
						_log.info("准备从vuze中删除bt下载，torrentFile：{}", torrentFile.getAbsolutePath());
						VuzeDownload.getInstance().removeDownloadManager(torrentFile);
						_log.info("已从vuze中删除bt下载，torrentFile：{}", torrentFile.getAbsolutePath());
					} else {
						videoFile = new File(videoFilePathParam.getValue());
					}
					_log.info("获取到视频文件地址：{}", videoFile.getAbsoluteFile());

					// 开始提取字幕
					MkvextractCmdUtils.extract(videoFile);

					// 验证字幕是否提取
					String namePrefix = XFileUtils.getNameRemoveExt(videoFile.getName());
					File subtitleFiles[] = videoFile.getParentFile().listFiles(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							if (name.startsWith(namePrefix) && (name.endsWith(".ass") || name.endsWith(".txt"))) {
								return true;
							}
							return false;
						}
					});
					if (subtitleFiles == null || subtitleFiles.length == 0) {
						throw new XException("未发现生成的字幕文件，videoFile：" + videoFile);
					}

					// 移动视频文件（生成File）
					File detailFolder = FilePathUtils.getAnimeDetailFolder(null, animeEpisode);
					File newVideoFile = new File(detailFolder, videoFile.getName());
					_log.info("生成视频文件移动目标：{}", newVideoFile.getAbsoluteFile());

					// 移动字幕文件
					// TODO 以后这一块，字幕信息不再手动生成，要变成自动生成字幕信息
					List<SubtitleInfo> listSubtitleInfo = subtitleInfoService.findByAnimeEpisodeId(animeEpisodeId);
					if (listSubtitleInfo.size() == 0) {
						throw new XException("未发现生成的字幕文件，videoFile：" + videoFile);
					}
					SubtitleInfo subtitleInfo = listSubtitleInfo.get(0);
					File targetSubtitleFolder = FilePathUtils.getCommonFolder(subtitleInfo.getLocalRootPath(), subtitleInfo.getLocalDetailPath());
					_log.info("生成字幕文件移动目标目录：{}", targetSubtitleFolder.getAbsoluteFile());
					for (File file : subtitleFiles) {
						File target = new File(targetSubtitleFolder, file.getName());
						boolean success = file.renameTo(target);
						if (!success) {
							throw new XException("移动字幕文件失败：" + file.getAbsolutePath() + " -> " + target.getAbsolutePath());
						}
						_log.info("移动了字幕：{} -> {}", file.getAbsoluteFile(), target.getAbsolutePath());
					}

					// 添加字幕任务
					ShotTask shotSubtitleTask = shotTaskService.addCreateSubtitleTask(null, animeEpisode.getAnimeInfoId(), new Date(), false, false);
					_log.info("添加字幕任务：", shotSubtitleTask);

					// 移动视频文件（移动）
					boolean success = videoFile.renameTo(newVideoFile);
					if (!success) {
						throw new XException("移动视频文件失败：" + videoFile.getAbsolutePath() + " -> " + newVideoFile.getAbsolutePath());
					}
					_log.info("移动了视频：{} -> {}", videoFile.getAbsoluteFile(), newVideoFile.getAbsolutePath());

					// 添加视频截图任务
					ShotTask shotEpisodeTask = shotTaskService.addRunNormalEpisideTimeTask(animeEpisodeId, new Date(), false, null, null, 5000L);
					_log.info("添加截图任务：", shotEpisodeTask);

					// 更新自动运行参数
					_log.info("视频处理完成，准备更新状态到4");
					autoRunParamService.updateEpisodeValue(animeEpisodeId, "video_download_do_download_flag", "4", "处理完成");
				}

				// TODO 其他不正确的状态
				else {
					String log = String.format("未知状态，animeEpisodeId:%s, key:%s", animeEpisodeId, "video_download_do_download_flag");
					_log.error(log);
				}
			} catch (Exception e) {
				String log = "上次状态:" + doDownloadFlagValue + ", 错误信息:" + e.toString();
				_log.error(log, e);
				autoRunParamService.updateEpisodeValue(animeEpisodeId, "video_download_do_download_flag", "3", log);
			}
		}
	}

	public File downloadTorrent(String url, File toFile) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		URL u = new URL(url);
		XDownloadUtils.download(u, toFile);
		return toFile;
	}

	/**
	 * 将种子交给auze进行下载
	 */
	public void addBtTask(File torrentFile, File videoPath) {
		VuzeDownload.getInstance().addTask(torrentFile, videoPath);
		_log.info("增加bt任务到vuze，torrentFile:{}， videoPath:{}", torrentFile.getAbsolutePath(), videoPath.getAbsolutePath());
	}

	public void downloadVideo(String url) {

	}

	public static void main(String[] arg) throws InterruptedException {
		System.setProperty("spring.profiles.default", "development");
		System.setProperty("spring.profiles.default", "productRemote");
		EpisodeFileDownloadTimer episodeUpdateMonitorTimer = SpringUtil.getBean(EpisodeFileDownloadTimer.class);
		episodeUpdateMonitorTimer.run();

		episodeUpdateMonitorTimer.listen();

		VuzeDownload.getInstance().stop();
	}

	public void listen() throws InterruptedException {
		List<AutoRunParam> doDownloadFlagList = autoRunParamService.findEpisodeDownloadList();

		for (int i = 0; i < 1; i++) {
			Thread.sleep(1000);
			try {
				List<DownloadManager> list = VuzeDownload.getInstance().getAzureusCore().getGlobalManager().getDownloadManagers();
				System.out.println(list.size());

				for (AutoRunParam doDownloadFlagParam : doDownloadFlagList) {
					String animeEpisodeId = doDownloadFlagParam.getAnimeEpisodeId();
					AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
					AutoRunParam doDownloadUrlParam = autoRunParamService.findEpisodeParam(animeEpisodeId, "video_download_do_download_url");
					if (doDownloadUrlParam != null) {
						String url = doDownloadUrlParam.getValue();
						if (url.endsWith(".torrent")) {
							String fileName = XUrlUtils.getFileName(url);
							File torrentFile = FilePathUtils.getAnimeDetailFolderWithTorrent(null, animeEpisode, fileName);

							DownloadManager dm = VuzeDownload.getInstance().getDownloadManager(torrentFile);
							System.out.println(dm.getTorrentFileName());
							System.out.println("ActivationCount:" + dm.getActivationCount());
							System.out.println("getPercentDoneExcludingDND:" + dm.getStats().getPercentDoneExcludingDND());
						} else {
							_log.error("非种子文件，animeEpisodeId:{}, key:{}, url:{}", animeEpisodeId, "video_download_do_download_url", url);
						}
					} else {
						_log.error("没有找到url参数，animeEpisodeId:{}, key:{}", animeEpisodeId, "video_download_do_download_url");
					}

				}

				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
