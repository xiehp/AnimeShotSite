package xie.animeshotsite.bo;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import xie.animeshotsite.db.entity.AutoRunParam;
import xie.animeshotsite.db.service.AutoRunParamService;
import xie.common.date.DateUtil;

/**
 * Created by xie on 2017/5/30.
 */
@Service
public class AutoRunParamBo {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
	AutoRunParamService autoRunParamService;

	/**
	 * 记录当前上传操作次数已满的时间点
	 */
	public void recordUploadFullInfo() {
		try {
			autoRunParamService.saveOrUpdateOneAutoRunParam(null, null, null, null, null, "贴图库上传次数已满的时间", "tietuku_upload_full_time", DateUtil.convertToString(new Date(), "yyyy-MM-dd HH"));
		} catch (Exception e) {
			log.error("设置tietuku_upload_full_time发生异常", e);
		}
	}

	/**
	 * 是否正处于等待上传次数已满的状态
	 */
	public boolean isWaitUploadFullUnlock() {
		String nowTimeStr = DateUtil.convertToString(new Date(), "yyyy-MM-dd HH");
		AutoRunParam autoRunParam = autoRunParamService.findByKey("tietuku_upload_full_time");
		String dbTimeStr = autoRunParam.getValue();
		if (nowTimeStr.equals(dbTimeStr)) {
			return true;
		} else {
			return false;
		}
	}
}
