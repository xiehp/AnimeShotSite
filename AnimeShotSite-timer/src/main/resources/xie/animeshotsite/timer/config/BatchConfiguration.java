package xie.animeshotsite.timer.config;

import javax.annotation.Resource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import xie.animeshotsite.db.entity.AnimeInfo;

//@Configuration
//@EnableBatchProcessing
//@EnableAutoConfiguration
public class BatchConfiguration {

	@Resource
	private JobBuilderFactory jobs;

	@Resource
	private StepBuilderFactory steps;

	@Bean
	public Job job(@Qualifier("step1") Step step1, @Qualifier("step2") Step step2) {
		return jobs.get("myJob").start(step1).next(step2).build();
	}

	@Bean
	protected Step step1(ItemReader<AnimeInfo> reader, ItemProcessor<AnimeInfo, AnimeInfo> processor, ItemWriter<AnimeInfo> writer) {
		return steps.get("step1").<AnimeInfo, AnimeInfo> chunk(10).reader(reader).processor(processor).writer(writer).build();
	}

	@Bean
	protected Step step2(Tasklet tasklet) {
		return steps.get("step2").tasklet(tasklet).build();
	}
}