package xie.sample.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(value = "sampleTimer")
public class SampleTimer {

	public SampleTimer() {
		System.out.println("SampleTimer !###########################! ");
	}

	private Logger _log = LoggerFactory.getLogger(SampleTimer.class);
	
	public void init(){
		_log.warn("例子线程已启动，请注意！");
	}
	
}
