package xie.web.protal.controller.other.ma;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.utils.SiteUtils;
import xie.base.controller.BaseController;
import xie.base.page.PageRequestUtil;
import xie.common.Constants;
import xie.common.date.DateUtil;
import xie.common.number.XNumberUtils;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.common.web.util.ConstantsWeb;
import xie.other.ma.db.entity.CommentRecord;
import xie.other.ma.db.entity.CommonRecord;
import xie.other.ma.db.entity.MaDamage;
import xie.other.ma.db.repository.CommonRecordDao;
import xie.other.ma.db.repository.MaDamageDao;
import xie.other.ma.db.service.CommentRecordService;
import xie.other.ma.db.service.CommonRecordService;
import xie.other.ma.db.service.MaDamageService;

@Controller
public class MaDamageCalcController extends BaseController {

	@Resource
	CommonRecordDao commonRecordDao;
	@Resource
	MaDamageDao maDamageDao;
	@Resource
	CommonRecordService commonRecordService;
	@Resource
	MaDamageService maDamageService;
	@Resource
	CommentRecordService commentRecordService;
	@Resource
	EntityCache entityCache;

	protected String getJspFileRootPath() {
		return "/";
	};

	@RequestMapping(value = "/ma/doCalc")
	public String doMaCalc(
			@RequestParam(value = "sortType", defaultValue = "sort") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		return getUrlRedirectPath("/maCalc");
	}

	@RequestMapping(value = "/maCalc/{id}")
	public String showSingle(
			@PathVariable String id,
			Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		MaDamage maDamage = null;
		CommonRecord commonRecord = null;
		if (id != null) {
			maDamage = maDamageDao.findByCommonRecordId(id);
			commonRecord = commonRecordDao.findById(id);
		}
		if (maDamage == null || commonRecord == null) {
			return getUrlRedirectPath("/maCalc");
		}

		// 开始计算
		long result1 = calcDamage(maDamage, false);
		long result2 = calcDamage(maDamage, true);

		// 将数据返回给页面
		setPageData(request, response, id);

		request.setAttribute("commonRecord", commonRecord);
		request.setAttribute("maDamage", maDamage);

		if (request.getAttribute("userName") == null) {
			request.setAttribute("userName", SiteUtils.getSiteCookieUserName(request));
		}
		if (request.getAttribute("cookieUserName") == null) {
			request.setAttribute("cookieUserName", SiteUtils.getSiteCookieUserName(request));
		}

		request.setAttribute("result1", result1);
		request.setAttribute("result2", result2);

		request.setAttribute("CommentRecordClass2", CommentRecord.CLASS2_MA_SINGLE);
		request.setAttribute("isSinglePage", true);

		return getJspFilePath("other/ma/damageCalc");
	}

	@RequestMapping(value = "/maCalc")
	public String maCalc(
			@RequestBody(required = false) MaDamage requestMaDamage,
			@RequestParam(value = "sortType", defaultValue = "sort") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, HttpServletRequest request, HttpServletResponse response)
					throws Exception {
		// 获取参数
		boolean advancedSettingFlag = getBoolean(request, "advancedSettingFlag"); // 是否使用高级设置
		// boolean saveFlag = getBoolean(request, "saveFlag"); // 是否使用高级设置

		// 开始计算
		long result1 = calcDamage(requestMaDamage, false);
		long result2 = calcDamage(requestMaDamage, true);

		String saveFlag = request.getParameter("saveFlag");
		if ("1".equals(saveFlag)) {
			// 保存提交的数据记录
			String siteCookieId = SiteUtils.getSiteCookieId(request);
			String userName = request.getParameter("userName");
			if (userName != null) {
				XCookieUtils.addCookieValue(response, ConstantsWeb.SITE_COOKIE_USER_NAME, userName);
				request.setAttribute("userName", userName);
				request.setAttribute("cookieUserName", userName);
			}
			String recordName = request.getParameter("name");
			// 提交主记录
			CommonRecord commonRecord = new CommonRecord();
			commonRecord.setCookieId(siteCookieId);
			commonRecord.setUserName(userName);
			commonRecord.setName(XStringUtils.isBlank(recordName) ? "数据" + DateUtil.convertToString(new Date(), DateUtil.YMD_FULL3) : recordName);
			commonRecord.setType(CommonRecord.TYPE_MA);
			commonRecord = commonRecordService.save(commonRecord);

			// 提交详细记录
			MaDamage maDamage = new MaDamage();
			requestMaDamage.setCommonRecordId(commonRecord.getId());
			maDamage.copyFromWithOutBaseInfo(requestMaDamage);
			// maDamage.setCommonRecordId(commonRecord.getId());
			// maDamage.setChainNumber(chainNumber);
			// TODO 详细记录
			maDamageService.save(maDamage);

			// 清除缓存
			entityCache.remove("commonRecordPageAll");
			entityCache.remove("commonRecordPageSelf_" + siteCookieId);
		}

		// 将数据返回给页面
		setPageData(request, response, null);

		request.setAttribute("maDamage", requestMaDamage);
		if (request.getAttribute("userName") == null) {
			request.setAttribute("userName", SiteUtils.getSiteCookieUserName(request));
		}
		if (request.getAttribute("cookieUserName") == null) {
			request.setAttribute("cookieUserName", SiteUtils.getSiteCookieUserName(request));
		}

		request.setAttribute("result1", result1);
		request.setAttribute("result2", result2);

		request.setAttribute("CommentRecordClass2", CommentRecord.CLASS2_MA_MAIN);

		return getJspFilePath("other/ma/damageCalc");
	}

	private void setPageData(HttpServletRequest request, HttpServletResponse response, String commonRecordId) throws UnsupportedEncodingException {
		// 显示计算记录列表
		{
			// 所有计算记录
			Page<CommonRecord> commonRecordPageAll = entityCache.get("commonRecordPageAll");
			if (commonRecordPageAll == null) {
				Map<String, Object> searchParamsAll = new HashMap<>();
				searchParamsAll.put("EQ_" + CommonRecord.COLUMN_TYPE, CommonRecord.TYPE_MA);
				commonRecordPageAll = commonRecordService.searchPageByParams(searchParamsAll, 1, 50, PageRequestUtil.SORT_TYPE_AUTO, CommonRecord.class);
				entityCache.put("commonRecordPageAll", commonRecordPageAll);
			}
			request.setAttribute("commonRecordPageAll", commonRecordPageAll);

			// 自己的计算记录
			String cookieId = SiteUtils.getSiteCookieId(request);
			if (cookieId != null) {
				Page<CommonRecord> commonRecordPageSelf = entityCache.get("commonRecordPageSelf_" + cookieId);
				if (commonRecordPageSelf == null) {
					Map<String, Object> searchParamsSelf = new HashMap<>();
					searchParamsSelf.put("EQ_" + CommonRecord.COLUMN_TYPE, CommonRecord.TYPE_MA);
					searchParamsSelf.put("EQ_" + CommonRecord.COLUMN_COOKIE_ID, cookieId);
					commonRecordPageSelf = commonRecordService.searchPageByParams(searchParamsSelf, 1, 50, PageRequestUtil.SORT_TYPE_AUTO, CommonRecord.class);
					entityCache.put("commonRecordPageSelf_" + cookieId, commonRecordPageSelf);
				}
				request.setAttribute("commonRecordPageSelf", commonRecordPageSelf);
			}
		}

		// 显示评论列表
		{
			// 主页面评论
			Page<CommentRecord> commentPageMain = entityCache.get("commentPageMain");
			if (commentPageMain == null) {
				Map<String, Object> searchParamsMain = new HashMap<>();
				searchParamsMain.put("EQ_" + CommentRecord.COLUMN_CLASS1, CommentRecord.CLASS1_MA);
				searchParamsMain.put("EQ_" + CommentRecord.COLUMN_CLASS2, CommentRecord.CLASS2_MA_MAIN);
				commentPageMain = commentRecordService.searchPageByParams(searchParamsMain, 1, 50, PageRequestUtil.SORT_TYPE_AUTO, CommentRecord.class);
				entityCache.put("commentPageMain", commentPageMain);
			}
			request.setAttribute("commentPageMain", commentPageMain);

			// 全部评论记录
			Page<CommentRecord> commentPageALl = entityCache.get("commentPageALl");
			if (commentPageALl == null) {
				Map<String, Object> searchParamsAll = new HashMap<>();
				searchParamsAll.put("EQ_" + CommentRecord.COLUMN_CLASS1, CommentRecord.CLASS1_MA);
				commentPageALl = commentRecordService.searchPageByParams(searchParamsAll, 1, 50, PageRequestUtil.SORT_TYPE_AUTO, CommentRecord.class);
				entityCache.put("commentPageALl", commentPageALl);
			}
			request.setAttribute("commentPageALl", commentPageALl);

			// 单个伤害评论
			if (commonRecordId != null) {
				Page<CommentRecord> commentPageSingle = entityCache.get("commentPageSingle_" + commonRecordId);
				if (commentPageSingle == null) {
					Map<String, Object> searchParamsSingle = new HashMap<>();
					searchParamsSingle.put("EQ_" + CommentRecord.COLUMN_CLASS1, CommentRecord.CLASS1_MA);
					searchParamsSingle.put("EQ_" + CommentRecord.COLUMN_CLASS2, CommentRecord.CLASS2_MA_SINGLE);
					searchParamsSingle.put("EQ_" + CommentRecord.COLUMN_TARGET_ID, commonRecordId);
					commentPageSingle = commentRecordService.searchPageByParams(searchParamsSingle, 1, 50, PageRequestUtil.SORT_TYPE_AUTO, CommentRecord.class);
					entityCache.put("commentPageSingle_" + commonRecordId, commentPageSingle);
				}
				request.setAttribute("commentPageSingle", commentPageSingle);
			}
		}
	}

	/**
	 * 计算面板伤害
	 */
	private long calcPanelDamage(MaDamage requestMaDamage, boolean 是否计算暴击) {
		Long panelDamage = requestMaDamage.getCardBaseValue();
		if (panelDamage == null || panelDamage == 0) {
			return 0;
		}
		return 0;
	}

	/**
	 * 计算数据
	 * 
	 * @param requestMaDamage
	 * @param 是否计算暴击
	 * @return
	 */
	private long calcDamage(MaDamage requestMaDamage, boolean 是否计算暴击) {
		if (requestMaDamage != null) {
			// 开始计算
			long 真实面板 = getLong(requestMaDamage.getPanelValue(), 1);
			long chainNumber = getLong(requestMaDamage.getChainNumber(), 1);
			double chainPerValue = Constants.FLAG_STR_YES.equals(requestMaDamage.getCardChainUpFlag()) ? 0.3 : 0.2;
			double 卡牌穿防百分比 = getLong(requestMaDamage.getCardPenetratePercent(), 0);
			double 叠chain倍率 = 1 + (chainNumber - 1) * chainPerValue;
			double 暴击倍率 = 是否计算暴击 ? 1.5 : 1;
			double 属性吸收率 = (double) getLong(requestMaDamage.getCardElementAttributePercent(), 200) / 100;
			double 锁定百分比 = (double) getLong(requestMaDamage.getTargetingPercent(), 0) / 100;

			long 真实默认防御 = getLong(requestMaDamage.getDefaultDef());
			long 真实物理防御 = getLong(requestMaDamage.getPhysicalDef());
			long 真实魔法防御 = getLong(requestMaDamage.getMagicDef());
			long 真实防御 = 0;
			if (XStringUtils.isBlank(requestMaDamage.getCardType())) {
				真实防御 = 真实默认防御;
			} else {
				if ("物理".equals(requestMaDamage.getCardType()) && requestMaDamage.getPhysicalDef() != null) {
					真实防御 = 真实物理防御;
				} else if ("魔法".equals(requestMaDamage.getCardType()) && requestMaDamage.getMagicDef() != null) {
					真实防御 = 真实魔法防御;
				}
			}
			long 真实耐性 = getLong(requestMaDamage.getSpecialDef());
			long 真实耐性火 = getLong(requestMaDamage.getSpecialDefHuo());
			long 真实耐性水 = getLong(requestMaDamage.getSpecialDefShui());
			long 真实耐性风 = getLong(requestMaDamage.getSpecialDefFeng());
			long 真实耐性光 = getLong(requestMaDamage.getSpecialDefGuang());
			long 真实耐性暗 = getLong(requestMaDamage.getSpecialDefAn());
			double ex默认补正 = getLong(requestMaDamage.getExDefaultPercent());
			double ex总物理补正 = getLong(requestMaDamage.getExPhysicalPercent());
			double ex总魔法补正 = getLong(requestMaDamage.getExMagicPercent());
			double ex补正 = 0;
			if (XStringUtils.isBlank(requestMaDamage.getCardType())) {
				ex补正 = ex默认补正;
			} else {
				if ("物理".equals(requestMaDamage.getCardType()) && requestMaDamage.getExPhysicalPercent() != null) {
					ex补正 = ex总物理补正;
				} else if ("魔法".equals(requestMaDamage.getCardType()) && requestMaDamage.getExMagicPercent() != null) {
					ex补正 = ex总魔法补正;
				}
			}
			ex补正 = ex补正 / 100;

			// 结果
			double result11 = 真实面板 + (long) (真实面板 * ex补正);
			double result12 = (long) result11 + (long) ((long) result11 * 锁定百分比);
			double result13 = (long) result12 + (long) ((long) result12 * (叠chain倍率 - 1));
			double result14 = (long) result13 + (long) ((long) result13 * (属性吸收率 - 1));
			double result15 = (long) result14 + (long) ((long) result14 * (暴击倍率 - 1));
			if (result15 < 1) {
				result15 = 1;
			}

			double result21 = ((long) result15 - 真实防御 * (1 - 卡牌穿防百分比 / 100) - 真实耐性);
			if (result21 < 1) {
				result21 = 1;
			}

			double result = result21;
			long resultLong = (long) result;
			return resultLong;
		} else {
			return 0;
		}
	}

	/**
	 * 
	 * @param request
	 * @param content
	 * @param userName
	 * @param class2 all, main
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/ma/comment/createComment")
	@ResponseBody
	@Deprecated
	public Map<String, Object> create(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String content,
			@RequestParam(required = false) String replyCommentId,
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) String class2,
			@RequestParam(required = false) String targetId) throws UnsupportedEncodingException {
		Map<String, Object> map = null;
		if (XStringUtils.isBlank(content)) {
			map = getFailCode(messageSourceUtils.getMessage("请输入评论内容"));
			return map;
		}

		if (content.length() > 2000) {
			map = getFailCode(messageSourceUtils.getMessage("对不起，评论最多只能输入2000个字"));
			return map;
		}

		if (userName != null && userName.length() > 50) {
			map = getFailCode(messageSourceUtils.getMessage("对不起，昵称最多只能输入50个字"));
			return map;
		}

		if (userName != null) {
			XCookieUtils.addCookieValue(response, ConstantsWeb.SITE_COOKIE_USER_NAME, userName);
		}

		CommentRecord commentRecord = new CommentRecord();
		commentRecord.setReplyCommentId(replyCommentId);
		commentRecord.setCommentDate(new Date());
		commentRecord.setCookieId(SiteUtils.getSiteCookieId(request));
		commentRecord.setClass1(CommentRecord.CLASS1_MA);
		commentRecord.setTargetId(targetId);
		if (CommentRecord.CLASS2_MA_MAIN.equals(class2)) {
			commentRecord.setClass2(CommentRecord.CLASS2_MA_MAIN);
		} else {
			commentRecord.setClass2(CommentRecord.CLASS2_MA_SINGLE);
		}
		commentRecord.setContent(content);
		commentRecord.setUserName(userName);
		commentRecord = commentRecordService.save(commentRecord);

		// 清除缓存
		entityCache.remove("commentPageMain");
		entityCache.remove("commentPageALl");
		entityCache.remove("commentPageSingle_" + targetId);

		if (commentRecord != null) {
			map = getSuccessCode(messageSourceUtils.getMessage("评论成功"));
			map.put("content", content);
		} else {
			map = getFailCode(messageSourceUtils.getMessage("评论失败"));
		}

		return map;
	}

	/**
	 * 追加比较伤害数据
	 */
	@RequestMapping(value = "/ma/addCompareData")
	@ResponseBody
	public Map<String, Object> create(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String id) throws UnsupportedEncodingException {
		Map<String, Object> map = null;
		return map;
	}

	private BigDecimal getBigDecimal(ServletRequest request, String key) {
		String value = request.getParameter(key);
		return XNumberUtils.getBigDecimal(value);
	}

	private boolean getBoolean(ServletRequest request, String key) {
		String value = request.getParameter(key);
		return XStringUtils.parseToBoolean(value);
	}

	private long getLong(Long value) {
		return value == null ? 0 : value;
	}

	private long getLong(Long value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return getLong(value);
	}
}
