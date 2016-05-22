package xie.animeshotsite.timer.a2i.listener;

import java.awt.image.BufferedImage;
import java.io.File;

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
import xie.animeshotsite.db.service.ImageUrlService;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.spring.SpringUtil;
import xie.animeshotsite.utils.FilePathUtils;
import xie.common.string.XStringUtils;
import xie.common.utils.JsonUtil;
import xie.tietuku.spring.TietukuConfig;
import xie.v2i.listener.Video2ImageAdapter;
import xie.v2i.utils.CImage;

@Component
public class SaveImageListener extends Video2ImageAdapter {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private UploadPerHourCouter uploadPerHourCouter;

	/** 是否已经保存过剧集和动画信息的图片 */
	private boolean hasSaveEpisodeImageFlg = false;

	/** 如果数据库图片已经已经存在，是否强制更新 */
	private boolean forceUpdate = false;

	/** 如果图片已经有贴图库信息，是否强制重新上传更新 */
	private boolean forceUpload = false;

	private String animeInfoId;
	private String animeEpisodeId;
	private AnimeEpisode animeEpisode;
	private File rootPath;
	/** 不带number的path */
	private String detailPath;
	private String number;
	private File fullDetailPath;

	private String tietukuToken;

	private ShotInfoService shotInfoService;
	private ShotInfoDao shotInfoDao;
	private AnimeEpisodeService animeEpisodeService;
	private AnimeInfoService animeInfoService;
	private ImageUrlService imageUrlService;

	public SaveImageListener(String animeInfoId, String animeEpisodeId, String rootPath, String detailPath, String number) {
		shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		shotInfoDao = SpringUtil.getBean(ShotInfoDao.class);
		animeEpisodeService = SpringUtil.getBean(AnimeEpisodeService.class);
		animeInfoService = SpringUtil.getBean(AnimeInfoService.class);
		imageUrlService = SpringUtil.getBean(ImageUrlService.class);
		uploadPerHourCouter =  SpringUtil.getBean(UploadPerHourCouter.class);

		this.animeInfoId = animeInfoId;
		this.animeEpisodeId = animeEpisodeId;
		animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
		if (rootPath != null) {
			this.rootPath = new File(rootPath);
		} else {
			this.rootPath = FilePathUtils.getRootDefault();
		}
		this.detailPath = detailPath;
		this.number = number;
		this.fullDetailPath = FilePathUtils.getShotDetailFolderDefault(detailPath, number);

		TietukuConfig tietukuConfig = SpringUtil.getBean(TietukuConfig.class);
		tietukuToken = tietukuConfig.getTietukuToken();
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

	@Override
	public void isRefreshedAfterChangeTime(long setTime, long originalTime, BufferedImage image) {
		// 保存截图到本地硬盘
		logger.info("isRefreshedAfterChangeTime setTime:" + setTime + ", originalTime" + originalTime);

		ShotInfo shotInfo = shotInfoDao.findByAnimeEpisodeIdAndTimeStamp(animeEpisodeId, setTime);
		String fileName = String.valueOf(setTime);
		if (shotInfo != null) {
			logger.info("获取shotInfo数据, " + "id:" + shotInfo.getId() + ", timeStamp:" + shotInfo.getTimeStamp() + ", version:" + shotInfo.getVersion());
			fileName = shotInfo.getLocalFileName();
		} else {
			logger.info("获取shotInfo数据, null");
			fileName = animeEpisode.getFullName() + " " + setTime;
			if (XStringUtils.isBlank(fileName)) {
				fileName = String.valueOf(setTime);
			}
		}
		File file = CImage.getFilePath(fileName, fullDetailPath);
		file = CImage.saveImage(image, file);
		if (file == null) {
			// TODO 保存失败
			logger.error("保存失败，路径：{}，时间： {}", fullDetailPath, setTime);
		}

		// 保存到数据库
		shotInfo = shotInfoService.createShotInfo(animeInfoId, animeEpisodeId, setTime, originalTime, rootPath.getAbsolutePath(), detailPath, file.getName(), forceUpdate);
		logger.info("保存到数据库, " + "id:" + shotInfo.getId() + ", timeStamp:" + shotInfo.getTimeStamp() + ", version:" + shotInfo.getVersion());

		if (forceUpload || XStringUtils.isBlank(shotInfo.getTietukuUrlId())) {
			// 增加次数，同时判断当前是否刷新上传次数，以及暂停操作
			uploadPerHourCouter.addCount();

			// 保存截图到贴图库网站
			String responseStr = PostImage.doUpload(file, tietukuToken);
			logger.info(responseStr);
			TietukuUploadResponse responseUpload = JsonUtil.fromJsonString(responseStr, TietukuUploadResponse.class);
			String tietukuUrl = responseUpload.getLinkurl();
			if (tietukuUrl == null) {
				logger.error("贴图库上传失败，返回值：{},{}", responseUpload.getCode(), responseUpload.getInfo());
				if ("483".equals(responseUpload.getCode())) {
					try {
						Thread.sleep(3600 * 1000);

						responseStr = PostImage.doUpload(file, tietukuToken);
						logger.info(responseStr);
						responseUpload = JsonUtil.fromJsonString(responseStr, TietukuUploadResponse.class);
						tietukuUrl = responseUpload.getLinkurl();
						if (tietukuUrl == null) {
							logger.error("贴图库上传失败，返回值：{},{}", responseUpload.getCode(), responseUpload.getInfo());
							throw new RuntimeException("贴图库上传失败，返回值：" + responseUpload.getCode() + "," + responseUpload.getInfo());
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					throw new RuntimeException("贴图库上传失败，返回值：" + responseUpload.getCode() + "," + responseUpload.getInfo());
				}
			}

			String tietukuImageUrlPrefix = TietukuUtils.getImageUrlPrefix(tietukuUrl);
			String tietukuImageUrlId = TietukuUtils.getImageUrlID(tietukuUrl);

			// 更新贴图库数据库
			shotInfo = shotInfoService.setTietukuUrl(shotInfo, tietukuImageUrlId, tietukuImageUrlPrefix);
		}

		shotInfoService.save(shotInfo);

		// 修改剧集图片信息
		if (!hasSaveEpisodeImageFlg) {
			if (setTime > getTotalTime() / 2) {
				AnimeInfo animeinfo = animeInfoService.findOne(animeInfoId);
				AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);

				// 剧集
				if (animeEpisode != null && XStringUtils.isBlank(animeEpisode.getTitleUrlId())) {
					animeEpisodeService.saveTitleUrl(animeEpisode, FilePathUtils.getAnimeRoot(null, animeEpisode).getAbsolutePath(), new File(detailPath, number).getPath(), file.getName(), shotInfo.getTietukuUrlId(), shotInfo.getTietukuUrlPrefix());
				}

				// 动画
				if (animeinfo != null && XStringUtils.isBlank(animeinfo.getTitleUrlId())) {
					animeInfoService.saveTitleUrl(animeinfo, rootPath.getAbsolutePath(), new File(detailPath, number).getPath(), file.getName(), shotInfo.getTietukuUrlId(), shotInfo.getTietukuUrlPrefix());
				}

				hasSaveEpisodeImageFlg = true;
			}
		}

		// TODO 其他网站
	}
}
