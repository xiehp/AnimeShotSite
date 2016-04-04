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
import xie.animeshotsite.db.entity.ImageUrl;
import xie.animeshotsite.db.entity.ShotInfo;
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

	/** 是否已经保存过剧集和动画信息的图片 */
	private boolean hasSaveEpisodeImageFlg = false;

	/** 如果数据库图片已经已经存在，是否强制更新 */
	private boolean forceUpdate = false;

	/** 如果图片已经有贴图库信息，是否强制重新上传更新 */
	private boolean forceUpload = false;

	private String animeInfoId;
	private String animeEpisodeId;
	private File rootPath;
	/** 不带number的path */
	private String detailPath;
	private String number;
	private File fullDetailPath;

	private String tietukuToken;

	private ShotInfoService shotInfoService;
	private AnimeEpisodeService animeEpisodeService;
	private AnimeInfoService animeInfoService;
	private ImageUrlService imageUrlService;

	public SaveImageListener(String animeInfoId, String animeEpisodeId, String rootPath, String detailPath, String number) {
		shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		animeEpisodeService = SpringUtil.getBean(AnimeEpisodeService.class);
		animeInfoService = SpringUtil.getBean(AnimeInfoService.class);
		imageUrlService = SpringUtil.getBean(ImageUrlService.class);

		this.animeInfoId = animeInfoId;
		this.animeEpisodeId = animeEpisodeId;
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
	public void isRefreshedAfterChangeTime(long time, BufferedImage image) {
		// 保存截图到本地硬盘
		logger.info("isRefreshedAfterChangeTime time:" + time);
		File file = CImage.getFilePath(time, fullDetailPath);
		file = CImage.saveImage(image, file);
		if (file == null) {
			// TODO 保存失败
			logger.error("保存失败，路径：{}，时间： {}", fullDetailPath, time);
		}

		// 保存到数据库
		ShotInfo shotInfo = shotInfoService.saveShotInfo(animeInfoId, animeEpisodeId, time, rootPath.getAbsolutePath(), detailPath, file.getName(), forceUpdate);

		if (forceUpload || XStringUtils.isBlank(shotInfo.getTietukuUrlId())) {
			// 保存截图到贴图库网站

			String responseStr = PostImage.doUpload(file, tietukuToken);
			logger.debug(responseStr);
			TietukuUploadResponse responseUpload = JsonUtil.fromJsonString(responseStr, TietukuUploadResponse.class);
			String tietukuUrl = responseUpload.getLinkurl();
			if (tietukuUrl == null) {
				logger.error("贴图库上传失败，返回值：{},{}", responseUpload.getCode(), responseUpload.getInfo());
				throw new RuntimeException("贴图库上传失败，返回值：" + responseUpload.getCode() + "," + responseUpload.getInfo());
			}

			String tietukuImageUrlPrefix = TietukuUtils.getImageUrlPrefix(tietukuUrl);
			String tietukuImageUrlId = TietukuUtils.getImageUrlID(tietukuUrl);

			// 更新贴图库数据库
			shotInfo = shotInfoService.updateTietukuUrl(animeEpisodeId, time, tietukuImageUrlId, tietukuImageUrlPrefix);
		}

		// 修改剧集图片信息
		if (!hasSaveEpisodeImageFlg) {
			if (time > getTotalTime() / 2) {
				// 剧集
				AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
				ImageUrl imageUrl = null;
				if (XStringUtils.isBlank(animeEpisode.getTitleUrlId())) {
					imageUrl = imageUrlService.saveImageInfo(rootPath.getAbsolutePath(), new File(detailPath, number).getPath(), file.getName(), shotInfo.getTietukuUrlId(), shotInfo.getTietukuUrlPrefix());
					animeEpisode.setTitleUrlId(imageUrl.getId());
					animeEpisodeService.save(animeEpisode);
				}

				// 动画
				AnimeInfo Animeinfo = animeInfoService.findOne(animeInfoId);
				if (XStringUtils.isBlank(Animeinfo.getTitleUrlId())) {
					if (imageUrl == null) {
						imageUrl = imageUrlService.saveImageInfo(rootPath.getAbsolutePath(), new File(detailPath, number).getPath(), file.getName(), shotInfo.getTietukuUrlId(), shotInfo.getTietukuUrlPrefix());
					}

					Animeinfo.setTitleUrlId(imageUrl.getId());
					animeInfoService.save(Animeinfo);
				}

				hasSaveEpisodeImageFlg = true;
			}
		}

		// TODO 其他网站
	}
}
