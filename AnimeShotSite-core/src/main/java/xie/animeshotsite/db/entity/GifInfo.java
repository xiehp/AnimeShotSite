package xie.animeshotsite.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.tietuku.entity.util.TietukuUtils;

import xie.common.date.DateUtil;

@Entity
@Table(name = GifInfo.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GifInfo extends BaseTietukuUrl {

	private static final long serialVersionUID = 1L;
	public static final String COLUMN_PUBLIC_LIKE_COUNT = "publicLikeCount";
	public static final String COLUMN_MEMBER_LIKE_COUNT = "memberLikeCount";
	public static final String COLUMN_PUBLIC_SHARE_COUNT = "publicShareCount";
	public static final String COLUMN_MASTER_RECOMMEND_RANK = "masterRecommendRank";
	public static final String COLUMN_MASTER_RECOMMEND_DATE = "masterRecommendDate";

	public static final String TABLE_NAME = "gif_info";

	/** 动画信息的id */
	private String animeInfoId;

	@Transient
	private AnimeInfo animeInfo;

	/** 动画剧集ID */
	private String animeEpisodeId;

	@Transient
	private AnimeEpisode animeEpisode;

	/** 预计获取的时间戳 */
	private Long timeStamp;

	/** 持续时间 */
	private Long continueTime;

	/** 时间偏移，新生成的图片可能和第一次片源不一样 */
	private Long timeOffset;

	/** ？？？未知 */
	private Integer type;

	/** 名称 */
	private String name;

	/** 宽 */
	private Integer width;

	/** 高 */
	private Integer height;

	/** 站长推荐等级 */
	private Long masterRecommendRank;

	/** 站长推荐日期 */
	private Date masterRecommendDate;

	/** 公共喜欢次数 */
	private Long publicLikeCount;

	/** 会员喜欢次数 */
	private Long memberLikeCount;

	/** 公共分享次数 */
	private Long publicShareCount;

	/** 会员收藏次数 */
	private Long memberFavoriteCount;

	/** 本地所在文件名 */
	private String localFileName;

	public String getAnimeInfoId() {
		return animeInfoId;
	}

	public void setAnimeInfoId(String animeInfoId) {
		this.animeInfoId = animeInfoId;
	}

	public String getAnimeEpisodeId() {
		return animeEpisodeId;
	}

	public void setAnimeEpisodeId(String animeEpisodeId) {
		this.animeEpisodeId = animeEpisodeId;
	}

	public AnimeInfo getAnimeInfo() {
		return animeInfo;
	}

	public void setAnimeInfo(AnimeInfo animeInfo) {
		this.animeInfo = animeInfo;
	}

	public AnimeEpisode getAnimeEpisode() {
		return animeEpisode;
	}

	public void setAnimeEpisode(AnimeEpisode animeEpisode) {
		this.animeEpisode = animeEpisode;
	}



	public Long getContinueTime() {
		return continueTime;
	}

	public void setContinueTime(Long continueTime) {
		this.continueTime = continueTime;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @return mm:ss
	 */
	public String getFormatedMinSec() {
		return DateUtil.formatTime(timeStamp, 0);
	}

	/**
	 * @return SSS
	 */
	public String getFormatedMicroSec() {
		return DateUtil.formatTime(timeStamp, 1);
	}

	/**
	 * @return mm:ss:SSS
	 */
	public String getFormatedTime() {
		return DateUtil.formatTime(timeStamp, 2);
	}

	/**
	 * @return "mm分ss秒SSS", SSS为0时省略
	 */
	public String getFormatedTimeChina() {
		return DateUtil.formatTime(timeStamp, 3);
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Long getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(Long timeOffset) {
		this.timeOffset = timeOffset;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getMasterRecommendRank() {
		return masterRecommendRank;
	}

	public void setMasterRecommendRank(Long masterRecommendRank) {
		this.masterRecommendRank = masterRecommendRank;
	}

	public Date getMasterRecommendDate() {
		return masterRecommendDate;
	}

	public void setMasterRecommendDate(Date masterRecommendDate) {
		this.masterRecommendDate = masterRecommendDate;
	}

	public Long getPublicLikeCount() {
		return publicLikeCount;
	}

	public void setPublicLikeCount(Long publicLikeCount) {
		this.publicLikeCount = publicLikeCount;
	}

	public Long getMemberLikeCount() {
		return memberLikeCount;
	}

	public void setMemberLikeCount(Long memberLikeCount) {
		this.memberLikeCount = memberLikeCount;
	}

	public Long getPublicShareCount() {
		return publicShareCount;
	}

	public void setPublicShareCount(Long publicShareCount) {
		this.publicShareCount = publicShareCount;
	}

	public Long getMemberFavoriteCount() {
		return memberFavoriteCount;
	}

	public void setMemberFavoriteCount(Long memberFavoriteCount) {
		this.memberFavoriteCount = memberFavoriteCount;
	}

	public String getLocalFileName() {
		return localFileName;
	}

	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}


	/**
	 * 获得tietuku O url.
	 *
	 * @return tietuku O url
	 */
	public String getTietukuOUrl() {
		return TietukuUtils.getImageOriginalUrl(getTietukuUrlPrefix(), getTietukuUrlId(), ".gif");
	}
}
