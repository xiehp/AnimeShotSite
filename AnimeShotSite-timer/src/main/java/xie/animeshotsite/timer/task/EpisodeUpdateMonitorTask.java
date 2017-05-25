package xie.animeshotsite.timer.task;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import xie.animeshotsite.timer.base.XBaseTask;
import xie.module.httpclient.XHttpClientUtils;

@Component
public class EpisodeUpdateMonitorTask extends XBaseTask {

	@Resource
	XHttpClientUtils httpClientUtils;
	
	
	@Override
	public void runTask(Map<String, Object> paramMap) throws Exception {
//		httpClientUtils.getHtml(url);
	}

	
	public static void main(String[] args) {
//		SpringUtils
	}
}
