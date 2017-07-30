package xie.animeshotsite.timer.a2i.listener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tietuku.entity.main.PostImage;
import com.tietuku.entity.util.TietukuUtils;
import com.tietuku.entity.vo.TietukuUploadResponse;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.utils.AutoCollectUtils;
import xie.animeshotsite.utils.FilePathUtils;
import xie.common.date.DateUtil;
import xie.common.string.XStringUtils;
import xie.module.spring.SpringUtil;
import xie.tietuku.spring.TietukuConfig;
import xie.v2i.listener.Video2ImageAdapter;
import xie.v2i.utils.CImage;

@Component
public class SaveImageListener extends Video2ImageAdapter {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private UploadPerHourCouter uploadPerHourCouter;

	/** 是否已经保存过剧集和动画信息的图片 */
	private boolean hasSavedEpisodeImageFlg = false;

	/** 是否已经保存过时长 */
	private boolean hasSavedDurationFlg = false;

	/** 如果数据库图片已经已经存在，是否强制更新 */
	private boolean forceUpdate = false;

	/** 如果图片已经有贴图库信息，是否强制重新上传更新 */
	private boolean forceUpload = false;

	private int perHourLimitCount = 295;

	private String animeInfoId;
	private String animeEpisodeId;
	private AnimeEpisode animeEpisode;
	private File rootPath;
	/** 不带number的detail path */
	private File detailPath;
	private File detailPathWithNumber;
	private File fullDetailPath;

	private String tietukuToken;

	private ShotInfoService shotInfoService;
	private ShotInfoDao shotInfoDao;
	private AnimeEpisodeService animeEpisodeService;
	private AnimeInfoService animeInfoService;
	private AutoCollectUtils autoCollectUtils;

	private PostImage postImage;

	/**
	 * 
	 * @param animeInfoId
	 * @param animeEpisodeId
	 * @param rootPath
	 * @param detailPath
	 * @param number 剧集唯一标识
	 */
	@Deprecated
	public SaveImageListener(String animeInfoId, String animeEpisodeId, String rootPath, String detailPath, String number) {
		init(animeInfoId, animeEpisodeId, rootPath, detailPath, number);
	}

	public SaveImageListener(AnimeEpisode animeEpisode) {
		shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		shotInfoDao = SpringUtil.getBean(ShotInfoDao.class);
		animeEpisodeService = SpringUtil.getBean(AnimeEpisodeService.class);
		animeInfoService = SpringUtil.getBean(AnimeInfoService.class);
		uploadPerHourCouter = SpringUtil.getBean(UploadPerHourCouter.class);
		autoCollectUtils = SpringUtil.getBean(AutoCollectUtils.class);

		this.animeEpisode = animeEpisode;
		this.animeInfoId = animeEpisode.getAnimeInfoId();
		this.animeEpisodeId = animeEpisode.getId();
		AnimeInfo animeInfo = animeInfoService.findOne(animeInfoId);
		this.rootPath = FilePathUtils.getShotRoot(null, animeEpisode, animeInfo);
		this.detailPath = new File(FilePathUtils.getDetailPath(null, animeEpisode, animeInfo));
		this.detailPathWithNumber = new File(detailPath, animeEpisode.getNumber());
		this.fullDetailPath = FilePathUtils.getShotDetailFolder(null, animeEpisode, animeInfo);

		TietukuConfig tietukuConfig = SpringUtil.getBean(TietukuConfig.class);
		tietukuToken = tietukuConfig.getTietukuToken();
		postImage = new PostImage();
	}

	private void init(String animeInfoId, String animeEpisodeId, String rootPath, String detailPath, String number) {
		shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		shotInfoDao = SpringUtil.getBean(ShotInfoDao.class);
		animeEpisodeService = SpringUtil.getBean(AnimeEpisodeService.class);
		animeInfoService = SpringUtil.getBean(AnimeInfoService.class);
		uploadPerHourCouter = SpringUtil.getBean(UploadPerHourCouter.class);

		this.animeInfoId = animeInfoId;
		this.animeEpisodeId = animeEpisodeId;
		animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
		if (rootPath != null) {
			this.rootPath = new File(rootPath);
		} else {
			this.rootPath = FilePathUtils.getRootDefault();
		}

		this.detailPath = new File(detailPath);
		this.fullDetailPath = FilePathUtils.getShotDetailFolderDefault(detailPath, number);

		TietukuConfig tietukuConfig = SpringUtil.getBean(TietukuConfig.class);
		tietukuToken = tietukuConfig.getTietukuToken();
		postImage = new PostImage();
	}

	public boolean isForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public boolean isForceUpdload() {
		return forceUpload;
	}

	public void setForceUpload(boolean forceUpdload) {
		this.forceUpload = forceUpdload;
	}

	public void close() throws IOException {
		postImage.close();
	}

	@Override
	public void isRefreshedAfterChangeTime(long setTime, long originalTime, BufferedImage image) {
		try {
			doSaveAndPostImage(setTime, originalTime, image);
		} catch (Exception e) {
			logger.error("发生错误，再次进行提交", e);

			// 连续发生两次错误就终止
			doSaveAndPostImage(setTime, originalTime, image);
		}
	}

	private void doSaveAndPostImage(long setTime, long originalTime, BufferedImage image) {
		if (hasSavedDurationFlg) {
			hasSavedDurationFlg = true;

			AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
			if (animeEpisode.getInfoDuration() == null) {
				animeEpisode.setInfoDuration(getTotalTime());
				animeEpisodeService.save(animeEpisode);
			}
		}
		// 保存截图到本地硬盘
		logger.info("isRefreshedAfterChangeTime setTime:" + setTime + ", originalTime" + originalTime);

		ShotInfo shotInfo = shotInfoDao.findByAnimeEpisodeIdAndTimeStamp(animeEpisodeId, setTime);
		String fileName = String.valueOf(setTime);
		if (shotInfo != null) {
			logger.info("获取shotInfo数据, " + "id:" + shotInfo.getId() + ", timeStamp:" + shotInfo.getTimeStamp() + ", version:" + shotInfo.getVersion());
			fileName = shotInfo.getLocalFileName();
		} else {
			logger.info("获取shotInfo数据, null");
			fileName = animeEpisode.getFullName() + " " + DateUtil.formatTime(setTime, 3);
		}
		if (XStringUtils.isBlank(fileName)) {
			fileName = String.valueOf(setTime);
		}

		File file = CImage.getFilePath(fileName, fullDetailPath);
		file = CImage.saveImage(image, file);
		if (file == null) {
			logger.error("保存失败，路径：{}，时间： {}", fullDetailPath, setTime);
			throw new RuntimeException("保存失败");
		}

		// 保存到数据库
		shotInfo = shotInfoService.createShotInfo(animeInfoId, animeEpisodeId, setTime, originalTime, null, null, file.getName(), forceUpdate);
		logger.info("保存到数据库, " + "id:" + shotInfo.getId() + ", timeStamp:" + shotInfo.getTimeStamp() + ", version:" + shotInfo.getVersion());

		if (forceUpload || XStringUtils.isBlank(shotInfo.getTietukuUrlId())) {
			// 增加次数，同时判断当前是否刷新上传次数，以及暂停操作
			// 控制295张图，留5张备用
			uploadPerHourCouter.addCount(perHourLimitCount);

			// 保存截图到贴图库网站
			logger.info("贴图库上传, " + "shotInfoId:" + shotInfo.getId());
			TietukuUploadResponse tietukuUploadResponse = postImage.uploadToTietuku(file, tietukuToken);
			String tietukuUrl = tietukuUploadResponse.getLinkurl();

			String tietukuImageUrlPrefix = TietukuUtils.getImageUrlPrefix(tietukuUrl, false);
			String tietukuImageUrlId = TietukuUtils.getImageUrlID(tietukuUrl);

			// 更新贴图库数据库
			shotInfo = shotInfoService.setTietukuUrl(shotInfo, tietukuImageUrlId, tietukuImageUrlPrefix);
		}

		shotInfo = shotInfoService.save(shotInfo);

		// 修改剧集图片信息
		if (!hasSavedEpisodeImageFlg) {
			if (setTime > getTotalTime() / 2) {
				AnimeInfo animeinfo = animeInfoService.findOne(animeInfoId);
				AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);

				// 剧集
				if (animeEpisode != null && XStringUtils.isBlank(animeEpisode.getTitleUrlId())) {
					animeEpisodeService.saveTitleUrl(animeEpisode, rootPath.getAbsolutePath(), detailPathWithNumber.getPath(), file.getName(), shotInfo.getTietukuUrlId(), shotInfo.getTietukuUrlPrefix());
				}

				// 动画
				if (animeinfo != null && XStringUtils.isBlank(animeinfo.getTitleUrlId())) {
					animeInfoService.saveTitleUrl(animeinfo, rootPath.getAbsolutePath(), detailPathWithNumber.getPath(), file.getName(), shotInfo.getTietukuUrlId(), shotInfo.getTietukuUrlPrefix());
				}

				// 剧集设置为显示状态
				animeEpisodeService.show(animeEpisodeId);

				hasSavedEpisodeImageFlg = true;
			}
		}

		// TODO 其他网站
	}

	@Override
	public void onSuccessComplete() {
		try {
			// 进行一次title获取
			AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
			if (animeEpisode.getSummary() == null) {
				autoCollectUtils.collectEpisodeSummary(animeInfoId, false);
			}
		} catch (Exception e) {
			logger.error("结束处理出错：", e);
		}
	}

	/**
	 * 如果截图数量符合预期，则返回true
	 */
	@Override
	public boolean canSuccessExit(long timeInterval) {
		if (timeInterval <= 0) {
			return false;
		}

		long totalTime = getTotalTime();
		if (totalTime <= 0) {
			return false;
		}

		int count = shotInfoDao.countByAnimeEpisodeId(animeEpisodeId);
		if (count <= 0) {
			return false;
		}

		int expectedCount = (int) (totalTime / timeInterval);
		if (count <= expectedCount) {
			return false;
		}

		return true;
	}

	public void setPerHourLimitCount(int perHourLimitCount) {
		this.perHourLimitCount = perHourLimitCount;
	}
}
