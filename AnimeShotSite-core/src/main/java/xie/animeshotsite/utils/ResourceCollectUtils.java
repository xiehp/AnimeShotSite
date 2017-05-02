package xie.animeshotsite.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.common.string.XStringUtils;
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

	/**
	 * Map 从1开始计数
	 * 
	 * @param url
	 * @param titleReplaceReg
	 * @return
	 */
	public LinkedHashMap<Integer, Map<String, String>> collectBaiduEpisodeSummary(String url, String titleReplaceReg) {
		LinkedHashMap<Integer, Map<String, String>> collectedList = new LinkedHashMap<>();
		if (titleReplaceReg == null) {
			titleReplaceReg = ".*\\s+(.*)";
		}
		Pattern titleReg = Pattern.compile(titleReplaceReg);

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
				map.put("title", fullTitle);
				collectedList.put(i + 1, map);
				System.out.println(map.get("FullTitle"));
				if (XStringUtils.isNotBlank(fullTitle)) {
					// String title = fullTitle.replaceFirst(titleReplaceReg, "");

					Matcher matcher = titleReg.matcher(fullTitle);
					if (matcher.find()) {
						// System.out.println("count: " + matcher.groupCount());
						// System.out.println("matcher.group(): " + matcher.group());

						map.put("title", "");
						if (matcher.groupCount() > 0) {
							for (int g = 1; g < matcher.groupCount() + 1; g++) {
								// System.out.println("matcher.group(" + g + "):" + matcher.group(g));
								map.put("title", map.get("title") + matcher.group(g));
							}
						}
					}
				}
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
		// resourceCollectUtils.collectBaiduEpisodeSummary("https://baike.baidu.com/item/NEW%20GAME%21/18751606#分集剧情", null);
		// resourceCollectUtils.collectBaiduEpisodeSummary("http://baike.baidu.com/item/MACROSS%20DELTA?fromtitle=%E8%B6%85%E6%97%B6%E7%A9%BA%E8%A6%81%E5%A1%9EDelta&type=syn", "(MISSION .* )|(Mission .* )");
		resourceCollectUtils.collectBaiduEpisodeSummary("http://baike.baidu.com/item/%E5%A4%8F%E7%9B%AE%E5%8F%8B%E4%BA%BA%E5%B8%90%E9%99%86#reference-[2]-21004121-wrap", null);
	}

}
