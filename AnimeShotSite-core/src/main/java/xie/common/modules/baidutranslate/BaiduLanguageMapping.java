package xie.common.modules.baidutranslate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.jcraft.jsch.jce.MD5;

import xie.common.Constants;

public class BaiduLanguageMapping {
	public static final Map<String, String> LANGUAGE_MAPPING = new HashMap<>();

	static {
		LANGUAGE_MAPPING.put(Constants.LANGUAGE_ZH_CN.toLowerCase(), "zh");
		LANGUAGE_MAPPING.put(Constants.LANGUAGE_ZH_TW.toLowerCase(), "cht");
		LANGUAGE_MAPPING.put(Constants.LANGUAGE_JA.toLowerCase(), "jp");
		LANGUAGE_MAPPING.put(Constants.LANGUAGE_EN_US.toLowerCase(), "en");
		LANGUAGE_MAPPING.put(Constants.LANGUAGE_AR.toLowerCase(), "ara");
	}

	public static Map<String, String> getMap() {
		return LANGUAGE_MAPPING;
	}

	public static String getBaiduSign(String appid, String queryText, String salt, String appkey) {
		return DigestUtils.md5Hex(appid + queryText + salt + appkey);
	}
}
