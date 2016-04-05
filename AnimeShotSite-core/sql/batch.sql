-- 更新动画全称
update anime_info a1 set full_name = CONCAT(name,' ', ifnull(DIVISION_NAME,'')) ;

-- 更新剧集全称
update anime_episode a1 set DIVISION_NAME = ifnull(name, DIVISION_NAME);
update anime_episode a1 set a1.full_name = ifnull(CONCAT((select full_name from anime_info a2 where a1.anime_info_id = a2.id), ' ', a1.division_name), full_name);








