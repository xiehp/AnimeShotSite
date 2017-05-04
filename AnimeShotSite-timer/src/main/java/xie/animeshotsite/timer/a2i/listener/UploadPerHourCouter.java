package xie.animeshotsite.timer.a2i.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xie.common.date.XTimeUtils;
import xie.common.utils.XWaitTime;

@Component
public class UploadPerHourCouter {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private int maxUploadPerHour = 295;

	private int nowUploadPerHour = 0;

	private XWaitTime uploadPerHourWait = new XWaitTime(XTimeUtils.getNeedTimeNextHour() + 5000);

	/**
	 * 增加次数，同时判断当前是否刷新上传次数，以及暂停操作
	 */
	public void addCount() {
		// 判断当前是否刷新上传次数
		if (uploadPerHourWait.isTimeout()) {
			nowUploadPerHour = 0;
			uploadPerHourWait.resetNowtime();
		}

		// 上传前判断是否已经超限
		nowUploadPerHour++;
		if (nowUploadPerHour > maxUploadPerHour) {
			logger.info("达到最大每小时上传限制" + maxUploadPerHour + ", 等待到下个整点。");
			try {
				long sleepTime = XTimeUtils.getNeedTimeNextHour() + 5000;
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nowUploadPerHour = 1;
			uploadPerHourWait.resetNowtime();
		}
	}
}
