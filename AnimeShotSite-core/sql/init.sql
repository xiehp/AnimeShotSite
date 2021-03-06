CREATE TABLE `gif_info` (
  `ID` varchar(32) NOT NULL,
  `STATUS` int(1) DEFAULT NULL,
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) DEFAULT NULL,
  `DELETE_FLAG` int(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `comment_record` (
  `ID` varchar(32) NOT NULL,
  `CLASS1` varchar(45) NOT NULL COMMENT '分类1',
  `CLASS2` varchar(45) DEFAULT NULL COMMENT '分类2',
  `CLASS3` varchar(45) DEFAULT NULL COMMENT '分类3',
  `CLASS4` varchar(45) DEFAULT NULL COMMENT '分类4',
  `TARGET_ID` varchar(32) DEFAULT NULL COMMENT '评论所属对象ID',
  `CONTENT` varchar(5000) NOT NULL COMMENT '评论内容',
  `COMMENT_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '评论时间',
  `REPLY_COMMENT_ID` varchar(32) DEFAULT NULL COMMENT '回复评论ID',
  `USER_ID` varchar(32) DEFAULT NULL COMMENT '评论人ID 空则匿名',
  `USER_NAME` varchar(200) DEFAULT NULL COMMENT '评论人名字 空则匿名',
  `COOKIE_ID` varchar(100) DEFAULT NULL,
  `STATUS` int(1) NOT NULL DEFAULT '1',
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) NOT NULL DEFAULT '0',
  `DELETE_FLAG` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论记录';

CREATE TABLE `common_record` (
  `ID` varchar(32) NOT NULL,
  `USER_ID` varchar(32) DEFAULT NULL COMMENT '所属用户ID',
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '用户名字',
  `COOKIE_ID` varchar(100) DEFAULT NULL COMMENT '所属CookieID',
  `TYPE` varchar(50) NOT NULL COMMENT '记录类型',
  `NAME` varchar(200) NOT NULL COMMENT '记录名称',
  `STATUS` int(1) NOT NULL DEFAULT '1',
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) NOT NULL DEFAULT '0',
  `DELETE_FLAG` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通用记录';

CREATE TABLE `ma_damage` (
  `ID` varchar(32) NOT NULL,
  `COMMON_RECORD_ID` varchar(32) NOT NULL COMMENT '通用记录ID',
  `ADVANCED_SETTING_FLAG` varchar(1) DEFAULT NULL COMMENT '是否使用高级设置',
  `PANEL_VALUE` int(11) DEFAULT NULL COMMENT '最终面板数值',
  `CHAIN_NUMBER` int(11) DEFAULT NULL COMMENT 'chain数量',
  `physical_Def` int(11) DEFAULT NULL COMMENT '物理防御',
  `magic_Def` int(11) DEFAULT NULL COMMENT '魔法防御',
  `special_Def` int(11) DEFAULT NULL COMMENT '耐性',
  `special_Def_huo` int(11) DEFAULT NULL COMMENT '耐性火',
  `special_Def_feng` int(11) DEFAULT NULL COMMENT '耐性风',
  `special_Def_shui` int(11) DEFAULT NULL COMMENT '耐性水',
  `special_Def_guang` int(11) DEFAULT NULL COMMENT '耐性光',
  `special_Def_an` int(11) DEFAULT NULL COMMENT '耐性暗',
  `CARD_COST` int(11) DEFAULT NULL COMMENT '卡牌Cost',
  `card_element_attribute_percent` int(11) DEFAULT NULL COMMENT '卡牌颜色增益百分比 默认为200%',
  `card_Physical_Percent` int(11) DEFAULT NULL,
  `card_Magic_Percent` int(11) DEFAULT NULL,
  `card_Life_Percent` int(11) DEFAULT NULL COMMENT '卡牌吃血量数值百分比',
  `card_Value_Percent` int(11) DEFAULT NULL COMMENT '卡牌特定条件提升百分比',
  `card_Penetrate_Percent` int(11) DEFAULT NULL COMMENT '卡牌穿防百分比',
  `card_Chain_Up_Flag` varchar(1) DEFAULT NULL COMMENT '卡牌是否有chainUp',
  `card_Attack_Times` int(11) DEFAULT NULL COMMENT '卡牌攻击次数',
  `ex_Physical_Percent` int(11) DEFAULT NULL COMMENT 'ex物理补正百分比',
  `ex_Magic_Percent` int(11) DEFAULT NULL COMMENT 'ex魔法补正百分比',
  `people_Life_Value` int(11) DEFAULT NULL COMMENT '人物基础血量数值',
  `people_Physical_Value` int(11) DEFAULT NULL COMMENT '人物基础物理数值',
  `people_Magic_Value` int(11) DEFAULT NULL COMMENT '人物基础魔法数值',
  `people_Recovery_Value` int(11) DEFAULT NULL COMMENT '人物基础回复数值',
  `buff_Life_Value` int(11) DEFAULT NULL COMMENT '增益血量数值',
  `buff_Physical_Value` int(11) DEFAULT NULL COMMENT '增益物理数值',
  `buff_Magic_Value` int(11) DEFAULT NULL COMMENT '增益魔法数值',
  `buff_Recovery_Value` int(11) DEFAULT NULL COMMENT '增益回复数值',
  `max_Buff_Value` int(11) DEFAULT NULL COMMENT '最大增益数值',
  `card_Type` varchar(32) DEFAULT NULL COMMENT '卡牌类型 1:物理 2:魔法',
  `card_Color` varchar(32) DEFAULT NULL COMMENT '卡牌颜色 1:火 2:水 3:风 4:光 5:暗',
  `card_base_Value` int(11) DEFAULT NULL COMMENT '卡牌数值',
  `STATUS` int(1) DEFAULT '1',
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) DEFAULT '0',
  `DELETE_FLAG` int(1) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='MA伤害数值';

CREATE TABLE `auto_run_param` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(200) DEFAULT NULL COMMENT '参数表示名',
  `KEY` varchar(200) DEFAULT NULL COMMENT 'key',
  `VALUE` varchar(4000) DEFAULT NULL COMMENT '值',
  `ANIME_INFO_ID` varchar(32) DEFAULT NULL COMMENT '动画ID',
  `ANIME_EPISODE_ID` varchar(32) DEFAULT NULL COMMENT '剧集ID',
  `REF_ID` varchar(32) DEFAULT NULL COMMENT '所属对象的ID',
  `REF_TYPE` varchar(100) DEFAULT NULL COMMENT '所属对象类型，用于表示REF_ID是哪种对象',
  `STATUS` int(1) DEFAULT NULL,
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `DELETE_FLAG` int(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自动化运行的参数';

CREATE TABLE `collect` (
  `ID` varchar(32) NOT NULL,
  `USER_ID` varchar(32) DEFAULT NOT NULL COMMENT '所属用户ID',
  `COLLECT_NAME` varchar(512) DEFAULT NOT NULL COMMENT '收藏名',
  `COLLECT_COUNT` int(11) NULL COMMENT '收藏数量',
  `STATUS` int(1) DEFAULT NULL,
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `DELETE_FLAG` int(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收藏分组表';

CREATE TABLE `user_collect` (
  `ID` varchar(32) NOT NULL,
  `USER_ID` varchar(32) DEFAULT NOT NULL COMMENT '所属用户ID',
  `COLLECT_ID` varchar(32) DEFAULT NOT NULL COMMENT '收藏ID',
  `ANIME_INFO_ID` varchar(32) DEFAULT NULL COMMENT '动画ID',
  `ANIME_EPISODE_ID` varchar(32) DEFAULT NULL COMMENT '剧集ID',
  `SHOT_ID` varchar(32) DEFAULT NULL COMMENT '截图ID',
  `GIF_ID` varchar(32) DEFAULT NULL COMMENT '动态图ID',
  `STATUS` int(1) DEFAULT NULL,
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `DELETE_FLAG` int(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收藏表';


CREATE TABLE `subtitle_search_history` (
  `ID` varchar(32) NOT NULL,
  `ANIME_INFO_ID` varchar(32) DEFAULT NULL COMMENT '动画ID',
  `ANIME_EPISODE_ID` varchar(32) DEFAULT NULL COMMENT '剧集ID',
  `SEARCH_TEXT` varchar(45) NOT NULL COMMENT '搜索文本',
  `SEARCH_COUNT` varchar(45) DEFAULT NULL COMMENT '搜索次数',
  `FIRST_DATE` timestamp NOT NULL COMMENT '首次搜索时间',
  `COOKIE_ID` varchar(100) DEFAULT NULL,
  `STATUS` int(1) NOT NULL DEFAULT '1',
  `CREATE_BY` varchar(32) DEFAULT NULL,
  `CREATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_BY` varchar(32) DEFAULT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `VERSION` int(11) NOT NULL DEFAULT '0',
  `DELETE_FLAG` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字幕搜索历史';
