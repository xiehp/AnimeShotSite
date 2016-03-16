package xie.site.test;

import java.util.List;

import xie.animeshotsite.db.entity.ShotInfo;
import xie.animeshotsite.db.service.ShotInfoService;
import xie.animeshotsite.spring.SpringUtil;

public class DataTest {
	public static void main(String[] args) {
		ShotInfoService shotInfoService = SpringUtil.getBean(ShotInfoService.class);
		List<ShotInfo> list = shotInfoService.getMasterRecommandShotList(7, 44);
		
		System.out.println(list);
	}
}
