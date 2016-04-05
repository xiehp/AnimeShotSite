ALTER TABLE `anime_info` 
ADD COLUMN `SUMMARY` VARCHAR(2000) NULL COMMENT '动画摘要' AFTER `TITLE_URL_ID`;

ALTER TABLE `anime_episode` 
ADD COLUMN `SUMMARY` VARCHAR(2000) NULL COMMENT '剧集摘要' AFTER `TITLE_URL_ID`;


ALTER TABLE `anime_info` ADD COLUMN `SECOND_NAME` VARCHAR(100) NULL AFTER `NAME`;
ALTER TABLE `anime_info` CHANGE COLUMN `SECOND_NAME` `SECOND_NAME` VARCHAR(100) NULL COMMENT '副标题' ;
ALTER TABLE `anime_info` ADD COLUMN `SORT` INT(1) NULL DEFAULT NULL COMMENT '排序' AFTER `SUMMARY`;
ALTER TABLE `anime_info` ADD COLUMN `DIVISION_NAME` VARCHAR(20) NULL DEFAULT NULL COMMENT '第几季名称' AFTER `SECOND_NAME`;


ALTER TABLE `anime_episode` ADD COLUMN `DIVISION_NAME` VARCHAR(20) NULL DEFAULT NULL COMMENT '第几集名称' AFTER `NAME`;
ALTER TABLE `anime_episode` ADD COLUMN `TITLE` VARCHAR(100) NULL COMMENT '剧集标题' AFTER `DIVISION_NAME`;

ALTER TABLE `anime_info` ADD COLUMN `FULL_NAME` VARCHAR(100) NULL DEFAULT NULL COMMENT 'NAME+空格+DIVISION_NAME' AFTER `DIVISION_NAME`;
ALTER TABLE `anime_info` CHANGE COLUMN `NAME` `NAME` VARCHAR(50) NULL DEFAULT NULL COMMENT '名称' ;
ALTER TABLE `anime_info` CHANGE COLUMN `SECOND_NAME` `SECOND_NAME` VARCHAR(50) NULL DEFAULT NULL COMMENT '副标题' ;
ALTER TABLE `anime_episode` 
CHANGE COLUMN `NAME` `NAME` VARCHAR(50) NULL COMMENT '剧集名称' ,
CHANGE COLUMN `TITLE` `TITLE` VARCHAR(50) NULL DEFAULT NULL COMMENT '剧集标题' ,
ADD COLUMN `FULL_NAME` VARCHAR(100) NULL COMMENT '剧集全称=动画全称+空格+DIVISION_NAME' AFTER `TITLE`;


-----------------------------以上为已更新sql---------------------------------------
-----------------------------以上为已更新sql---------------------------------------
-----------------------------以上为已更新sql---------------------------------------
-----------------------------以上为已更新sql---------------------------------------
-----------------------------以上为已更新sql---------------------------------------
-----------------------------以下为未更新sql---------------------------------------
-----------------------------以下为未更新sql---------------------------------------
-----------------------------以下为未更新sql---------------------------------------
-----------------------------以下为未更新sql---------------------------------------
-----------------------------以下为未更新sql---------------------------------------



ALTER TABLE `animeshottest`.`anime_episode` 
CHANGE COLUMN `SHOT_STATUS` `SHOT_STATUS` INT(1) NULL DEFAULT '0' COMMENT '截图状态 0：未截图 1：部分截图 2：已截图' ,
CHANGE COLUMN `PROCESS_ACTION` `PROCESS_ACTION` INT(1) NULL DEFAULT '0' COMMENT '处理动作 0：不做任何事情 1：完整截图 2：只截缺少部分' ;
















