package xie.animeshotsite.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.module.httpclient.XHttpClientUtils;
import xie.module.httpclient.XPoolingHttpClientConnectionManager;
import xie.module.test.XTestUtils;

/**
 * 资源采集
 */
@Component
public class ResourceCollectUtils {

	Logger Logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	XHttpClientUtils xHttpClientUtils;

	public LinkedHashMap<Integer, Map<String, String>> collectBaiduEpisodeSummary(String url, String titleReplaceReg) {
		LinkedHashMap<Integer, Map<String, String>> collectedList = new LinkedHashMap<>();
		if (titleReplaceReg == null) {
			titleReplaceReg = "第.*话 ";
		}
		try {
			String htmlStr = xHttpClientUtils.getHtml(url);

			Document document = Jsoup.parse(htmlStr);
			Element elementDramaSerialList = document.select("#dramaSerialList").first();
			// 标题
			Elements ElementsTitleTd = elementDramaSerialList.select("dt");
			for (int i = 0; i < ElementsTitleTd.size(); i++) {
				Element element = ElementsTitleTd.get(i);
				Map<String, String> map = new HashMap<>();
				String fullTitle = element.text();
				map.put("FullTitle", fullTitle);
				collectedList.put(i + 1, map);

				System.out.println(map.get("FullTitle"));

				String title = fullTitle.replaceFirst(titleReplaceReg, "");
				map.put("title", title);
			}

			// 正文
			Elements ElementsDetailDD = elementDramaSerialList.select("dd");
			for (int i = 0; i < ElementsDetailDD.size(); i++) {
				Element element = ElementsDetailDD.get(i);
				element.select("div").remove();
				Map<String, String> values = collectedList.get(i + 1);
				if (values != null) {
					values.put("summary", element.text());

					System.out.println(values);
				}
			}

		} catch (Exception e) {
			Logger.error("collectBaiduEpisodeSummary出错", e);
		}

		return collectedList;
	}

	public static void main(String[] args) throws IOException {

		// URL url = new URL("http://www.acgimage.com");
		// Document document = Jsoup.parse(url, 30000);
		// System.out.println(document);

		ResourceCollectUtils resourceCollectUtils = XTestUtils.getBean(ResourceCollectUtils.class, XHttpClientUtils.class, XPoolingHttpClientConnectionManager.class);
		//resourceCollectUtils.collectBaiduEpisodeSummary("https://baike.baidu.com/item/NEW%20GAME%21/18751606#分集剧情", null);
		//resourceCollectUtils.collectBaiduEpisodeSummary("http://baike.baidu.com/item/MACROSS%20DELTA?fromtitle=%E8%B6%85%E6%97%B6%E7%A9%BA%E8%A6%81%E5%A1%9EDelta&type=syn", "(MISSION .* )|(Mission .* )");
		resourceCollectUtils.collectBaiduEpisodeSummary("http://baike.baidu.com/item/Fate%2Fkaleid%20liner%20%E9%AD%94%E6%B3%95%E5%B0%91%E5%A5%B3%E2%98%86%E4%BC%8A%E8%8E%89%E9%9B%85%203rei%21%21#分集剧情", null);
	}
	
	
}
