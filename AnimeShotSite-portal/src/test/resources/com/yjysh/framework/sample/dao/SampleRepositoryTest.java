package com.yjysh.framework.sample.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.yjysh.framework.base.page.PageInfo;
import com.yjysh.framework.common.utils.DateUtil;
import com.yjysh.framework.sample.entity.Sample;
import com.yjysh.framework.sample.repository.SampleRepository;
import com.yjysh.framework.spring.SpringContextTestCase;

@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext.xml","/applicationContext-framework.xml","/projectContext.xml","/deployContext.xml" })
public class SampleRepositoryTest  extends SpringContextTestCase {
	
	@Resource
	SampleRepository sampleRepository;
	
	@Test
	public void listTest() throws Exception {
		Date startDate = DateUtil.convertFromString("2015-10-1", DateUtil.YMD1);
		List<Sample> list = sampleRepository.findSampleByCreateDate(startDate);
		for(Sample s : list) {
			System.out.println(s.toString());
		}
	}
	
	@Test
	public void add(){
		for(int i=0; i<100; i++) {
			Sample s = new Sample();
			s.setParam1("p1"+i);
			s.setParam2(new BigDecimal(i));
			sampleRepository.save(s);
		}
	}
	
	@Test
	public void findByPage(){
		PageInfo pageInfo = new PageInfo(1,10,Direction.ASC);
		Page<Sample> samplePage = sampleRepository.findSample(pageInfo.getPageRequestInfo());
		for (Sample sample : samplePage.getContent()) {
			System.out.println(sample);
		}
	}
	
	@Test
	public void modify1(){
		Sample s1 = sampleRepository.findOne("4028198151f178450151f17859880000");
		
		s1.setParam1("111111");
		sampleRepository.save(s1);
	}
	
	@Test
	public void modify2(){
		Sample s1 = sampleRepository.findOne("4028198151f178450151f17859880000");
		
		s1.setParam1("2222222");
		sampleRepository.save(s1);
	}
	
	@Test
	public void queryTest(){
		List<String> list = sampleRepository.findByParam1("p1");
		for (String string : list) {
			System.out.println(string);
		}
	}
}
