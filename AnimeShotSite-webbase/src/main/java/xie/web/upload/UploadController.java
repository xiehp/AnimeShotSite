package xie.web.upload;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import xie.base.controller.BaseFunctionController;

@Controller
@RequestMapping(value="/ajaxUpload")
public class UploadController extends BaseFunctionController {
	
	@Override
	public Map<String, Object> getProcessResult(File file, String[] params) {
		
		Map<String, Object> resultMap = Maps.newHashMap();
		
		
		return resultMap;
	}

}
