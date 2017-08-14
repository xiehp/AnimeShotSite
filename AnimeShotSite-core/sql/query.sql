-- 获得百分比
SELECT 
episode.FULL_NAME, episode.id

, a.name, a.remark, a.param_key, a.value, a.message

, c.name, c.remark, c.param_key, c.value

, d.name, d.remark, d.param_key, d.value

, b.name, b.remark, b.param_key, b.value

FROM anime_episode episode

left outer join auto_run_param a on 
a.ANIME_EPISODE_ID = episode.id and a.param_key = 'video_download_do_download_flag' 

left outer join auto_run_param b on 
b.ANIME_EPISODE_ID = episode.id and b.param_key = 'video_download_completed_percent' 

left outer join auto_run_param c on 
c.ANIME_EPISODE_ID = episode.id and c.param_key = 'video_download_do_download_url' 

left outer join auto_run_param d on 
d.ANIME_EPISODE_ID = episode.id and d.param_key = 'video_download_torrent_file_path' 

where 
1=1
and a.name is not null
and a.VALUE <> 4

order by episode.FULL_NAME ;



-- 还在监视中的动画
SELECT 
anime_info.FULL_NAME

, auto_run_param.name, auto_run_param.remark, auto_run_param.param_key, auto_run_param.value, auto_run_param.message
,auto_run_param.*
from anime_info, auto_run_param
where 1=1
and anime_info.id = auto_run_param.ANIME_INFO_ID
and auto_run_param.ANIME_EPISODE_ID is null
and auto_run_param.param_key = 'video_download_monitor_do_flag'

and value = 1

order by anime_info.FULL_NAME 
;


-- 获取一个自动运行参数
SELECT * FROM auto_run_param a where a.anime_Episode_Id = 'XXXXXX' and a.param_key = 'XXXXXX' ;


