package xie.base.controller;

import java.io.File;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import xie.common.utils.UploadUtil;

public class BaseFunctionController<M, ID extends Serializable> extends BaseController {
	
	protected Logger _log = LoggerFactory.getLogger(BaseFunctionController.class);
	
	private final String PARAM_NAME = "param";

	@RequestMapping(value="/uploadFile")
	public ResponseEntity<?> fileUpload(@RequestParam("fileUpload") CommonsMultipartFile file, @RequestParam String... params) {
		
		Map<String, Object> result = null;
		try{
			String fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
			
			String[] tempPathArr = UploadUtil.getCreatePathWithSuffix(fileName,UploadUtil.DEFAULT);
			
			String absolutePath = tempPathArr[0];
			
			File tempFile = new File(absolutePath);
			file.transferTo(tempFile);
			
			result = getProcessResult(tempFile, params);
			
		}catch(Exception e){
			_log.error("上传文件错误:{}", e.getCause());
		}
		
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value="/autoComplete")
	public ResponseEntity<?> autoComplete(@RequestBody Map<String, Object> params){
		List<String> list = getAutoCompleteList((String)params.get(PARAM_NAME));
		return new ResponseEntity<List<String>>(list, HttpStatus.OK);
	}
	
	@RequestMapping(value="/chosen")
	public ResponseEntity<?> chosen(@RequestBody Map<String, Object> params) {
		List<M> list = getChosenList(params);
		return new ResponseEntity<List<M>>(list, HttpStatus.OK);
	}
	
	public List<M> getChosenList(Map<String, Object> params){return null;}
	
	public List<String> getAutoCompleteList(String param){return null;}
	
	public Map<String, Object> getProcessResult(File tempFile, String[] params){return null;}
}
