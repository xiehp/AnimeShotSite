package xie.web.ajax.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xie.animeshotsite.db.entity.AnimeEpisode;
import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.base.controller.BaseController;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.common.string.XStringUtils;

@Controller
@RequestMapping(value = "/ajaxAutoComplete")
public class AutoCompleteController extends BaseController {

	@Resource
	private AnimeInfoService animeInfoService;
	@Resource
	private AnimeEpisodeService animeEpisodeService;

	/**
	 * 获取服务器时间
	 * 
	 * @return
	 */
	@RequestMapping("/getNowDate")
	public ResponseEntity<?> getNowDate(Model model, ServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Date nowDate = new Date();
		map.put("nowDate", DateUtil.convertToString(nowDate, DateUtil.YMD1));
		map.put("nowDateFull", DateUtil.convertToString(nowDate, DateUtil.YMD_FULL));
		Calendar cal = Calendar.getInstance();
		map.put("year", String.valueOf(cal.get(Calendar.YEAR)));
		map.put("month", String.valueOf(cal.get(Calendar.MONTH)));
		map.put("day", String.valueOf(cal.get(Calendar.DATE)));
		map.put("hour", String.valueOf(cal.get(Calendar.HOUR)));
		map.put("minute", String.valueOf(cal.get(Calendar.MINUTE)));
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}

	/**
	 * @return
	 */
	@RequestMapping("/getAnimeName")
	public ResponseEntity<?> getAnimeName(
			@RequestParam(value = "name", required = false) String name,
			Model model, ServletRequest request) {

		name = request.getParameter("term");

		Map<String, Object> searchParams = new HashMap<String, Object>();
		if (XStringUtils.isNotBlank(name)) {
			List<String> fullNameValueList = new ArrayList<>();
			for (int i=0; i<name.length(); i++) {
				String str = name.substring(i, i+1);
				if (XStringUtils.isNotBlank(str)) {
					fullNameValueList.add(str);
				}
			}
			searchParams.put("LIKE_" + AnimeInfo.COLUMN_FULL_NAME, fullNameValueList);
		}
		searchParams.put("EQ_" + AnimeInfo.COLUMN_SHOW_FLG, Constants.FLAG_INT_YES);
		searchParams.put("EQ_" + AnimeInfo.COLUMN_DELETE_FLAG, Constants.FLAG_INT_NO);
		Page<AnimeInfo> page = animeInfoService.searchPageByParams(searchParams, 1, 20, AnimeInfo.COLUMN_FULL_NAME, AnimeInfo.class);
		List<AnimeInfo> list = page.getContent();

		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("code", list.get(i).getId());
			hashMap.put("label", list.get(i).getFullName());
			listData.add(hashMap);
		}
		return new ResponseEntity<List<Map<String, String>>>(listData, HttpStatus.OK);
	}

	/**
	 * @return
	 */
	@RequestMapping("/getEpisodeName")
	public ResponseEntity<?> getEpisodeName(
			@RequestParam(value = "name", required = false) String name,
			Model model, ServletRequest request) {
		name = request.getParameter("term");

		Assert.hasText(name, "名字不能为空");

		Map<String, Object> searchParams = new HashMap<String, Object>();

		List<String> fullNameValueList = new ArrayList<>();
		for (int i=0; i<name.length(); i++) {
			String str = name.substring(i, i+1);
			if (XStringUtils.isNotBlank(str)) {
				fullNameValueList.add(str);
			}
		}

		searchParams.put("LIKE_" + AnimeEpisode.COLUMN_FULL_NAME, fullNameValueList);
		searchParams.put("EQ_" + AnimeEpisode.COLUMN_SHOW_FLG, Constants.FLAG_INT_YES);
		searchParams.put("EQ_" + AnimeEpisode.COLUMN_DELETE_FLAG, Constants.FLAG_INT_NO);
		Page<AnimeEpisode> page = animeEpisodeService.searchPageByParams(searchParams, 1, 20, AnimeEpisode.COLUMN_FULL_NAME, AnimeEpisode.class);
		List<AnimeEpisode> list = page.getContent();

		List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("code", list.get(i).getId());
			hashMap.put("label", list.get(i).getFullName());
			listData.add(hashMap);
		}
		return new ResponseEntity<List<Map<String, String>>>(listData, HttpStatus.OK);
	}

}