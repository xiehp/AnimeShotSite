package xie.animeshotsite.timer.task;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.SubtitleInfoDao;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.SubtitleInfoService;
import xie.animeshotsite.db.service.SubtitleLineService;
import xie.animeshotsite.spring.SpringUtil;
import xie.base.entity.BaseEntity;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.module.baidu.XPostBaiduUrls;
import xie.module.sitemap.XSiteMap;

@Component
public class CreateSiteMap {
	static Logger logger = LoggerFactory.getLogger(CreateSiteMap.class);

	String postBaiduUrl = "http://data.zz.baidu.com/urls?site=www.acgimage.com&token=Zxb8L0pj9RH7W8Ij";// 网站的服务器连接
	String baseUrl = "http://www.acgimage.com";

	XSiteMap xSiteMap = null;
	List<String> urls = new ArrayList<>();

	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	AnimeInfoDao animeInfoDao;
	@Autowired
	AnimeEpisodeService animeEpisodeService;
	@Autowired
	AnimeEpisodeDao animeEpisodeDao;
	@Autowired
	SubtitleInfoService subtitleInfoService;
	@Autowired
	SubtitleInfoDao subtitleInfoDao;
	@Autowired
	SubtitleLineService subtitleLineService;
	@Autowired
	SubtitleLineDao subtitleLineDao;

	private int urlCount = 0;

	public void runTask(Map<String, Object> paramMap) {
		xSiteMap = new XSiteMap();

		// 增加首页loc
		addUrl("/", XSiteMap.CHANGEFREQ_DAILY, "1.0", null, "首页");
		addUrl("/anime", XSiteMap.CHANGEFREQ_WEEKLY, "0.9", null, "动画列表");
		addUrl("/search", XSiteMap.CHANGEFREQ_WEEKLY, "0.9", null, "字幕台词搜索");
		addUrl("/random", XSiteMap.CHANGEFREQ_MONTHLY, "0.9", null, "随便看");
		addUrl("/maCalc", XSiteMap.CHANGEFREQ_DAILY, "0.9", null, "乖离性百万亚瑟王伤害计算器");

		// 增加所有剧集的截图列表的第一页loc
		{
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO);
			searchParams.put("EQ_showFlg", Constants.FLAG_STR_YES);
			Page<AnimeEpisode> animeEpisodePage = animeEpisodeService.searchPageByParams(searchParams, 1, Integer.MAX_VALUE, BaseEntity.COLUMN_CREATE_DATE, AnimeEpisode.class);
			List<AnimeEpisode> animeEpisodeList = animeEpisodePage.getContent();
			for (AnimeEpisode animeEpisode : animeEpisodeList) {
				addUrl("/shot/list/" + animeEpisode.getId(), XSiteMap.CHANGEFREQ_MONTHLY, "0.8", DateUtil.convertToString(animeEpisode.getUpdateDate(), "yyyy-MM-dd"), "截图列表 " + animeEpisode.getFullName() + " 第一页");
			}
		}

		// 增加所有动画loc
		{
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_deleteFlag", Constants.FLAG_INT_NO);
			searchParams.put("EQ_showFlg", Constants.FLAG_STR_YES);
			Page<AnimeInfo> animeInfoPage = animeInfoService.searchPageByParams(searchParams, 1, Integer.MAX_VALUE, BaseEntity.COLUMN_CREATE_DATE, AnimeInfo.class);
			List<AnimeInfo> animeInfoList = animeInfoPage.getContent();
			for (AnimeInfo animeInfo : animeInfoList) {
				addUrl("/episode/list/" + animeInfo.getId(), XSiteMap.CHANGEFREQ_MONTHLY, "0.7", DateUtil.convertToString(animeInfo.getUpdateDate(), "yyyy-MM-dd"), "剧集列表 " + animeInfo.getFullName());
			}
		}

		File file = new File("D:\\work\\project\\AnimeShotSite\\AnimeShotSite-portal\\src\\main\\webapp\\sitemap.xml");
		try {
			xSiteMap.save(file);
			logger.info("sitemap生成结束，url数：{}，路径：{}", urlCount, file.getAbsolutePath());
		} catch (TransformerException e) {
			logger.error("sitemap生成失败：", e);
		}

		// 百度推送
		boolean doPostBaiduUrl = false;
		if (doPostBaiduUrl) {
			try {
				String[] urlArray = new String[urls.size()];
				urls.toArray(urlArray);
				XPostBaiduUrls.Post(postBaiduUrl, urlArray);
			} catch (Exception e) {
				logger.error("百度推送失败：", e);
			}
		}
	}

	private void addUrl(String path, String changefreq, String priority, String lastmod, String title) {
		String loc = path.startsWith("/") ? baseUrl + path : baseUrl + "/" + path;
		xSiteMap.addUrl(loc, changefreq, priority, lastmod, title);
		urls.add(loc);
		urlCount++;
	}

	public static void main(String[] args) {
		CreateSiteMap createSiteMap = SpringUtil.getBean(CreateSiteMap.class);
		createSiteMap.runTask(null);
		System.exit(0);
	}
}
