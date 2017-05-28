package xie.animeshotsite.timer.job;

import java.util.List;
import java.util.TimerTask;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;

import xie.animeshotsite.db.entity.AnimeInfo;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.spring.SpringUtil;

public class AnimeShotJob extends TimerTask {

	@Resource
	AnimeInfoDao animeInfoDao;

	public AnimeShotJob() {
		animeInfoDao = (AnimeInfoDao) SpringUtil.getBean("animeInfoDao");
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		System.out.println("afterJob");
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("BatchStatus.COMPLETED");
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			System.out.println("BatchStatus.FAILED");
		}
	}

	@Override
	public void run() {
		List<AnimeInfo> list = animeInfoDao.findByProcessAction(AnimeInfo.PROCESS_ACTION_FULL);
		System.err.println(list);
	}

}
