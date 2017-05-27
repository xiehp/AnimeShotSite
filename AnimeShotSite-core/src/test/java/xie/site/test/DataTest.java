package xie.site.test;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.common.json.XJsonUtil;
import xie.common.json.XObjectMapperFactoryBean;
import xie.module.spring.SpringUtil;

public class DataTest {
	public static void main(String[] args) throws Exception {
//		System.setProperty("spring.profiles.default", "development");
		System.setProperty("spring.profiles.default", "remoteProduct");

		// ObjectMapper xObjectMapper = SpringUtil.getBean("xObjectMapper");
		// System.out.println(xObjectMapper);
		//
		// XObjectMapperFactoryBean xObjectMapperFactoryBean = SpringUtil.getBean(XObjectMapperFactoryBean.class);
		// System.out.println(xObjectMapperFactoryBean);
		// System.out.println(xObjectMapperFactoryBean.getObject());
		//
		// xObjectMapperFactoryBean = SpringUtil.getBean("xObjectMapper");
		// System.out.println(xObjectMapperFactoryBean);
		//
		// System.out.println(XJsonUtil.getObjectMapper());

		ShotInfoService shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		List<ShotInfo> list = shotInfoService.getMasterRecommandShotList(1, 7, 44);

		System.out.println(list);

		ShotInfoDao shotInfoDao = SpringUtil.getBean(ShotInfoDao.class);
		List<Object[]> list2 = shotInfoDao.countRowNumberGroupByAnimeEpisodeId("2c9380825b5b7e41015b60f668390006");
		System.out.println(list2);
	}
}
