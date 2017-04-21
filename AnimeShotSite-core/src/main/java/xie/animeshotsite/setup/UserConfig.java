package xie.animeshotsite.setup;

import xie.module.language.XELangLocal;

/** 用户自定义配置文件 */
public class UserConfig {

	private String siteCookieId;

	/** 用户设定是否不显示翻译 */
	private boolean notTranFlag;

	/** 用户设定的翻译语言 */
	private XELangLocal tranLanguage;

	/** 用户设定的翻译语言的颜色 */
	private String tranLanguageColor = "#39aa1d";

	/** 用户设定的翻译语言的字体大小 */
	private String tranLanFonsize;

	public String getSiteCookieId() {
		return siteCookieId;
	}

	public void setSiteCookieId(String siteCookieId) {
		this.siteCookieId = siteCookieId;
	}

	public boolean isNotTranFlag() {
		return notTranFlag;
	}

	public void setNotTranFlag(boolean notTranFlag) {
		this.notTranFlag = notTranFlag;
	}

	public XELangLocal getTranLanguage() {
		return tranLanguage;
	}

	public void setTranLanguage(XELangLocal tranLanguage) {
		this.tranLanguage = tranLanguage;
	}

	public String getTranLanguageColor() {
		return tranLanguageColor;
	}

	public void setTranLanguageColor(String tranLanguageColor) {
		this.tranLanguageColor = tranLanguageColor;
	}

	public String getTranLanFonsize() {
		return tranLanFonsize;
	}

	public void setTranLanFonsize(String tranLanFonsize) {
		this.tranLanFonsize = tranLanFonsize;
	}

}
