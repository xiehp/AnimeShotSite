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

-- 针对诸神字幕组中日合并字幕，中文字幕读取时去除日文字幕
update subtitle_info set FILTER_REMOVE = 'Style#jp'
where 1=1 
and ANIME_INFO_ID in ('f39c57f455b0c65c0155c814c555014f','f39c57f455b0c65c0155bde6d2fe00df','f39c57f455b0c65c0155bdd7e74100de', 'f39c57f45580ccaf0155afc8a6ae001c', '4028e381545d157b01545d5b9fd80000')
-- and language = '4'
-- and language = 'jp'
-- and language = 'sc'
-- and language = '1000'
and (language = '4' or language = 'sc')
;


-- 删除乖离页面的垃圾数据
update comment_record set delete_flag = 1 where user_name = 'angelina' or content = 'angelina' or user_name = 'hacker' or content = '88888';
update common_record set delete_flag = 1 where user_name = 'angelina' or user_name = 'hacker' or user_name = '88888' or NAME = '88888';


-- 更新字幕信息是否已录入字幕字段
update subtitle_info set SUB_IN_STATUS = 0;
update subtitle_info aaa set SUB_IN_STATUS = 1
where exists (select * from subtitle_line where subtitle_line.SUBTITLE_INFO_ID = aaa.id limit 1);


-- 删除繁体字幕
delete from subtitle_line where SUBTITLE_INFO_ID in (select id from subtitle_info where ANIME_INFO_ID='f39c57f45580ccaf0155aff0c4410054' and LANGUAGE = 4)  ;
delete  from subtitle_info where ANIME_INFO_ID='f39c57f45580ccaf0155aff0c4410054' and LANGUAGE = 4;



-- 更新贴图库地址
SELECT * FROM animeshotsiteinst1.shot_info where TIETUKU_URL_PREFIX like '%https://i2.muimg.com%';
update shot_info set TIETUKU_URL_PREFIX='http://i2.muimg.com/541950/' where TIETUKU_URL_PREFIX = 'https://i2.muimg.com/541950/';
update gif_info set TIETUKU_URL_PREFIX='http://i2.muimg.com/541950/' where TIETUKU_URL_PREFIX = 'https://i2.muimg.com/541950/';
update image_url set TIETUKU_URL_PREFIX='http://i2.muimg.com/541950/' where TIETUKU_URL_PREFIX = 'https://i2.muimg.com/541950/';


