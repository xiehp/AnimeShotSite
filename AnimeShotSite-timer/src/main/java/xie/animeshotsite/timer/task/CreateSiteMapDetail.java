package xie.animeshotsite.timer.task;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.repository.*;
import xie.animeshotsite.db.service.*;
import xie.base.entity.BaseEntity;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.module.sitemap.XSiteMap;
import xie.module.spring.SpringUtil;

@Component
public class CreateSiteMapDetail {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	String baseUrl = "http://www.acgimage.com";

	XSiteMap xSiteMap = null;

	@Resource
	AnimeInfoService animeInfoService;
	@Resource
	AnimeInfoDao animeInfoDao;
	@Resource
	AnimeEpisodeService animeEpisodeService;
	@Resource
	AnimeEpisodeDao animeEpisodeDao;
	@Resource
	SubtitleInfoService subtitleInfoService;
	@Resource
	SubtitleInfoDao subtitleInfoDao;
	@Resource
	SubtitleLineService subtitleLineService;
	@Resource
	ShotInfoService shotInfoService;
	@Resource
	SubtitleLineDao subtitleLineDao;
	@Resource
	ShotInfoDao shotInfoDao;

	private int urlCount = 0;

	public void runTask(Map<String, Object> paramMap) {
		xSiteMap = new XSiteMap();
		logger.info("开始运行", "CreateSiteMapDetail");

		// 增加所有截图的loc
		{
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO);
			int pageIndex = 1;
			while (true) {
				Page<ShotInfo> shotInfoPage = shotInfoService.searchPageByParams(searchParams, pageIndex, 1000, BaseEntity.COLUMN_CREATE_DATE, ShotInfo.class);
				List<ShotInfo> shotInfoList = shotInfoPage.getContent();
				logger.info("获得截图数据：{}条", shotInfoList.size());
				if (shotInfoList.isEmpty()) {
					break;
				}

				for (ShotInfo shotInfo : shotInfoList) {
					addUrl("/shot/view/" + shotInfo.getId(), XSiteMap.CHANGEFREQ_MONTHLY, "0.6", DateUtil.convertToString(shotInfo.getUpdateDate(), "yyyy-MM-dd"), null);
				}
				
				pageIndex++;
			}
		}

		File file = new File("D:\\work\\project\\AnimeShotSite\\AnimeShotSite-portal\\src\\main\\webapp\\sitemap_detail.xml");
		try {
			xSiteMap.save(file);
			logger.info("sitemap生成结束，url数：{}，路径：{}", urlCount, file.getAbsolutePath());
		} catch (TransformerException e) {
			logger.error("sitemap生成失败：", e);
		}
	}

	private void addUrl(String path, String changefreq, String priority, String lastmod, String title) {
		String loc = path.startsWith("/") ? baseUrl + path : baseUrl + "/" + path;
		xSiteMap.addUrl(loc, changefreq, priority, lastmod, title);
		urlCount++;
	}

	public static void main(String[] args) {
		CreateSiteMapDetail createSiteMapDetail  = SpringUtil.getBean(CreateSiteMapDetail.class);
		createSiteMapDetail.runTask(null);
		System.exit(0);
	}
}
