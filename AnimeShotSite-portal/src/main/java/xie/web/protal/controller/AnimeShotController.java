package xie.web.protal.controller;

import java.io.*;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import xie.animeshotsite.bo.AutoRunParamBo;
import xie.animeshotsite.constants.ShotCoreConstants;
import xie.animeshotsite.constants.SysConstants;
import xie.animeshotsite.db.entity.*;
import xie.animeshotsite.db.entity.cache.EntityCache;
import xie.animeshotsite.db.repository.AnimeEpisodeDao;
import xie.animeshotsite.db.repository.AnimeInfoDao;
import xie.animeshotsite.db.repository.ShotInfoDao;
import xie.animeshotsite.db.repository.SubtitleLineDao;
import xie.animeshotsite.db.service.*;
import xie.animeshotsite.setup.ShotSiteSetup;
import xie.animeshotsite.setup.UserConfig;
import xie.animeshotsite.utils.FilePathUtils;
import xie.animeshotsite.utils.SiteUtils;
import xie.base.controller.BaseFunctionController;
import xie.base.page.PageRequestUtil;
import xie.base.user.UserUtils;
import xie.common.Constants;
import xie.common.constant.XConst;
import xie.common.string.XStringUtils;
import xie.common.utils.XCookieUtils;
import xie.common.utils.XRequestUtils;
import xie.common.utils.XSSHttpUtil;
import xie.common.web.util.ConstantsWeb;
import xie.module.language.XELangLocal;
import xie.module.language.translate.baidu.XELangBaidu;
import xie.other.ma.db.entity.CommentRecord;
import xie.other.ma.db.service.CommentRecordService;
import xie.web.util.SiteConstants;

@Controller
@RequestMapping(value = "/shot")
public class AnimeShotController extends BaseFunctionController<ShotInfo, String> {

    @Resource
    private AnimeInfoService animeInfoService;
    @Resource
    private AnimeInfoDao animeInfoDao;
    @Resource
    private AnimeEpisodeService animeEpisodeService;
    @Resource
    private AnimeEpisodeDao animeEpisodeDao;
    @Resource
    private ShotInfoService shotInfoService;
    @Resource
    private ShotInfoDao shotInfoDao;
    @Resource
    private SubtitleInfoService subtitleInfoService;
    @Resource
    private SubtitleLineService subtitleLineService;
    @Resource
    private SubtitleLineDao subtitleLineDao;
    @Resource
    private EntityCache entityCache;
    @Resource
    private ShotTaskService shotTaskService;
    @Resource
    private ShotSiteSetup shotSiteSetup;
    @Resource
    private CommentRecordService commentRecordService;
    @Resource
    private AutoRunParamBo autoRunParamBo;

    protected String getJspFileRootPath() {
        return "/shot/";
    }

    ;

    @RequestMapping(value = "/list/{animeEpisodeId}")
    public String shotList(
            @PathVariable String animeEpisodeId,
            @RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
            Model model, HttpServletRequest request)
            throws Exception {
        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        // 增加删除过滤
        searchParams.put("EQ_animeEpisodeId", animeEpisodeId);

        AnimeEpisode animeEpisode = animeEpisodeService.findOne(animeEpisodeId);
        if (animeEpisode == null) {
            // 剧集不存在，重定向到动画列表
            return "redirect:/anime";
        }

        AnimeInfo animeInfo = animeInfoService.findOne(animeEpisode.getAnimeInfoId());
        Page<ShotInfo> shotInfoPage = shotInfoService.searchPageByParams(searchParams, pageNumber, ConstantsWeb.SHOT_LIST_PAGE_NUMBER, sortType, ShotInfo.class);
        if (pageNumber > shotInfoPage.getTotalPages() && shotInfoPage.getTotalPages() > 0) {
            // 页数不对， 并且有数据，直接定位到最后一页
            String pageUrl = shotInfoPage.getTotalPages() > 1 ? "?page=" + shotInfoPage.getTotalPages() : "";
            return getUrlRedirectPath("list/" + animeEpisode.getId() + pageUrl);
        }

        model.addAttribute("animeInfo", animeInfo);
        model.addAttribute("animeEpisode", animeEpisode);
        model.addAttribute("shotInfoPage", shotInfoPage);

        if (pageNumber == 1) {
            // 字幕
            List<SubtitleLine> subtitleLineList = entityCache.get("subtitleLineList" + animeEpisodeId);
            if (subtitleLineList == null) {
                List<SubtitleInfo> subtitleInfoList = entityCache.get("subtitleInfoList" + animeEpisodeId);
                if (subtitleInfoList == null) {
                    subtitleInfoList = subtitleInfoService.findByAnimeEpisodeId(animeEpisodeId);
                    entityCache.put("subtitleInfoList" + animeEpisodeId, subtitleInfoList, XConst.SECOND_10_MIN * 1000);
                }
                if (subtitleInfoList.size() > 0) {
                    List<String> searchSubtitleInfoIdList = new ArrayList<String>();
                    for (SubtitleInfo subtitleInfo : subtitleInfoList) {
                        if (!Constants.FLAG_INT_YES.equals(subtitleInfo.getShowFlg())) {
                            continue;
                        }

                        if (Constants.FLAG_INT_YES.equals(subtitleInfo.getDeleteFlag())) {
                            continue;
                        }

                        searchSubtitleInfoIdList.add(subtitleInfo.getId());
                    }

                    subtitleLineList = subtitleLineDao.findBySubtitleInfoId(searchSubtitleInfoIdList);
                    entityCache.put("subtitleLineList" + animeEpisodeId, subtitleLineList, XConst.SECOND_10_MIN * 1000);
                }
            }
            model.addAttribute("subtitleLineList", subtitleLineList);
        }

        // 将搜索条件编码成字符串，用于排序，分页的URL
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return getJspFilePath("list");
    }

    @RequestMapping(value = "/list/{animeEpisodeId}/{page}")
    public String shotList2(
            @PathVariable String animeEpisodeId,
            @PathVariable(value = "page") int pageNumber,
            @RequestParam(value = "sortType", defaultValue = "timeStamp") String sortType,
            Model model, ServletRequest request)
            throws Exception {

        return getUrlRedirectPath("list/" + animeEpisodeId + "?page=" + pageNumber);
    }

    @RequestMapping(value = "/view/{id}")
    public String shotView(
            @PathVariable String id,
            @RequestParam(required = false) String scorllTop,
            @RequestParam(required = false) List<String> showLanage,
            Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ShotInfo shotInfo = shotInfoDao.findOne(id);
        if (shotInfo == null) {
            // TODO 404跳转
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("canBaiduIndex", false);// 不要索引
            return getUrlRedirectPath("404");
        }
        shotInfo = shotInfoService.convertToVO(shotInfo);
        AnimeInfo animeInfo = entityCache.findOne(animeInfoDao, shotInfo.getAnimeInfoId());
        AnimeEpisode animeEpisode = entityCache.findOne(animeEpisodeDao, shotInfo.getAnimeEpisodeId());

        model.addAttribute("shotInfo", shotInfo);
        model.addAttribute("animeInfo", animeInfo);
        model.addAttribute("animeEpisode", animeEpisode);

        // 算出当前数据在列表中的页数
        Integer rowNumber = entityCache.get("shotRowNumber_" + shotInfo.getId());
        if (rowNumber == null) {
            rowNumber = shotInfoDao.getRowNumber(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp(), Constants.FLAG_INT_NO);
            entityCache.put("shotRowNumber_" + shotInfo.getId(), rowNumber);
        }
        int pageSize = ConstantsWeb.SHOT_LIST_PAGE_NUMBER;
        model.addAttribute("rowNumber", rowNumber);
        int pageNumber = (rowNumber - 1) / pageSize + 1;
        model.addAttribute("pageNumber", (rowNumber - 1) / pageSize + 1);
        if (pageNumber > 1) {
            model.addAttribute("pageNumberUrl", "?page=" + pageNumber);
        }

        // 搜索前后页
        ShotInfo previousShotInfo = entityCache.findPreviousShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
        ShotInfo nextShotInfo = entityCache.findNextShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
        model.addAttribute("previousShotInfo", shotInfoService.convertToVO(previousShotInfo));
        model.addAttribute("nextShotInfo", shotInfoService.convertToVO(nextShotInfo));

        // 搜索字幕
        String localeLanguage = Constants.LANGUAGE_UNKNOW;
        if (!XRequestUtils.isSearchspider(request)) {
            localeLanguage = XRequestUtils.getLocaleLanguageCountry(request);
        }
        boolean showAllSubtitleFlag = Constants.FLAG_STR_YES.equals(XCookieUtils.getCookieValue(request, SiteConstants.COOKIE_SHOW_ALL_SUBTITLE_FLAG));

        UserConfig userConfig = SiteUtils.getUserConfig(request);
        List<String> toTranLanguage = new ArrayList<>();
        if (!userConfig.isNotTranFlag()) {
            if (userConfig.getTranLanguage() != null) {
                // 现在 TODO 每次session清空都会导致翻译设置失效
                toTranLanguage.add(userConfig.getTranLanguage().getValue());
            } else {
                // 翻译设置不存在，则直接将显示语言作为翻译语言
                toTranLanguage.add(localeLanguage);
            }
        }

        List<String> actualShowLanage = subtitleInfoService.findActualShowLanguage(animeEpisode.getId(), localeLanguage, showLanage, showAllSubtitleFlag);
        Long startTime = shotInfo.getTimeStamp();
        Long endTime = nextShotInfo == null ? startTime + 5000 : nextShotInfo.getTimeStamp();
        actualShowLanage.addAll(toTranLanguage); // TODO 有错误
        List<SubtitleLine> subtitleLineList = subtitleLineService.findByTimeRemoveDuplicate(animeEpisode.getId(), actualShowLanage, startTime, endTime);
        // 如果显示语言在搜索到的字幕中不存在，则删除掉
        deleteNoSubtitleLanguage(actualShowLanage, subtitleLineList);
        subtitleLineList = subtitleLineService.convertChinese(subtitleLineList, actualShowLanage, localeLanguage);
        model.addAttribute("subtitleLineList", subtitleLineList);

        // 生成每句話的百度翻譯API接口調用的MD5签名
        try {
            if (toTranLanguage.size() > 0) {
                setTranslateSign(toTranLanguage, actualShowLanage, model, subtitleLineList);
            }
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
        }

        // 生成前台title，描述等地方使用的字符串
        StringBuilder subtitleLineTextStrSb = new StringBuilder();
        for (SubtitleLine subtitleLine : subtitleLineList) {
            subtitleLineTextStrSb.append(subtitleLine.getText());
        }
        String subtitleLineTextStr = subtitleLineTextStrSb.toString();
        model.addAttribute("subtitleLineTextStr", subtitleLineTextStr);
        model.addAttribute("subtitleLineTextStr50", StringUtils.substring(subtitleLineTextStr.toString(), 0, 50));
        model.addAttribute("subtitleLineTextStr100", StringUtils.substring(subtitleLineTextStr.toString(), 0, 100));
        model.addAttribute("subtitleLineTextStr200", StringUtils.substring(subtitleLineTextStr.toString(), 0, 200));
        model.addAttribute("subtitleLineTextStr500", StringUtils.substring(subtitleLineTextStr.toString(), 0, 500));

        // 前台页面cookie等参数设置
        model.addAttribute("scorllTop", scorllTop); // 用户提交时滚屏高度

        String ShotViewImgWidth = XCookieUtils.getCookieValue(request, "ShotViewImgWidth"); // 用户设定的图片展示宽度
        if (ShotViewImgWidth != null && !ShotViewImgWidth.matches("[0-9]+")) {
            ShotViewImgWidth = null;
        }
        model.addAttribute("ShotViewImgWidth", ShotViewImgWidth);
        String ShotImgDivWidth = null;// 需要设置的图片div宽度
        if (ShotViewImgWidth != null) {
            ShotImgDivWidth = ShotViewImgWidth;
        } else {
            ShotImgDivWidth = XCookieUtils.getCookieValue(request, "ShotImgDivWidth"); // 用户提交前获得的图片div宽度
            if (ShotImgDivWidth != null && !ShotImgDivWidth.matches("[0-9]+")) {
                ShotImgDivWidth = null;
            }
        }
        model.addAttribute("ShotImgDivWidth", ShotImgDivWidth);

        model.addAttribute("subtitleTranslatedTextColor", userConfig.getTranLanguageColor());
        model.addAttribute("subtitleTranslatedTextFontsize", userConfig.getTranLanFonsize() == null ? "smaller" : userConfig.getTranLanFonsize());

        // 显示评论
        // 主页面评论
        final String shotId = shotInfo.getId();
        Page<CommentRecord> shotCommentPage = entityCache.get("shot_comment_" + shotId, () -> {
            Map<String, Object> searchParamsMain = new HashMap<>();
            searchParamsMain.put("EQ_" + CommentRecord.COLUMN_TARGET_ID, shotId);
            searchParamsMain.put("EQ_" + CommentRecord.COLUMN_CLASS1, "shot");
            Page<CommentRecord> page = commentRecordService.searchPageByParams(searchParamsMain, 1, 50, PageRequestUtil.SORT_TYPE_AUTO, CommentRecord.class);
            return page;
        });
        model.addAttribute("shotCommentPage", shotCommentPage);

        return getJspFilePath("view");
    }

    @RequestMapping(value = "/img/{shotIdTemp}")
    public void getImage(@PathVariable String shotIdTemp, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        InputStream fis = null;
        servletResponse.setContentType("image/jpg");
        try {
            OutputStream out = servletResponse.getOutputStream();
            File file = null;

            shotIdTemp = shotIdTemp.trim();
            if (shotIdTemp.endsWith("t")) {
                shotIdTemp = shotIdTemp.substring(0, shotIdTemp.length() - 1);
            }
            if (shotIdTemp.endsWith("s")) {
                shotIdTemp = shotIdTemp.substring(0, shotIdTemp.length() - 1);
            }

            String shotId = shotIdTemp;
            ShotInfo shotInfo = shotInfoService.findOne(shotId);
            if (shotInfo != null) {
                AnimeEpisode animeEpisode = animeEpisodeService.findOne(shotInfo.getAnimeEpisodeId());
                AnimeInfo animeInfo = animeInfoService.findOne(shotInfo.getAnimeInfoId());
                if (shotInfo != null) {
                    file = FilePathUtils.getShotFullFilePath(shotInfo, animeEpisode, animeInfo);
                }
            }
            if (file != null) {
                fis = new FileInputStream(file);
            } else {
                fis = FilePathUtils.getNoImageFileStream();
            }

            byte[] b = new byte[fis.available()];

            if (shotIdTemp.endsWith("t")) {
                // TODO 转换成缩略图
            }
            if (shotIdTemp.endsWith("s")) {
                // TODO 转换成缩略图
            }

            fis.read(b);
            out.write(b);
            out.flush();
        } catch (

                Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除没有在字幕列表中出现的显示语言
     */
    private void deleteNoSubtitleLanguage(List<String> actualShowLanage, List<SubtitleLine> subtitleLineList) {
        List<String> toDelActList = new ArrayList<>();
        for (String actLan : actualShowLanage) {
            if (actLan == null) {
                toDelActList.add(actLan);
                continue;
            }

            boolean hasDataFlag = false;
            for (SubtitleLine subLine : subtitleLineList) {
                if (actLan.equals(subLine.getLanguage())) {
                    hasDataFlag = true;
                    break;
                }
            }
            if (!hasDataFlag) {
                toDelActList.add(actLan);
            }
        }
        actualShowLanage.removeAll(toDelActList);
    }

    /**
     * 设置api接口调用时的签名
     *
     * @param toTranLanguage   用户设定的语言
     * @param actualShowLanage 数据库中实际可以显示的语言
     * @param model
     * @param subtitleLineList
     */
    private void setTranslateSign(List<String> toTranLanguage, List<String> actualShowLanage, Model model, List<SubtitleLine> subtitleLineList) {
        // 用户设定了语言才执行翻译，否则不翻译
        if (toTranLanguage == null || toTranLanguage.size() == 0) {
            return;
        }

        if (actualShowLanage == null || actualShowLanage.size() == 0) {
            return;
        }

        if (subtitleLineList == null || subtitleLineList.size() == 0) {
            return;
        }

        // 将语言名称转换成统一类型
        List<String> actualShowLanageTemp = actualShowLanage;
        actualShowLanage = new ArrayList<>();
        for (int i = 0; i < actualShowLanageTemp.size(); i++) {
            String subLangType = actualShowLanageTemp.get(i);
            if (subLangType == null) {
                continue;
            }

            // 检查该语言是否在当前显示的字幕中存在，不存在的话，则要从翻译源怨言中删除掉
            for (SubtitleLine sub : subtitleLineList) {
                if (subLangType.equals(sub.getLanguage())) {
                    // 存在這個語言的字幕
                    actualShowLanage.add(SubtitleInfo.LANGUAGE_MAPPING_this2Constants.get(subLangType));
                    break;
                }
            }
        }

        if (actualShowLanage == null || actualShowLanage.size() == 0) {
            return;
        }

        // 决定翻译成哪些语言
        List<String> toTranslateLangList = new ArrayList<>();
        for (String lan : toTranLanguage) {
            // 用户设定的语言在实际可以显示语言中不存在，则进行翻译
            if (!XStringUtils.existIgnoreCase(actualShowLanage, lan)) {
                toTranslateLangList.add(lan);
            }
        }

        if (toTranslateLangList.size() == 0) {
            return;
        }

        Map<String, Object> translateParam = new HashMap<>();
        String appId = shotSiteSetup.getBaiduTranslateAppid();
        String key = shotSiteSetup.getBaiduTranslateKey();
        String salt = new Random().nextInt(999999) + "";
        // String fromLang = "auto";
        // 根据优先级获得翻译源语言
        actualShowLanage.sort(new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                XELangLocal lan1 = XELangLocal.parseValue(o1, XELangLocal.class);
                XELangLocal lan2 = XELangLocal.parseValue(o2, XELangLocal.class);
                if (lan1 == null || lan2 == null) {
                    return 0;
                }

                return lan1.getOrder() - lan2.getOrder();
            }
        });
        String fromLang = actualShowLanage.get(0);
        String doSubtitleLang = SubtitleInfo.LANGUAGE_MAPPING.get(fromLang); // 语言标识为数据库类型，应该和fromLang保持一致

        // String toLang = "en";
        List<String> toLangList = toTranslateLangList;
        List<String> toDeleteList = new ArrayList<>();
        for (int i = 0; i < toLangList.size(); i++) {
            // 翻译成百度的语言标识
            XELangLocal xeLangLocal = XELangLocal.parseValue(toLangList.get(i), XELangLocal.class);
            if (xeLangLocal == null) {
                toDeleteList.add(toLangList.get(i));
                continue;
            }
            XELangBaidu xeLangBaidu = XELangBaidu.getLanguage(xeLangLocal);
            if (xeLangBaidu != null) {
                toLangList.set(i, xeLangBaidu.getValue());
            } else {
                toDeleteList.add(toLangList.get(i));
            }
        }
        toLangList.removeAll(toDeleteList);

        // 如果字幕中存在简体或者繁体，则不需要翻译成简体或繁体
        if (XStringUtils.existIgnoreCase(actualShowLanage, XELangLocal.ZH_CN.getValue()) ||
                XStringUtils.existIgnoreCase(actualShowLanage, XELangLocal.ZH_TW.getValue())) {
            toLangList.remove(XELangBaidu.zh.getValue());
            toLangList.remove(XELangBaidu.cht.getValue());
        }

        if (toLangList.size() == 0) {
            return;
        }

        translateParam.put("appId", appId);
        translateParam.put("salt", salt);
        translateParam.put("doSubtitleLang", doSubtitleLang); // 语言标识为数据库类型
        translateParam.put("fromLang", XELangBaidu.getLanguage(XELangLocal.parseValue(fromLang, XELangLocal.class)).getValue());
        // translateParam.put("toLang", XELangBaidu.getLanguage(XELangLocal.parseValue(toLang, XELangLocal.class)).getValue());
        translateParam.put("toLangList", toLangList);
        model.addAttribute("translateParam", translateParam);

        List<String> subtitleLineSignList = subtitleLineService.createBaiduTranslateSign(subtitleLineList, appId, key, salt);
        model.addAttribute("subtitleLineSignList", subtitleLineSignList);
    }

    @RequiresPermissions(value = "userList:add")
    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public Map<String, Object> delete(
            @PathVariable String id,
            Model model, HttpServletRequest request) throws Exception {

        // 检查
        ShotInfo shotInfo = shotInfoDao.findOne(id);
        if (shotInfo == null) {
            return getFailCode("数据不存在");
        }

        Map<String, Object> map = getSuccessCode("删除成功");

        // 搜索前后页
        ShotInfo showShotInfo = entityCache.findNextShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
        if (showShotInfo == null) {
            showShotInfo = entityCache.findPreviousShotInfo(shotInfo.getAnimeEpisodeId(), shotInfo.getTimeStamp());
        }
        if (showShotInfo != null) {
            map.put("showShotInfoId", showShotInfo.getId());
        }

        // 删除
        shotInfoService.delete(shotInfo);

        // 清除缓存
        clearPreNextShotCache();

        return map;
    }

    @RequestMapping(value = "/publicLike")
    @ResponseBody
    public Map<String, Object> publicLike(@RequestParam String id) {
        Map<String, Object> map = null;
        ShotInfo shotInfo = shotInfoService.publicLikeAdd(id);
        if (shotInfo != null) {
            map = getSuccessCode();
            map.put("newCount", shotInfo.getPublicLikeCount());
        } else {
            map = getFailCode("截图不存在");
        }

        return map;
    }

    @RequestMapping(value = "/random_old")
    public String random_old(Model model) throws Exception {
        List<ShotInfo> shotInfoList = new ArrayList<ShotInfo>();
        for (int i = 0; i < 9; i++) {
            List<ShotInfo> list = shotInfoService.findRandomShot(1, null, null);
            shotInfoList.addAll(list);
        }
        model.addAttribute("shotInfoList", shotInfoList);

        return getJspFilePath("random");
    }

    @RequestMapping(value = "/random")
    public String random(Model model) throws Exception {
        List<AnimeEpisode> episodeList = new ArrayList<AnimeEpisode>();
        for (int i = 0; i < 12; i++) {
            List<AnimeEpisode> list = animeEpisodeService.findRandom(1);
            if (list.size() > 0) {
                episodeList.addAll(list);
            }
        }

        List<ShotInfo> shotInfoList = new ArrayList<ShotInfo>();
        episodeList.forEach(animeEpisode -> {
            List<ShotInfo> list = shotInfoService.findRandomShot(1, null, animeEpisode.getId());
            if (list.size() > 0) {
                shotInfoList.addAll(list);
            }
        });
        model.addAttribute("shotInfoList", shotInfoList);

        return getJspFilePath("random");
    }

    @RequestMapping(value = "/recommend")
    public String recommend(
            Model model,
            @RequestParam(value = "time", required = false) Integer time,
            @RequestParam(value = "page", defaultValue = "1") int pageNumber) throws Exception {

        String cacheKey = "masterRecommandShotPage" + "_" + time + "_" + pageNumber;

        // 获得站长推荐
        Page<ShotInfo> masterRecommandShotPage = entityCache.get(cacheKey);
        if (masterRecommandShotPage == null) {
            masterRecommandShotPage = shotInfoService.getMasterRecommandShotPage(pageNumber, time, 48, false);
            entityCache.put(cacheKey, masterRecommandShotPage, XConst.SECOND_10_MIN * 1000);
        }

        model.addAttribute("shotInfoPage", masterRecommandShotPage);

        return getJspFilePath("recommend");
    }

    /**
     * 增加一张截图
     *
     * @param refShotInfoId 参照的截图ID
     * @param preFlg        向前还是向后
     * @param offsetTime    偏移多少毫秒
     * @return
     */
    @RequestMapping(value = "/doCreateShot")
    @ResponseBody
    public Map<String, Object> doCreateShot(
            @RequestParam(required = false) String refShotInfoId,
            @RequestParam(required = false) Long offsetTime,
            @RequestParam(required = false, defaultValue = "false") boolean preFlg,
            Model model, HttpServletRequest request) {

        if (refShotInfoId == null) {
            return getFailCode(messageSourceUtils.getMessage("未指定图片"));
        }
        if (offsetTime == null) {
            return getFailCode(messageSourceUtils.getMessage("未指定偏移时间"));
        }

        if (!UserUtils.hasRole(SysConstants.ROLE_ADMIN)) {
            // 非管理員不能指定1000和2000之外
            if (offsetTime != 1000 && offsetTime != 2000) {
                return getFailCode(messageSourceUtils.getMessage("指定时间不正确，只能指定1000或2000"));
            }
        }

        try {
            if (autoRunParamBo.isWaitUploadFullUnlock()) {
                return getFailCode(messageSourceUtils.getMessage("对不起，截图队列已满，请在下个整点（一小时后）再来"));
            }
        } catch (Exception e) {
            _log.error("", e);
        }

        ShotInfo shotInfo = shotInfoDao.findById(refShotInfoId);
        if (shotInfo == null || Constants.FLAG_INT_YES.equals(shotInfo.getDeleteFlag())) {
            return getFailCode(messageSourceUtils.getMessage("指定图片不存在，请重新操作"));
        }
        String animeEpidodeId = shotInfo.getAnimeEpisodeId();
        AnimeEpisode animeEpisode = animeEpisodeDao.findById(animeEpidodeId);
        if (animeEpisode == null) {
            return getFailCode(messageSourceUtils.getMessage("指定图片的剧集不存在，请重新操作"));
        }

        long toGetTimestamp = shotInfo.getTimeStamp();
        if (preFlg) {
            toGetTimestamp = toGetTimestamp - offsetTime;
        } else {
            toGetTimestamp = toGetTimestamp + offsetTime;
        }

        // 判断图片是否已经存在
        ShotInfo toGetShotInfo = shotInfoDao.findByAnimeEpisodeIdAndTimeStamp(animeEpidodeId, toGetTimestamp);
        if (toGetShotInfo != null) {
            return getFailCode(messageSourceUtils.getMessage("指定截图已存在，请刷新页面"));
        }

        // TODO 判断相同任务是否已经存在，等待中或执行中
        // ShotTask sameTask = shotTaskService.findByParam("");
        // if (sameTask != null) {
        // return getFailCode("指定任务已存在，请稍后刷新页面后查看");
        // }

        ShotTask shotTask = shotTaskService.addUserSelfRunSpecifyEpisideTimeTask(animeEpidodeId, new Date(), false, String.valueOf(toGetTimestamp), XSSHttpUtil.getForwardedRemoteIpAddr(request));

        Map<String, Object> map = null;
        map = getSuccessCode(messageSourceUtils.getMessage("正在截图中，请耐心等候，此过程大概需要1分钟"));
        map.put("taskId", shotTask.getId());
        map.put("animeEpisodeId", animeEpidodeId);
        map.put("timestamp", toGetTimestamp);

        return map;
    }

    /**
     * 检测是否截图成功
     *
     * @param taskId         参照的截图ID
     * @param animeEpisodeId 向前还是向后
     * @param timestamp      偏移多少毫秒
     * @return
     */
    @RequestMapping(value = "/checkCreateShot")
    @ResponseBody
    public Map<String, Object> checkCreateShot(
            @RequestParam(required = false) String taskId,
            @RequestParam(required = false) String animeEpisodeId,
            @RequestParam(required = false) Long timestamp,
            Model model, HttpServletRequest request) {

        if (taskId == null) {
            return getFailCode(messageSourceUtils.getMessage("未指定任务ID"));
        }
        if (animeEpisodeId == null) {
            return getFailCode(messageSourceUtils.getMessage("未指定剧集ID"));
        }
        if (timestamp == null) {
            return getFailCode(messageSourceUtils.getMessage("未指定时间"));
        }

        Map<String, Object> map = null;

        // 查看任务状态
        ShotTask shotTask = shotTaskService.findById(taskId);
        if (shotTask == null) {
            return getFailCode(messageSourceUtils.getMessage("指定任务不存在"));
        }

        long pastTime = (new Date().getTime() - shotTask.getCreateDate().getTime()) / 1000;
        map = getSuccessCode();
        map.put("pastTime", pastTime);
        map.put("taskResutStatus", shotTask.getTaskResult());
        if (ShotTask.TASK_RESULT_WAIT.equals(shotTask.getTaskResult())) {
            if (pastTime < 30) {
                map.put("taskMessage", messageSourceUtils.getMessage("任务等待中，请稍后，请不要关闭画面"));
            } else if (pastTime < 120) {
                map.put("taskResutStatus", 11);
                map.put("taskMessage", messageSourceUtils.getMessage("任务等待超时，或许是服务器正忙"));
            } else {
                map.put("taskResutStatus", 12);
                map.put("taskMessage", messageSourceUtils.getMessage("任务等待超时，请改天再来查看您需要的截图"));
            }
        }
        if (ShotTask.TASK_RESULT_PROCESSING.equals(shotTask.getTaskResult())) {
            map.put("taskMessage", messageSourceUtils.getMessage("正在获取截图，请稍后，请不要关闭画面"));
        }
        if (ShotTask.TASK_RESULT_FAIL.equals(shotTask.getTaskResult())) {
            map.put("taskMessage", messageSourceUtils.getMessage("获取截图失败，请联系管理员"));
        }
        if (ShotTask.TASK_RESULT_SUCCESS.equals(shotTask.getTaskResult())) {
            map.put("taskMessage", messageSourceUtils.getMessage("获取截图已成功，等待返回截图，请不要关闭画面"));
        }

        // 判断图片是否已经存在
        ShotInfo shotInfo = shotInfoDao.findByAnimeEpisodeIdAndTimeStamp(animeEpisodeId, timestamp);
        if (shotInfo != null) {
            clearPreNextShotCache();
            map.put("taskResutStatus", ShotTask.TASK_RESULT_SUCCESS);
            map.put("taskMessage", messageSourceUtils.getMessage("已成功获取到截图"));
            map.put(Constants.JSON_RESPONSE_KEY_MESSAGE, messageSourceUtils.getMessage("已成功获取到截图"));
            map.put("shotInfoId", shotInfo.getId());
        }

        return map;
    }

    public void clearPreNextShotCache() {
        entityCache.clearBegin(EntityCache.CACHE_ID_Previous_ShotInfo);
        entityCache.clearBegin(EntityCache.CACHE_ID_Next_ShotInfo);
    }
}
