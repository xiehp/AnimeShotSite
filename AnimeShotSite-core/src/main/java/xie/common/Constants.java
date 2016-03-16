package xie.common;

import java.math.BigDecimal;

public class Constants {

	public static final BigDecimal ZERO = new BigDecimal(0);

	public static final String DEFAULT_SYSTEM_ENCODE = "UTF-8";
	public static final String UTF8_ENCODE = "UTF-8";
	public static final String ISO8859_ENCODE = "ISO8859-1";

	/**
	 * 未登录
	 */
	public static final String UNLOGIN_CODE = "00001";

	/**
	 * 操作成功
	 */
	public static final String SUCCESS_CODE = "10000";

	/**
	 * 操作失败
	 */
	public static final String FAIL_CODE = "10001";

	/**
	 * 默认管理员ID
	 */
	public static final String USER_MANAGE_LOGIN_ID = "admin";

	/**
	 * 默认分页
	 */
	public static final int FIRST_PAGE_NUM = 0;
	/**
	 * 默认分页数
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/** 是否标志 */
	public static final int FLAG_INT_YES = 1;
	public static final int FLAG_INT_NO = 0;
	/** 是否标志 */
	public static final String FLAG_STR_YES = "1";
	public static final String FLAG_STR_NO = "0";

	/** 显示标志 */
	public static final int IS_SHOW = 1;
	public static final int NOT_SHOW = 0;

	/** 用户类型 */
	public static final int USER_TYPE_FOREGROUND = 1;
	public static final int USER_TYPE_BACKGROUND = 2;

	/** 用户状态 */
	public static final int USER_STATUS_NORMAL = 1; // 正常
	public static final int USER_STATUS_LOCK = 2; // 锁定

	// 固定角色类型
	/** 固定角色类型 前台参与评级企业 */
	public static final String ROLE_ENTERPRISE = "ROLE_ENTERPRISE";

	/** 消息key */
	public static final String MESSAGE = "message";

	/** 错误key */
	public static final String ERROR = "error";

	/** 菜单KEY */
	public static final String MENU_KEY = "menu_key";

	/** Http key */
	public static final String HTTP_KEY = "success";
	public static final String HTTP_MESSAGE = "message";

	/** 菜单状态记录 */
	public static final String MENU_STATUA_KEY = "MENU_STATUA_KEY";
	public static final String SECOND_MENU_STATUA_KEY = "SECOND_MENU_STATUA_KEY";
	public static final String THIRD_MENU_STATUA_KEY = "THIRD_MENU_STATUA_KEY";
	public static final String FOURTH_MENU_STATUA_KEY = "FOURTH_MENU_STATUA_KEY";

	/** 默认可用资源权限 view */
	public static final String DEFAULT_BASIC_AVAILABLE_PERMISSION = "view";

	/** 默认可用资源等级 > 1 */
	public static final int DEFAULT_RESOURCE_AVAILABLE_LEVEL = 1;

	/**
	 * 资源等级
	 */
	public static final int RESOURCE_LEVEL_ONE = 1;
	public static final int RESOURCE_LEVEL_TWO = 2;
	public static final int RESOURCE_LEVEL_THREE = 3;
	public static final int RESOURCE_LEVEL_FOUR = 4;
	public static final int RESOURCE_LEVEL_FIVE = 5;

	// /**
	// * 默认区域顶级数id为100
	// */
	// public static final String DEFAULT_TOP_AREA_ID = PropsUtil.getProperty("default.top.area.id");
	// /**
	// * 低级区域等级 为 5
	// */
	// public final static int LOWEST_AREA_LEVEL = 5;

	/** 信息模板类型 */
	public static final Integer MS_TEMP_TYPE_SMS = 1; // 短信模板
	public static final Integer MS_TEMP_TYPE_EMAIL = 2; // 邮件模板

	/* 皮肤 */
	public static final String SKIN_DEFAULT = "default"; // 默认皮肤 黑色

	/** 删除标志 */
	public static final int IS_DELETE = 1;
	public static final int NOT_DELETE = 0;

}