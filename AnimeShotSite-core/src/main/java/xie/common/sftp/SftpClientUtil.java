package xie.common.sftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import xie.common.exception.WebRuntimeException;
import xie.common.utils.string.StringUtil;

public class SftpClientUtil {

	private static final Logger LOG = LoggerFactory.getLogger(SftpClientUtil.class);

	private static final String FTP_ERROR_MESSAGE = "FTP发生错误";

	protected Session session;
	protected boolean isShareSession;
	protected int channelTimeout;
	protected SftpPool sftpPool;
	protected String filenameEncode;

	public SftpClientUtil(final String ftpId, final boolean isShareSession) {
		channelTimeout = SftpPropertyLoad.getInstance().getInt("".equals(ftpId) ? "ftp.service.channel.timeout" : "ftp." + ftpId + ".service.channel.timeout");
		filenameEncode = SftpPropertyLoad.getInstance().getString("".equals(ftpId) ? "ftp.service.filename.encode" : "ftp." + ftpId + ".service.filename.encode");
		filenameEncode = filenameEncode == null ? "UTF-8" : filenameEncode;
		this.isShareSession = isShareSession;
		this.sftpPool = SftpPool.getInstance(ftpId);
	}

	public SftpClientUtil(final boolean isShareSession) {
		this("", isShareSession);
	}

	public SftpClientUtil(final String ftpId) {
		this(ftpId, false);
	}

	public SftpClientUtil() {
		this("", false);
	}

	public static SftpClientUtil createRatingSftp() {
		return new SftpClientUtil("rating");
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	public SftpPool getSftpPool() {
		return sftpPool;
	}

	public void closePool() {
		sftpPool.close();
	}

	public ChannelSftp openChannel() {
		if (!isShareSession) {
			session = sftpPool.newSession();
		}

		ChannelSftp channelSftp = null;

		try {
			final Channel channel = session.openChannel("sftp");
			channel.connect(channelTimeout);
			channelSftp = (ChannelSftp) channel;
			channelSftp.setFilenameEncoding(filenameEncode);
		} catch (JSchException e) {
			LOG.error(FTP_ERROR_MESSAGE, e);
		} catch (SftpException e) {
			LOG.error(FTP_ERROR_MESSAGE, e);
		}

		return channelSftp;
	}

	public void closeChannel(final ChannelSftp channelSftp) {
		if (channelSftp != null) {
			channelSftp.disconnect();
		}

		if (!isShareSession) {
			if (session != null) {
				sftpPool.releaseSession(session);
			}
		}
	}

	protected String concat(final String finalPath, final String fileName) {
		return StringUtil.replace(FilenameUtils.concat(finalPath, fileName), "\\", "/");
	}

	/**
	 * 上传单个文件到服务器的牧歌目录中
	 * 
	 * @param remoteDirectory
	 * @param localFilePathName
	 */
	public void upload(final String remoteDirectory, final String localFilePathName) {
		final ChannelSftp sftp = openChannel();
		try {
			createDirecroty(remoteDirectory, sftp);
			sftp.cd(remoteDirectory);
			sftp.put(localFilePathName, FilenameUtils.getName(localFilePathName));
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE + remoteDirectory + localFilePathName, e);
		} finally {
			closeChannel(sftp);
		}
	}

	public void upload(final String remoteDirectory, final InputStream is, final String fileName) {
		final ChannelSftp sftp = openChannel();
		try {
			createDirecroty(remoteDirectory, sftp);
			sftp.cd(remoteDirectory);
			sftp.put(is, fileName);
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			closeChannel(sftp);
		}
	}

	public void upload(final String remoteDirectory, final byte[] bytes, final String fileName) {
		final ChannelSftp sftp = openChannel();
		OutputStream os = null;
		try {
			createDirecroty(remoteDirectory, sftp);
			sftp.cd(remoteDirectory);
			os = sftp.put(fileName);
			os.write(bytes);
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} catch (final IOException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					LOG.error(FTP_ERROR_MESSAGE, e);
				}
			}
			closeChannel(sftp);
		}
	}

	public void createDirecroty(final String remote) {
		final ChannelSftp sftp = openChannel();
		try {
			createDirecroty(remote, sftp);
		} finally {
			closeChannel(sftp);
		}
	}

	protected void createDirecroty(final String remote, final ChannelSftp sftp) {
		final String[] dirs = remote.split("/");
		try {
			sftp.cd("/");
			for (int i = 1; i < dirs.length; i++) {
				if (!dirs[i].contains(".")) {
					boolean dirExists = isExsitDir(dirs[i], sftp);
					if (!dirExists) {
						sftp.mkdir(dirs[i]);
						sftp.cd(dirs[i]);
					}
				}
			}
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		}
	}

	protected boolean isExsitDir(final String directory, final ChannelSftp sftp) {
		boolean exist = false;
		try {
			sftp.cd(directory);
			exist = true;
		} catch (final SftpException e) {

		}
		return exist;
	}

	protected boolean isExsit(final String pathOrFileName, final ChannelSftp sftp) {
		boolean exist = false;
		try {
			sftp.lstat(pathOrFileName);
			exist = true;
		} catch (final SftpException e) {

		}
		return exist;
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param filePath
	 */
	public void delete(final String filePath) {
		final ChannelSftp sftp = openChannel();
		try {
			deleteInternal(filePath, sftp);
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			closeChannel(sftp);
		}
	}

	@SuppressWarnings("unchecked")
	private void deleteInternal(final String filePath, final ChannelSftp sftp) throws SftpException {
		if (isExsitDir(filePath, sftp)) {
			final Vector<LsEntry> vector = sftp.ls(filePath);
			for (LsEntry entry : vector) {
				final String fname = entry.getFilename();
				if (!(".".equals(fname) || "..".equals(fname))) {
					deleteInternal(concat(filePath, fname), sftp);
				}
			}
			sftp.rmdir(filePath);
		} else {
			sftp.rm(filePath);
		}
	}

	/**
	 * 移动文件或目录 参数为：文件对文件，目录对目录
	 * 
	 * @param fromFilePath
	 * @param toFilePath
	 */
	public void move(final String fromFilePath, final String toFilePath) {
		final ChannelSftp sftp = openChannel();
		try {
			moveInternal(fromFilePath, toFilePath, sftp);
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			closeChannel(sftp);
		}
	}

	@SuppressWarnings("unchecked")
	private void moveInternal(final String fromPath, final String toPath, final ChannelSftp sftp) throws SftpException {
		final SftpATTRS attr = sftp.lstat(fromPath);
		if (attr.isDir()) {
			if (FilenameUtils.getPath(fromPath).equals(FilenameUtils.getPath(toPath)) && (!isExsitDir(toPath, sftp))) {
				sftp.rename(fromPath, toPath);
			} else {
				final Vector<LsEntry> vector = sftp.ls(fromPath);
				for (LsEntry entry : vector) {
					final String fname = entry.getFilename();
					if (!(".".equals(fname) || "..".equals(fname))) {
						moveInternal(concat(fromPath, fname), concat(toPath, fname), sftp);
					}
				}
				sftp.rmdir(fromPath);
			}
		} else {
			createDirecroty(toPath, sftp);
			sftp.rename(fromPath, toPath);
		}
	}

	public byte[] getFileData(final String filePath) throws IOException {
		final ChannelSftp sftp = openChannel();
		InputStream ins = null;
		byte[] returnByte = null;

		try {
			ins = sftp.get(filePath);
			returnByte = FileCopyUtils.copyToByteArray(ins);
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			if (ins != null) {
				ins.close();
			}

			closeChannel(sftp);
		}

		return returnByte;
	}

	private void uploadFileInternal(final String localFileName, final String remoteFileName, final ChannelSftp sftp) throws SftpException {
		final String remotePath = "/" + FilenameUtils.getPath(remoteFileName);
		createDirecroty(remotePath, sftp);
		sftp.cd(remotePath);
		sftp.put(localFileName, FilenameUtils.getName(remoteFileName));

	}

	private void uploadFolderInternal(final String localPath, final String remotePath, final ChannelSftp sftp) throws SftpException {
		createDirecroty(remotePath, sftp);
		sftp.cd(remotePath);
		final File file = new File(localPath); // 在此目录中找文件
		final File filelist[] = file.listFiles();

		for (final File fileTemp : filelist) {
			if (fileTemp.isDirectory()) {
				uploadFolderInternal(localPath + "/" + fileTemp.getName(), remotePath + "/" + fileTemp.getName(), sftp);
			} else {
				uploadFileInternal(localPath + "/" + fileTemp.getName(), remotePath + "/" + fileTemp.getName(), sftp);
			}
		}
	}

	/**
	 * 上传文件或目录 参数为：文件对文件，目录对目录
	 * 
	 * @param localFileName
	 * @param remoteFileName
	 */
	public void uploadFileOrFolder(final String localFileName, final String remoteFileName) {
		final ChannelSftp sftp = openChannel();
		try {
			final File file = new File(localFileName); // 在此目录中找文件

			if (file.isDirectory()) {
				uploadFolderInternal(localFileName, remoteFileName, sftp);
			} else {
				uploadFileInternal(localFileName, remoteFileName, sftp);
			}
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			closeChannel(sftp);
		}
	}

	/**
	 * 下载文件或目录到本地目录中
	 * 
	 * @param remoteFilePath
	 * @param localPath
	 */
	public void download(final String remoteFilePath, final String localPath) {
		final ChannelSftp sftp = openChannel();
		try {
			downloadInternal(remoteFilePath, localPath, sftp);
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			closeChannel(sftp);
		}
	}

	@SuppressWarnings("unchecked")
	private void downloadInternal(final String remoteFilePath, final String localPath, final ChannelSftp sftp) throws SftpException {
		final SftpATTRS attr = sftp.lstat(remoteFilePath);
		try {
			FileUtils.forceMkdir(new File(localPath));
		} catch (IOException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		}
		if (attr.isDir()) {
			final Vector<LsEntry> vector = sftp.ls(remoteFilePath);
			for (final LsEntry entry : vector) {
				final String fname = entry.getFilename();
				if (!(".".equals(fname) || "..".equals(fname))) {
					downloadInternal(concat(remoteFilePath, fname), entry.getAttrs().isDir() ? concat(localPath, fname) : localPath, sftp);
				}
			}
		} else {
			sftp.get(remoteFilePath, localPath);
		}
	}

	@SuppressWarnings("unchecked")
	public List<LsEntry> list(final String remoteFilePath) {
		final ChannelSftp sftp = openChannel();
		try {
			final List<LsEntry> entryList = new ArrayList<LsEntry>();
			final Vector<LsEntry> vector = sftp.ls(remoteFilePath);
			for (final LsEntry entry : vector) {
				final String fname = entry.getFilename();
				if (!(".".equals(fname) || "..".equals(fname))) {
					entryList.add(entry);
				}
			}
			return entryList;
		} catch (final SftpException e) {
			throw new WebRuntimeException(FTP_ERROR_MESSAGE, e);
		} finally {
			closeChannel(sftp);
		}
	}
}
