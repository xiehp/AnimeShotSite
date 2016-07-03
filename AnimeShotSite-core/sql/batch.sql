-- 更新动画全称
update anime_info a1 set full_name = CONCAT(name,' ', ifnull(DIVISION_NAME,'')) ;

-- 更新剧集全称
update anime_episode a1 set DIVISION_NAME = ifnull(name, DIVISION_NAME);
update anime_episode a1 set a1.full_name = ifnull(CONCAT((select full_name from anime_info a2 where a1.anime_info_id = a2.id), ' ', a1.division_name), full_name);

-- 将时间戳更新到原始时间字段
update shot_info set original_time = time_stamp;

-- 查询可能重复的截图
SELECT 
	id
    -- ,ANIME_INFO_ID, ANIME_EPISODE_ID, ROUND(time_stamp / 1000, 1) * 1000, time_stamp
FROM
	shot_info
WHERE
	(ANIME_INFO_ID , ANIME_EPISODE_ID, time_stamp) IN (
		SELECT 
			ANIME_INFO_ID, ANIME_EPISODE_ID, ROUND(time_stamp / 1000, 1) * 1000
		FROM
			shot_info
		GROUP BY ANIME_INFO_ID , ANIME_EPISODE_ID , ROUND(time_stamp / 1000, 1) * 1000
		HAVING COUNT(time_stamp) > 1
	)

-- 更新截图为新的时间戳
UPDATE shot_info SET time_stamp = ROUND(time_stamp / 1000, 1) * 1000;


-- 更新所有字幕文件名
update subtitle_info


-- 显示时间为空的字段，设置为更新时间
update `anime_episode`  set SHOW_DATE = UPDATE_DATE where SHOW_DATE is null;