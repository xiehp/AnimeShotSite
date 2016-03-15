package xie.animeshotsite.timer.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("beforeJob");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("afterJob");
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("BatchStatus.COMPLETED");
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			System.out.println("BatchStatus.FAILED");
		}
	}

}
