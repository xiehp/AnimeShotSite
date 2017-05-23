package xie.site.test;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.spring.SpringUtil;
import xie.common.json.XJsonUtil;
import xie.common.json.XObjectMapperFactoryBean;

public class DataTest {
	public static void main(String[] args) throws Exception {
		System.setProperty("spring.profiles.default", "development");
		ObjectMapper xObjectMapper = SpringUtil.getBean("xObjectMapper");
		System.out.println(xObjectMapper);

		XObjectMapperFactoryBean xObjectMapperFactoryBean = SpringUtil.getBean(XObjectMapperFactoryBean.class);
		System.out.println(xObjectMapperFactoryBean);
		System.out.println(xObjectMapperFactoryBean.getObject());

		xObjectMapperFactoryBean = SpringUtil.getBean("xObjectMapper");
		System.out.println(xObjectMapperFactoryBean);

		System.out.println(XJsonUtil.getObjectMapper());

		// ShotInfoService shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		// List<ShotInfo> list = shotInfoService.getMasterRecommandShotList(1, 7, 44);
		//
		// System.out.println(list);
	}
}
