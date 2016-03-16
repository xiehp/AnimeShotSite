package xie.common.utils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import xie.common.date.DateUtil;
import xie.common.string.StringPool;
import xie.common.utils.props.PropsUtil;

public class UploadUtil {
	
	private final static String FILE_UPLOAD_PATH = PropsUtil.getProperty("file.upload.path"); 
	
	public final static String DEFAULT = "default";

	public static String[] getCreatePathWithSuffix(String realName,String sign) throws Exception{
		String type = realName.substring(realName.lastIndexOf(".") + 1);
		String suffix = "";
		if(!type.isEmpty()){
			suffix = StringPool.PERIOD + type;
		}
		String path = FILE_UPLOAD_PATH;
		
		String name = UUID.randomUUID().toString() + "-" + (int)(Math.random() * 10000) + suffix;
		
		String dateStr = DateUtil.convertToString(new Date(), DateUtil.YMD1);
		
		String relativePath = dateStr + StringPool.FORWARD_SLASH + (sign == null ? DEFAULT : sign) + StringPool.FORWARD_SLASH + name;
		
		path = path + StringPool.FORWARD_SLASH + relativePath;
		
		File file = new File((new File(path)).getParent());
		file.mkdirs();
		
		return new String[]{path,relativePath,name};
	}
	
}
