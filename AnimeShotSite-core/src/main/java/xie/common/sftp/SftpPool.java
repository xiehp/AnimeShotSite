package xie.common.sftp;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Session;

public class SftpPool {
	private static final Logger LOG = LoggerFactory.getLogger(SftpPool.class);
	private GenericObjectPool<Session> pool;

	private static Map<String, SftpPool> instanceMap = new HashMap<String, SftpPool>();

	public static SftpPool getInstance(final String ftpId) {
		synchronized (SftpPool.class) {
			if (!instanceMap.containsKey(ftpId)) {
				instanceMap.put(ftpId, new SftpPool(ftpId));
			}
		}
		return instanceMap.get(ftpId);
	}

	@SuppressWarnings("unchecked")
	public SftpPool(final String ftpId) {
		final GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(SftpPropertyLoad.getInstance().getInt("ftp.pool.max.total"));
		config.setMaxIdle(SftpPropertyLoad.getInstance().getInt("ftp.pool.max.idle"));
		config.setMinIdle(SftpPropertyLoad.getInstance().getInt("ftp.pool.min.idle"));
		pool = new GenericObjectPool<Session>(new SftpPoolableObjectFactory(ftpId), config);
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	public Session newSession() {
		Session session = null;
		try {
			session = pool.borrowObject();
		} catch (Exception e) {
			LOG.error("newSession", e);
		}
		return session;
	}

	public void releaseSession(final Session session) {
		pool.returnObject(session);
	}

	public void close() {
		pool.close();
	}
}
