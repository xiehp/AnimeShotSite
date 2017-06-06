package xie.animeshotsite.timer.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tietuku.entity.main.PostImage;
import com.tietuku.entity.util.TietukuUtils;
import com.tietuku.entity.vo.TietukuUploadResponse;

import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.GifInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.GifInfoService;
import xie.animeshotsite.timer.base.XBaseTask;
import xie.animeshotsite.utils.FilePathUtils;
import xie.common.date.DateUtil;
import xie.common.number.XNumberUtils;
import xie.common.utils.XWaitTime;
import xie.module.command.XCommandFactory;
import xie.module.command.impl.XWindowsCommand;
import xie.module.spring.SpringUtil;
import xie.tietuku.spring.TietukuConfig;
import xie.v2i.config.Video2ImageProperties;

import javax.annotation.Resource;

@Component(value = "CreateGifTask")
public class CreateGifTask extends XBaseTask {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeEpisodeService animeEpisodeService;
	@Resource
	private GifInfoService gifInfoService;
	@Resource
	private ApplicationContext applicationContext;
	@Resource
	TietukuConfig tietukuConfig;

	// private String gifCmdStr = "ffmpeg -ss 25 -t 10 -i E:\\AnimeShotSIte\\anime\\J\\吉卜力\\听到涛声\\Umi.ga.Kikoeru.2015.BluRay.1080p.FLAC.x265-MGRT.mkv -s 384x216 -f gif -r 12 D:\b.gif";
	/** 参数1:开始时间 参数2:持续时间 参数3:视频文件路径4:gif存放路径 */
	private String gifCmdStr = "ffmpeg -ss {} -t {} -i \"{}\" -s 384x216 -f gif -r 1 \"{}\"";

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
		CreateGifTask shotSpecifyTime = SpringUtil.getBean(CreateGifTask.class);
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

			String animeEpisodeId = (String) paramMap.get(AnimeEpisode.COLUMN_ID);
			//String animeInfoId = (String) paramMap.get(AnimeEpisode.COLUMN_ANIME_INFO_ID);
			long startTime = XNumberUtils.getLongValue(paramMap.get("startTime"));
			long continueTime = XNumberUtils.getLongValue(paramMap.get("continueTime"));

			AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
			AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
			String animeInfoId = animeInfo.getId();
			logger.info("begin process : " + animeEpisode.getFullName());
			File animeEpisodeFile = FilePathUtils.getAnimeFullFilePath(animeInfo, animeEpisode, animeEpisode.getLocalFileName());

			logger.info("begin process : " + animeEpisodeFile.getAbsolutePath());
			if (!animeEpisodeFile.exists()) {
				logger.error("文件不存在：" + animeEpisodeFile.getAbsolutePath());
				throw new FileNotFoundException("文件不存在：" + animeEpisodeFile.getAbsolutePath());
			}

			// 生成gif存放路径
			String detailPath = FilePathUtils.getDetailPath(null, animeEpisode, animeInfo);
			detailPath = detailPath.replaceFirst(ShotCoreConstants.LOCAL_ROOT_SHOT_PATH, ShotCoreConstants.LOCAL_ROOT_GIF_PATH);
			File fullDetailPath = FilePathUtils.getShotDetailFolder(null, animeEpisode, animeInfo);
			fullDetailPath = new File(fullDetailPath.getAbsolutePath().replaceFirst("\\" + ShotCoreConstants.LOCAL_ROOT_SHOT_PATH, "\\" + ShotCoreConstants.LOCAL_ROOT_GIF_PATH));
			if (!fullDetailPath.exists()) {
				fullDetailPath.mkdirs();
			}
			String gifFileName = animeEpisode.getFullName() + " " + DateUtil.formatTime(startTime * 1000, 3) + "_" + continueTime + "秒.gif";
			gifFileName = gifFileName.replace("?", "？");
			gifFileName = gifFileName.replace("  ", " ").replace("  ", " ").replace("  ", " ");
			File gifFilePath = new File(fullDetailPath, gifFileName);
			logger.info("文件路径：" + gifFilePath);

			XWindowsCommand xWindowsCommand = XCommandFactory.createWindowsInstance();
			Object[] cmdParams = new Object[] { startTime, continueTime, animeEpisodeFile.getAbsolutePath(), gifFilePath };
			gifCmdStr = "ffmpeg -ss {} -t {} -i \"{}\" -s 384x216 -f gif -r 8 \"{}\"";

			String cmd = MessageFormatter.arrayFormat(gifCmdStr, cmdParams).getMessage();
			logger.info("生成命令：" + cmd);

			XWaitTime xWaitTime = new XWaitTime(9999999);
			xWindowsCommand.runCmd(cmd);
			logger.info("命令执行时间：{}毫秒" + xWaitTime.getPastTime());

			// 将gif结果存到数据库中
			GifInfo shotInfo = gifInfoService.createGifInfo(animeInfoId, animeEpisodeId, startTime * 1000, continueTime * 1000, null, null, gifFilePath.getName());
			logger.info("保存到数据库, " + "id:" + shotInfo.getId() + ", timeStamp:" + shotInfo.getTimeStamp() + ", version:" + shotInfo.getVersion());
			// 设置尺寸
			shotInfo.setWidth(394);
			shotInfo.setHeight(216);

			// 向贴图库传文件
			PostImage postImage = new PostImage();
			TietukuUploadResponse tietukuUploadResponse = postImage.uploadToTietuku(gifFilePath, tietukuConfig.getTietukuTokenGif());

			// 贴图库内容保存到数据库中
			String tietukuUrl = tietukuUploadResponse.getLinkurl();
			String tietukuImageUrlPrefix = TietukuUtils.getImageUrlPrefix(tietukuUrl, true);
			String tietukuImageUrlId = TietukuUtils.getImageUrlID(tietukuUrl);

			// 更新贴图库数据库
			shotInfo = gifInfoService.setTietukuUrl(shotInfo, tietukuImageUrlId, tietukuImageUrlPrefix);
			shotInfo = gifInfoService.save(shotInfo);

			logger.info("process 成功 : {}, 时间{}, 持续时间", gifFilePath, startTime, continueTime);
		} catch (Exception e) {
			logger.error("process 失败", e);
			throw e;
		}

		return 0;
	}
}
