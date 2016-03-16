package xie.common.sftp;

import java.util.Properties;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import xie.common.exception.WebRuntimeException;

@SuppressWarnings("rawtypes")
public class SftpPoolableObjectFactory implements PooledObjectFactory {
	private String ftpServer;
	private int ftpPort;
	private String ftpUserName;
	private String ftpPassword;
	private int connectTimeout;

	public SftpPoolableObjectFactory(final String ftpId) {
		super();
		ftpServer = SftpPropertyLoad.getInstance().getString("".equals(ftpId) ? "ftp.service.url" : "ftp." + ftpId + ".service.url");
		ftpPort = SftpPropertyLoad.getInstance().getInt("".equals(ftpId) ? "ftp.service.port" : "ftp." + ftpId + ".service.port");
		ftpUserName = SftpPropertyLoad.getInstance().getString("".equals(ftpId) ? "ftp.service.username" : "ftp." + ftpId + ".service.username");
		ftpPassword = SftpPropertyLoad.getInstance().getString("".equals(ftpId) ? "ftp.service.password" : "ftp." + ftpId + ".service.password");
		connectTimeout = SftpPropertyLoad.getInstance().getInt("".equals(ftpId) ? "ftp.service.connect.timeout" : "ftp." + ftpId + ".service.connect.timeout");
	}

	@Override
	public void activateObject(PooledObject obj) throws Exception {
		final Session sshSession = (Session) obj.getObject();
		if (!sshSession.isConnected()) {
			// 登录服务器，设置登陆超时时间
			sshSession.connect(connectTimeout);
		}
	}

	@Override
	public void destroyObject(PooledObject obj) throws Exception {
		final Session sshSession = (Session) obj.getObject();
		sshSession.disconnect();
	}

	@Override
	public PooledObject<Session> makeObject() throws Exception {
		final JSch jsch = new JSch();

		// 连接服务器
		final Session sshSession = jsch.getSession(ftpUserName, ftpServer, ftpPort);
		// 如果服务器连接不上，则抛出异常
		if (sshSession == null) {
			throw new WebRuntimeException("连接服务器失败, server:" + ftpServer + ", user:" + ftpUserName + ", port:" + ftpPort);
		}
		// 设置登陆主机的密码
		sshSession.setPassword(ftpPassword);

		final Properties sshConfig = new Properties();
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);

		return new DefaultPooledObject<Session>(sshSession);
	}

	@Override
	public void passivateObject(PooledObject obj) throws Exception {
		// final Session sshSession = (Session) obj.getObject();
		// sshSession.disconnect();
	}

	@Override
	public boolean validateObject(PooledObject obj) {
		final Session sshSession = (Session) obj.getObject();
		return sshSession.isConnected();
	}

}
