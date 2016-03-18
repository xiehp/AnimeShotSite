package xie.web.ajax.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.base.controller.BaseController;
import xie.common.date.DateUtil;

@Controller
@RequestMapping(value = "/ajaxAutoComplete")
public class AutoCompleteController extends BaseController {

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

}