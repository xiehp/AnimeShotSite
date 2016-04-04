package xie.animeshotsite.timer.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xie.animeshotsite.db.entity.ShotTask;
import xie.animeshotsite.db.repository.ShotTaskDao;
import xie.animeshotsite.db.service.AnimeEpisodeService;
import xie.animeshotsite.db.service.AnimeInfoService;
import xie.animeshotsite.db.service.ShotTaskService;
import xie.animeshotsite.spring.SpringUtil;
import xie.animeshotsite.timer.base.XTask;
import xie.common.string.XStringUtils;
import xie.common.utils.JsonUtil;

@Component
public class ShotTaskTimer2 extends TimerTask {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AnimeInfoService animeInfoService;
	@Autowired
	AnimeEpisodeService animeEpisodeService;

	@Autowired
	ShotTaskService shotTaskService;
	@Autowired
	ShotTaskDao shotTaskDao;

	@Autowired
	EntityManager entityManager;

	@Override
	public void run() {
		
		// entityManager.getEntityManagerFactory().getCache().evictAll();
		// sessionFactory.getCache().evictEntityRegions();

		long count = shotTaskDao.count();
		List<ShotTask> list = shotTaskService.findNeedRunTask();

		System.out.println("list size:" + list.size());
		System.out.println("count size:" + count);
		
		
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
