package xie.sys.restful.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Component
public class BaseRest {

	private  RestTemplate restTemplate;
	protected  RestTemplate httpClientRestTemplate;
	private  HttpComponentsClientHttpRequestFactory httpClientRequestFactory;
	
	public RestTemplate getRestTemplate(){
		return restTemplate;
	}
	
	public RestTemplate getHttpClientRestTemplate(){
		return httpClientRestTemplate;
	}
	
	@PostConstruct
	public  void initResource(){
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(20000);
        requestFactory.setConnectTimeout(20000);
 
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		
//		// 默认使用JDK Connection
//		restTemplate = new RestTemplate();
//		// (optional)设置20秒超时
//		((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(20000);
//
		// 设置使用HttpClient4.0
		httpClientRestTemplate = new RestTemplate();
		httpClientRequestFactory = new HttpComponentsClientHttpRequestFactory();
		// (optional)设置20秒超时
		httpClientRequestFactory.setConnectTimeout(20000);
		httpClientRestTemplate.setRequestFactory(httpClientRequestFactory);
	}
	
	@PreDestroy
	public void close(){
		try {
			// 退出时关闭HttpClient4连接池中的连接
			httpClientRequestFactory.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	
}
