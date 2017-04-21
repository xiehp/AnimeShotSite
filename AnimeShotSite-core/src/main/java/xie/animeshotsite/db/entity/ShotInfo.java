package xie.animeshotsite.db.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import xie.common.date.DateUtil;

@Entity
@Table(name = ShotInfo.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShotInfo extends BaseTietukuUrl {

	private static final long serialVersionUID = 1L;
	public static final String COLUMN_PUBLIC_LIKE_COUNT = "publicLikeCount";
	public static final String COLUMN_MEMBER_LIKE_COUNT = "memberLikeCount";
	public static final String COLUMN_PUBLIC_SHARE_COUNT = "publicShareCount";
	public static final String COLUMN_MASTER_RECOMMEND_RANK = "masterRecommendRank";
	public static final String COLUMN_MASTER_RECOMMEND_DATE = "masterRecommendDate";

	public static final String TABLE_NAME = "shot_info";

	/** 动画信息的id */
	private String animeInfoId;

	@Transient
	private AnimeInfo animeInfo;

	/** 动画剧集ID */
	private String animeEpisodeId;

	@Transient
	private AnimeEpisode animeEpisode;

	/** 原始视频时间戳 */
	private Long originalTime;

	/** 预计获取的时间戳，时间戳以该字段为准 */
	private Long timeStamp;

	/** 时间偏移，新生成的图片可能和第一次片源不一样 */
	private Long timeOffset;

	/** ？？？未知 */
	private Integer type;

	/** 名称 */
	private String name;

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

	/**
	 * 获取 动画信息的id.
	 *
	 * @return 动画信息的id
	 */
	public String getAnimeInfoId() {
		return animeInfoId;
	}

	/**
	 * 设置 动画信息的id.
	 *
	 * @param animeInfoId 动画信息的id
	 */
	public void setAnimeInfoId(String animeInfoId) {
		this.animeInfoId = animeInfoId;
	}

	/**
	 * 获取 动画剧集ID.
	 *
	 * @return 动画剧集ID
	 */
	public String getAnimeEpisodeId() {
		return animeEpisodeId;
	}

	/**
	 * 设置 动画剧集ID.
	 *
	 * @param animeEpisodeId 动画剧集ID
	 */
	public void setAnimeEpisodeId(String animeEpisodeId) {
		this.animeEpisodeId = animeEpisodeId;
	}

	/**
	 * 获得anime info.
	 *
	 * @return anime info
	 */
	public AnimeInfo getAnimeInfo() {
		return animeInfo;
	}

	/**
	 * 设置anime info.
	 *
	 * @param animeInfo anime info
	 */
	public void setAnimeInfo(AnimeInfo animeInfo) {
		this.animeInfo = animeInfo;
	}

	/**
	 * 获得anime episode.
	 *
	 * @return anime episode
	 */
	public AnimeEpisode getAnimeEpisode() {
		return animeEpisode;
	}

	/**
	 * 设置anime episode.
	 *
	 * @param animeEpisode anime episode
	 */
	public void setAnimeEpisode(AnimeEpisode animeEpisode) {
		this.animeEpisode = animeEpisode;
	}

	/**
	 * 获取 原始视频时间戳.
	 *
	 * @return 原始视频时间戳
	 */
	public Long getOriginalTime() {
		return originalTime;
	}

	/**
	 * 设置 原始视频时间戳.
	 *
	 * @param originalTime 原始视频时间戳
	 */
	public void setOriginalTime(Long originalTime) {
		this.originalTime = originalTime;
	}

	/**
	 * 获取 预计获取的时间戳，时间戳以该字段为准.
	 *
	 * @return 预计获取的时间戳
	 */
	public Long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 获得formated min sec.
	 *
	 * @return mm:ss
	 */
	public String getFormatedMinSec() {
		return DateUtil.formatTime(timeStamp, 0);
	}

	/**
	 * 获得formated micro sec.
	 *
	 * @return SSS
	 */
	public String getFormatedMicroSec() {
		return DateUtil.formatTime(timeStamp, 1);
	}

	/**
	 * 获得formated time.
	 *
	 * @return mm:ss:SSS
	 */
	public String getFormatedTime() {
		return DateUtil.formatTime(timeStamp, 2);
	}

	/**
	 * 获得formated time china.
	 *
	 * @return "mm分ss秒SSS", SSS为0时省略
	 */
	public String getFormatedTimeChina() {
		return DateUtil.formatTime(timeStamp, 3);
	}

	/**
	 * 设置 预计获取的时间戳.
	 *
	 * @param timeStamp 预计获取的时间戳
	 */
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * 获取 时间偏移，新生成的图片可能和第一次片源不一样.
	 *
	 * @return 时间偏移，新生成的图片可能和第一次片源不一样
	 */
	public Long getTimeOffset() {
		return timeOffset;
	}

	/**
	 * 设置 时间偏移，新生成的图片可能和第一次片源不一样.
	 *
	 * @param timeOffset 时间偏移，新生成的图片可能和第一次片源不一样
	 */
	public void setTimeOffset(Long timeOffset) {
		this.timeOffset = timeOffset;
	}

	/**
	 * 获取 ？？？未知.
	 *
	 * @return ？？？未知
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置 ？？？未知.
	 *
	 * @param type ？？？未知
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取 名称.
	 *
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 名称.
	 *
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取 站长推荐等级.
	 *
	 * @return 站长推荐等级
	 */
	public Long getMasterRecommendRank() {
		return masterRecommendRank;
	}

	/**
	 * 设置 站长推荐等级.
	 *
	 * @param masterRecommendRank 站长推荐等级
	 */
	public void setMasterRecommendRank(Long masterRecommendRank) {
		this.masterRecommendRank = masterRecommendRank;
	}

	/**
	 * 获取 站长推荐日期.
	 *
	 * @return 站长推荐日期
	 */
	public Date getMasterRecommendDate() {
		return masterRecommendDate;
	}

	/**
	 * 设置 站长推荐日期.
	 *
	 * @param masterRecommendDate 站长推荐日期
	 */
	public void setMasterRecommendDate(Date masterRecommendDate) {
		this.masterRecommendDate = masterRecommendDate;
	}

	/**
	 * 获取 公共喜欢次数.
	 *
	 * @return 公共喜欢次数
	 */
	public Long getPublicLikeCount() {
		return publicLikeCount;
	}

	/**
	 * 设置 公共喜欢次数.
	 *
	 * @param publicLikeCount 公共喜欢次数
	 */
	public void setPublicLikeCount(Long publicLikeCount) {
		this.publicLikeCount = publicLikeCount;
	}

	/**
	 * 获取 会员喜欢次数.
	 *
	 * @return 会员喜欢次数
	 */
	public Long getMemberLikeCount() {
		return memberLikeCount;
	}

	/**
	 * 设置 会员喜欢次数.
	 *
	 * @param memberLikeCount 会员喜欢次数
	 */
	public void setMemberLikeCount(Long memberLikeCount) {
		this.memberLikeCount = memberLikeCount;
	}

	/**
	 * 获取 公共分享次数.
	 *
	 * @return 公共分享次数
	 */
	public Long getPublicShareCount() {
		return publicShareCount;
	}

	/**
	 * 设置 公共分享次数.
	 *
	 * @param publicShareCount 公共分享次数
	 */
	public void setPublicShareCount(Long publicShareCount) {
		this.publicShareCount = publicShareCount;
	}

	/**
	 * 获取 会员收藏次数.
	 *
	 * @return 会员收藏次数
	 */
	public Long getMemberFavoriteCount() {
		return memberFavoriteCount;
	}

	/**
	 * 设置 会员收藏次数.
	 *
	 * @param memberFavoriteCount 会员收藏次数
	 */
	public void setMemberFavoriteCount(Long memberFavoriteCount) {
		this.memberFavoriteCount = memberFavoriteCount;
	}

	/**
	 * 获取 本地所在文件名.
	 *
	 * @return 本地所在文件名
	 */
	public String getLocalFileName() {
		return localFileName;
	}

	/**
	 * 设置 本地所在文件名.
	 *
	 * @param localFileName 本地所在文件名
	 */
	public void setLocalFileName(String localFileName) {
		this.localFileName = localFileName;
	}

}
