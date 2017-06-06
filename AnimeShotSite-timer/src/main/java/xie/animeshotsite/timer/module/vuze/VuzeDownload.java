package xie.animeshotsite.timer.module.vuze;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.gudy.azureus2.core3.disk.DiskManagerFileInfo;
import org.gudy.azureus2.core3.disk.DiskManagerFileInfoSet;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.global.GlobalManagerDownloadRemovalVetoException;
import org.gudy.azureus2.core3.torrent.TOTorrent;
import org.gudy.azureus2.core3.torrent.TOTorrentException;
import org.gudy.azureus2.core3.torrent.TOTorrentFactory;
import org.springframework.util.Assert;

import com.aelitis.azureus.core.AzureusCore;
import com.aelitis.azureus.core.AzureusCoreException;
import com.aelitis.azureus.core.AzureusCoreFactory;

public class VuzeDownload {

	private VuzeDownload() {
	}

	private static VuzeDownload vuzeDownload;

	public static synchronized VuzeDownload getInstance() {
		if (vuzeDownload == null) {
			vuzeDownload = new VuzeDownload();
			vuzeDownload.init();
		}
		return vuzeDownload;
	}

	private AzureusCore core;
	private Map<String, TOTorrent> toTorrentMap = new LinkedHashMap<>();
	private Map<String, DownloadManager> downloadManagerMap = new LinkedHashMap<>();

	private void init() {
		core = AzureusCoreFactory.create();
		core.start();
	}

	public AzureusCore getAzureusCore() {
		return core;
	}

	public TOTorrent getTOTorrent(File torrentFile) throws TOTorrentException {
		TOTorrent toTorrent = toTorrentMap.get(torrentFile.getAbsolutePath());
		if (toTorrent == null) {
			toTorrent = TOTorrentFactory.deserialiseFromBEncodedFile(torrentFile);
			toTorrentMap.put(torrentFile.getAbsolutePath(), toTorrent);
		}
		return toTorrent;
	}

	public DownloadManager getDownloadManager(File torrentFile) throws TOTorrentException {
		DownloadManager dm = downloadManagerMap.get(torrentFile.getAbsolutePath());
		if (dm == null) {
			TOTorrent toTorrent = getTOTorrent(torrentFile);
			dm = core.getGlobalManager().getDownloadManager(toTorrent);
			downloadManagerMap.put(torrentFile.getAbsolutePath(), dm);
		}

		return dm;
	}

	public void removeDownloadManager(File torrentFile) throws AzureusCoreException, GlobalManagerDownloadRemovalVetoException, TOTorrentException {
		DownloadManager dm = getDownloadManager(torrentFile);
		if (dm != null) {
			core.getGlobalManager().removeDownloadManager(dm);
		}
	}

	public DownloadManager addTask(File torrentFile, File videoPath) {
		DownloadManager dm = core.getGlobalManager().addDownloadManager(torrentFile.getAbsolutePath(), videoPath.getAbsolutePath());

		Assert.notNull(dm, "创建bt下载任务失败");

		return dm;
	}

	public int getActivationCount(File torrentFile) throws TOTorrentException {
		return getDownloadManager(torrentFile).getActivationCount();
	}

	public int getPercentDoneExcludingDND(File torrentFile) throws TOTorrentException {
		return getDownloadManager(torrentFile).getStats().getPercentDoneExcludingDND();
	}

	public static final String[] videoTypes = new String[] { ".mkv", ".mp4" };

	public File getVideoFile(File torrentFile) throws TOTorrentException {
		DownloadManager dm = getDownloadManager(torrentFile);
		DiskManagerFileInfoSet fileSet = dm.getDiskManagerFileInfoSet();
		DiskManagerFileInfo[] fileInfos = fileSet.getFiles();
		if (fileInfos != null && fileInfos.length > 0) {
			for (DiskManagerFileInfo info : fileInfos) {
				File file = info.getFile(true);
				for (String videoType : videoTypes) {
					if (file.getName().endsWith(videoType)) {
						return file;
					}
				}
			}
		}

		return null;
	}

	public void stop() {
		core.stop();
	}
}
