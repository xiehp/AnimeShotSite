package com.yjysh.framework.rest;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.mapper.JsonMapper;

import com.google.common.collect.Lists;
import com.yjysh.framework.sample.vo.Temp;
import com.yjysh.framework.spring.SpringContextTestCase;
import com.yjysh.framework.sys.restful.service.BaseRest;
import com.yjysh.framework.web.sqider.service.ReplieUrlService;

@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext.xml","/applicationContext-framework.xml","/projectContext.xml","/deployContext.xml" })
public class RestTest  extends SpringContextTestCase {

	@Autowired
	BaseRest baseRest;
	
	@Autowired
	ReplieUrlService replieUrlService;
	
	protected JsonMapper mapper = new JsonMapper();
	
	@Test
	public void send(){
		
		JSONObject json = new JSONObject();
		json.put("a", "a");
		JSONObject json2 = new JSONObject();
		json2.put("b", "b");
		
		List list = Lists.newArrayList();
		list.add(json);
		list.add(json2);
		String result = baseRest.getRestTemplate().postForObject("http://localhost:8080/rating/open/recieveData", list, String.class);
		System.out.println(result);
	}
	
	@Test
	public void test(){
		List replieUrlServiceList = replieUrlService.findReplieUrlListByCompanyId("4028e081521ad80001521ae30f85010f");
		System.out.println(replieUrlServiceList.toString());
	}
	
}
