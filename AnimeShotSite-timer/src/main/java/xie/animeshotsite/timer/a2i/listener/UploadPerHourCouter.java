package xie.animeshotsite.timer.a2i.listener;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import xie.common.date.XTimeUtils;
import xie.common.utils.XWaitTime;

@Component
public class UploadPerHourCouter {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int maxUploadPerHour = 295;

	private int nowUploadPerHour = 0;

	private XWaitTime uploadPerHourWait = new XWaitTime(XTimeUtils.getNeedTimeNextHour() + 150000);

	public void addCount() {
		addCount(maxUploadPerHour);
	}

	/**
	 * 增加次数，同时判断当前是否刷新上传次数，以及暂停操作
	 */
	public void addCount(int maxUploadPerHour) {

		// 判断当前是否刷新上传次数
		if (uploadPerHourWait.isTimeout()) {
			logger.info("时间到，刷新上传次数，当前时间:{}", new Date());
			nowUploadPerHour = 0;
			uploadPerHourWait.resetNowtime();
			uploadPerHourWait.setTimeout(XTimeUtils.getNeedTimeNextHour() + 150000);
		}

		// 上传前判断是否已经超限
		nowUploadPerHour++;
		logger.info("当前次数:{}，当前时间:{}", nowUploadPerHour, new Date());
		if (nowUploadPerHour > maxUploadPerHour) {
			logger.info("达到最大每小时上传限制" + maxUploadPerHour + ", 等待到下个整点。");
			try {
				long sleepTime = XTimeUtils.getNeedTimeNextHour() + 150000;
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nowUploadPerHour = 1;
			uploadPerHourWait.resetNowtime();
			uploadPerHourWait.setTimeout(XTimeUtils.getNeedTimeNextHour() + 150000);
		}
	}
}
