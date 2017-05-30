package xie.animeshotsite.timer.timer;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xie.common.constant.XConst;

public class BaseTaskTimer extends TimerTask {

	protected Logger _log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void run() {
		try {
			taskTimer();
		} catch (Exception e) {
			_log.error("执行发生异常，暂停10分钟。", e);
			try {
				Thread.sleep(XConst.SECOND_10_MIN * 1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	protected void taskTimer() throws Exception {

	}
}
