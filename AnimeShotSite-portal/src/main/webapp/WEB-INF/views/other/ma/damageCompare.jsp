<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<c:if test="${not isSinglePage}">
	<title><c:out value='乖离性百万亚瑟王 伤害计算器 乖离性MA 计算公式' /></title>
	<meta name="keywords" content="乖离性百万亚瑟王 伤害计算器 乖离性MA 计算公式" />
	<meta name="description" content="乖离性百万亚瑟王 伤害计算器 乖离性MA 计算公式" />
</c:if>
<c:if test="${isSinglePage}">
	<title><c:out value='${commonRecord.name}' /> - <c:out value='${commonRecord.userName}' /> - 乖离性MA 伤害计算器</title>
	<meta name="keywords" content="<c:out value='${commonRecord.name}' /> - <c:out value='${commonRecord.userName}' /> - 乖离性MA 伤害计算器" />
	<meta name="description" content="<c:out value='${commonRecord.name}' /> - <c:out value='${commonRecord.userName}' /> - 乖离性MA 伤害计算器" />
</c:if>

<style>
body {
	overflow: scroll;
}

.advancedSettingDiv {
	display: none;
}
</style>
</head>

<script type="text/javascript">
	$(function() {
		// 如果数据有高级设置，则展示高级设置的数据
		if ($("#advancedSettingFlag").val() == "1") {
			$("#advancedSettingDiv").show();
			$(".advancedSettingDiv").show();
		}
	});

	function createCommentRecord() {
		$.homeAjaxSubmit("createCommentForm", null, function(data) {
			if (data.success) {
				$.showMessageModal(data.content);
			} else {
				$.showMessageModal(data.message);
			}
		});
	}

	function showAdvancedSetting() {
		if ($("#advancedSettingFlag").val() == "1") {
			$("#advancedSettingFlag").val("0");
			$("#advancedSettingDiv").hide();
			$(".advancedSettingDiv").hide();
		} else {
			$("#advancedSettingFlag").val("1");
			$("#advancedSettingDiv").show();
			$(".advancedSettingDiv").show();
		}
	}
</script>

<div class="row">
	<form action="${ctx}/maCalc" method="post">
		<input type="hidden" name="calcFlag" value="1">
		<input type="hidden" name="saveFlag" value="1">
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px; margin-bottom: 10px;">
			<h1 style="font-size: 14px; margin: 0px;"><c:if test="${not isSinglePage}">
					<c:out value='乖离性Ma伤害计算器' />
				</c:if> <c:if test="${isSinglePage}">
					<c:out value='${commonRecord.name}' /> - <c:out value='${commonRecord.userName}' />
				</c:if></h1>
		</div>
		<!-- 
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">p1:真实面板</div>
			<div class="col-sm-6">
				<input name="p1" value="${p1}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">p2:叠chain</div>
			<div class="col-sm-6">
				<input name="p2" value="${p2}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">p3:暴击倍率</div>
			<div class="col-sm-6">
				<input name="p3" value="${p3}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">p4:属性吸收率</div>
			<div class="col-sm-6">
				<input name="p4" value="${p4}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">p7:ex补正</div>
			<div class="col-sm-6">
				<input name="p71" value="${p71}" class="form-control input-sm" placeholder="请输入数值">
				<input name="p72" value="${p72}" class="form-control input-sm" placeholder="请输入数值">
				<input name="p73" value="${p73}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">p5:真实防御</div>
			<div class="col-sm-6">
				<input name="p5" value="${p5}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">p6:真实耐性</div>
			<div class="col-sm-6">
				<input name="p6" value="${p6}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
-->




		<!-- *********************************************************************** -->
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">最终面板数值</div>
			<div class="col-sm-6">
				<input name="panelValue" value="${maDamage.panelValue}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">chain数量</div>
			<div class="col-sm-6">
				<input name="chainNumber" value="${maDamage.chainNumber}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">卡牌穿防百分比</div>
			<div class="col-sm-6">
				<input name="cardPenetratePercent" value="${maDamage.cardPenetratePercent}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">卡牌是否有chainUp</div>
			<div align="left" class="col-sm-6">
				<select name="cardChainUpFlag">
					<option value="0" ${maDamage.cardChainUpFlag == 0 ? "selected=\"selected\"" : ""}>无 chainUp</option>
					<option value="1" ${maDamage.cardChainUpFlag == 1 ? "selected=\"selected\"" : ""}>有 chainUp</option>
				</select>
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">锁定加伤百分比</div>
			<div class="col-sm-6">
				<input name="targetingPercent" value="${maDamage.targetingPercent}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">敌方防御数值</div>
			<div class="col-sm-6">
				<input name="defaultDef" value="${maDamage.defaultDef}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 advancedSettingDiv" style="margin-bottom: 3px;">
			<div class="col-sm-3">敌方物理防御数值</div>
			<div class="col-sm-6">
				<input name="physicalDef" value="${maDamage.physicalDef}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 advancedSettingDiv" style="margin-bottom: 3px;">
			<div class="col-sm-3">敌方魔法防御数值</div>
			<div class="col-sm-6">
				<input name="magicDef" value="${maDamage.magicDef}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">敌方耐性防御数值</div>
			<div class="col-sm-6">
				<input name="specialDef" value="${maDamage.specialDef}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">ex补正百分比</div>
			<div class="col-sm-6">
				<input name="exDefaultPercent" value="${maDamage.exDefaultPercent}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 advancedSettingDiv" style="margin-bottom: 3px;">
			<div class="col-sm-3">ex物理补正百分比</div>
			<div class="col-sm-6">
				<input name="exPhysicalPercent" value="${maDamage.exPhysicalPercent}" class="form-control input-sm" placeholder="请输入数值，所有数值相加即可">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 advancedSettingDiv" style="margin-bottom: 3px;">
			<div class="col-sm-3">ex魔法补正百分比</div>
			<div class="col-sm-6">
				<input name="exMagicPercent" value="${maDamage.exMagicPercent}" class="form-control input-sm" placeholder="请输入数值">
			</div>
		</div>
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">克制属性增益百分比</div>
			<div class="col-sm-6">
				<input name="cardElementAttributePercent" value="${maDamage.cardElementAttributePercent}" class="form-control input-sm" placeholder="请输入数值,默认200,克制属性增益百分比  克制为200，非克制为100，被克制为50">
			</div>
		</div>
		<div style="margin-top: 10px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="col-sm-3"></div>
			<div align="left" class="col-sm-3" style="font-size: 16px;">
				<b>计算结果(非暴)：</b>${result1}
			</div>
		</div>
		<div style="margin-bottom: 10px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="col-sm-3"></div>
			<div align="left" class="col-sm-3" style="font-size: 16px;">
				<b>计算结果(暴击)：</b>${result2}
			</div>
		</div>
		<div style="margin-top: 5px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="col-sm-3"></div>
			<div align="left" class="col-sm-9">
				您的昵称
				<input name="userName" value="<c:out value='${cookieUserName}' />" class="input-sm" placeholder="请输入您的昵称">
			</div>
		</div>
		<div style="margin-top: 5px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="col-sm-3"></div>
			<div align="left" class="col-sm-9">
				数据名称
				<input name="name" value="<c:out value='${name}' />" class="input-sm" placeholder="请给你的计算数据起个名字">
			</div>
		</div>
		<div style="margin-top: 10px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<div class="col-sm-3"></div>
			<div align="left" class="col-sm-2">
				<c:if test="${not isSinglePage}">
					<input type="submit" class="btn btn-sm btn-primary" value="计算并保存" />
				</c:if>
				<c:if test="${isSinglePage}">
					<input type="submit" class="btn btn-sm btn-primary" value="重新计算并保存" />
				</c:if>
			</div>
			<div align="left" class="col-sm-5">
				<div style="padding-top: 1px;" class="bdsharebuttonbox">
					<a style="background-position: 0 -32px; padding-left: 0px; padding-right: 10px;" title="分享计算结果">分享计算结果</a>
					<a href="#" class="bds_mshare" data-cmd="mshare" title="百度一键分享"></a>
					<a href="#" class="bds_tieba" data-cmd="tieba" title="分享到百度贴吧"></a>
					<a href="#" class="bds_sqq" data-cmd="sqq" title="分享到QQ好友"></a>
					<a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
					<a href="#" class="bds_bdxc" data-cmd="bdxc" title="分享到百度相册"></a>
					<a href="#" class="bds_duitang" data-cmd="duitang" title="分享到堆糖"></a>
					<a href="#" class="bds_copy" data-cmd="copy" title="复制网址"></a>
					<a href="#" class="bds_evernotecn" data-cmd="evernotecn" title="分享到印象笔记"></a>
					<a href="#" class="bds_more" data-cmd="more" title="更多"></a>
				</div>
			</div>
		</div>
		<c:if test="${isSinglePage}">
			<div style="margin-top: 5px;" align="left" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="col-sm-3"></div>
				<div align="left" class="col-sm-6">
					<a href="${ctx}/maCalc" class="btn btn-sm btn-primary">返回</a>
				</div>
			</div>
		</c:if>
		<div style="height: 20px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12"></div>

		<!-- ****************高级设置******************************************************* -->
		<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
			<div class="col-sm-3">
				是否使用高级设置<span style="color: red;">(还未实装)</span>
			</div>
			<div align="left" class="col-sm-6">
				<input type="button" value="显示高级设置" class="btn btn-sm btn-primary" onclick="showAdvancedSetting();">
				<input type="hidden" id="advancedSettingFlag" name="advancedSettingFlag" value="${maDamage.advancedSettingFlag}">
			</div>
		</div>
		<div id="advancedSettingDiv" style="display: none;">
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌Cost</div>
				<div class="col-sm-6">
					<input name="cardCost" value="${maDamage.cardCost}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-2">敌方具体耐性</div>
				<div class="col-sm-2">
					<input name="specialDefHuo" value="${maDamage.specialDefHuo}" style="color: white; background-color: red;" class="form-control input-sm" placeholder="请输入数值">
				</div>
				<div class="col-sm-2">
					<input name="specialDefFeng" value="${maDamage.specialDefFeng}" style="color: white; background-color: green;" class="form-control input-sm" placeholder="请输入数值">
				</div>
				<div class="col-sm-2">
					<input name="specialDefShui" value="${maDamage.specialDefShui}" style="color: white; background-color: blue;" class="form-control input-sm" placeholder="请输入数值">
				</div>
				<div class="col-sm-2">
					<input name="specialDefGuang" value="${maDamage.specialDefGuang}" style="color: black; background-color: yellow;" class="form-control input-sm" placeholder="请输入数值">
				</div>
				<div class="col-sm-2">
					<input name="specialDefAn" value="${maDamage.specialDefAn}" style="color: white; background-color: purple;" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌吃魔法数值百分比</div>
				<div class="col-sm-6">
					<input name="cardMagicPercent" value="${maDamage.cardMagicPercent}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌吃物理数值百分比</div>
				<div class="col-sm-6">
					<input name="cardPhysicalPercent" value="${maDamage.cardPhysicalPercent}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌吃血量数值百分比</div>
				<div class="col-sm-6">
					<input name="cardLifePercent" value="${maDamage.cardLifePercent}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌特定条件伤害提升百分比</div>
				<div class="col-sm-6">
					<input name="cardValuePercent" value="${maDamage.cardValuePercent}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌攻击次数</div>
				<div class="col-sm-6">
					<input name="cardAttackTimes" value="${maDamage.cardAttackTimes}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">人物基础血量数值</div>
				<div class="col-sm-6">
					<input name="peopleLifeValue" value="${maDamage.peopleLifeValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">人物基础物理数值</div>
				<div class="col-sm-6">
					<input name="peoplePhysicalValue" value="${maDamage.peoplePhysicalValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">人物基础魔法数值</div>
				<div class="col-sm-6">
					<input name="peopleMagicValue" value="${maDamage.peopleMagicValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">人物基础回复数值</div>
				<div class="col-sm-6">
					<input name="peopleRecoveryValue" value="${maDamage.peopleRecoveryValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">增益血量数值</div>
				<div class="col-sm-6">
					<input name="buffLifeValue" value="${maDamage.buffLifeValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">增益物理数值</div>
				<div class="col-sm-6">
					<input name="buffPhysicalValue" value="${maDamage.buffPhysicalValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">增益魔法数值</div>
				<div class="col-sm-6">
					<input name="buffMagicValue" value="${maDamage.buffMagicValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">增益回复数值</div>
				<div class="col-sm-6">
					<input name="buffRecoveryValue" value="${maDamage.buffRecoveryValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">最大增益数值</div>
				<div class="col-sm-6">
					<input name="maxBuffValue" value="${maDamage.maxBuffValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌类型 1:物理 2:魔法</div>
				<div class="col-sm-6">
					<input name="cardType" value="${maDamage.cardType}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌颜色 1:火 2:水 3:风 4:光 5:暗</div>
				<div class="col-sm-6">
					<input name="cardColor" value="${maDamage.cardColor}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>
			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-bottom: 3px;">
				<div class="col-sm-3">卡牌基础伤害数值</div>
				<div class="col-sm-6">
					<input name="cardBaseValue" value="${maDamage.cardBaseValue}" class="form-control input-sm" placeholder="请输入数值">
				</div>
			</div>




			<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="col-sm-9">
					<input type="submit" class="btn btn-sm btn-primary" value="计算" />
				</div>
			</div>

			<div style="margin-top: 10px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="col-sm-3"></div>
				<div align="left" class="col-sm-3" style="font-size: 16px;">
					<b>计算结果(非暴)：</b>${result1}
				</div>
			</div>
			<div style="margin-bottom: 10px;" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<div class="col-sm-3"></div>
				<div align="left" class="col-sm-3" style="font-size: 16px;">
					<b>计算结果(暴击)：</b>${result2}
				</div>
			</div>
		</div>
	</form>

	<style type='text/css'>
tr.locktop {
	position: relative;
	top: expression(( this.offsetParent.scrollTop > this.parentElement.parentElement.offsetTop ? this.offsetParent.scrollTop-this.parentElement.parentElement.offsetTop-1 : 0) -1);
}
</style>

	<!-- 全部计算记录 -->
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
		<div class="col-lg-11 col-md-11 col-sm-11 col-xs-11">
			<table class="table table-bordered table-hover table-condensed table-body-scroll">
				<caption>全部计算记录</caption>
				<thead style="display: block; width: 100%;">
					<tr style="width: 100%;">
						<td style="width: 30%;">
							<nobr>时间</nobr>
						</td>
						<td style="width: 40%;">
							<nobr>数据名称</nobr>
						</td>
						<td style="width: 30%;">
							<nobr>网友昵称</nobr>
						</td>
					</tr>
				</thead>
				<tbody style="overflow-y: auto; max-height: 300px; display: block;word-break:break-all;">
					<c:forEach items="${ commonRecordPageAll.content }" var="commonRecord">
						<tr>
							<td style="width: 30%;">
								<fmt:formatDate value="${commonRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td style="width: 40%;" title="<c:out value='${commonRecord.name }' />">
								<a href="${ctx}/maCalc/${commonRecord.id}" target="_blank">
									<c:out value='${commonRecord.name }' />
								</a>
							</td>
							<td style="width: 30%;" title="<c:out value='${commonRecord.userName }' />">
								<c:out value='${commonRecord.userName }' />
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<!-- 我的计算记录 -->
	<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
		<div class="col-lg-11 col-md-11 col-sm-11 col-xs-11">
			<table class="table table-bordered table-hover table-condensed table-body-scroll">
				<caption>我的计算记录</caption>
				<thead style="display: block; width: 100%;">
					<tr style="width: 100%;">
						<td style="width: 30%;">
							<nobr>时间</nobr>
						</td>
						<td style="width: 40%;">
							<nobr>数据名称</nobr>
						</td>
						<td style="width: 30%;">
							<nobr>网友昵称</nobr>
						</td>
					</tr>
				</thead>
				<tbody style="overflow-y: auto; max-height: 300px; display: block;word-break:break-all;">
					<c:forEach items="${ commonRecordPageSelf.content }" var="commonRecord">
						<tr>
							<td style="width: 30%;">
								<fmt:formatDate value="${commonRecord.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td style="width: 40%;" title="<c:out value='${commonRecord.name }' />">
								<a href="${ctx}/maCalc/${commonRecord.id}" target="_blank">
									<c:out value='${commonRecord.name }' />
								</a>
							</td>
							<td style="width: 30%;" title="<c:out value='${commonRecord.userName }' />">
								<c:out value='${commonRecord.userName }' />
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<div>
		<!-- 主页面评论记录 -->
		<c:if test="${false}">
			<c:if test="${!empty commentPageMain and not isSinglePage}">
				<div align="left" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
					主页面网友评论
					<c:forEach items="${ commentPageMain.content }" var="commentRecord">
						<div style="margin-top: 10px;">
							<fmt:formatDate value="${commentRecord.commentDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							<span style="margin-left: 10px;"><c:out value='${empty commentRecord.userName ? "某网友" : commentRecord.userName}' /></span>
							<div>
								<pre><c:out value='${commentRecord.content}' /></pre>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:if>
		</c:if>

		<!-- 全部评论记录 -->
		<c:if test="${!empty commentPageALl and not isSinglePage}">
			<div align="left" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				全部评论记录
				<c:forEach items="${ commentPageALl.content }" var="commentRecord">
					<div style="margin-top: 10px;">
						<fmt:formatDate value="${commentRecord.commentDate}" pattern="yyyy-MM-dd HH:mm:ss" />
						<span style="margin-left: 10px;"><c:out value='${empty commentRecord.userName ? "某网友" : commentRecord.userName}' /></span>
						<div>
							<pre><c:out value='${commentRecord.content}' /></pre>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:if>

		<!-- 对象评论记录 -->
		<c:if test="${!empty commentPageSingle and isSinglePage}">
			<div align="left" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
				<c:if test="${commentPageSingle.content.size() > 0}">
					网友评论
					<c:forEach items="${ commentPageSingle.content }" var="commentRecord">
						<div style="margin-top: 10px;">
							<fmt:formatDate value="${commentRecord.commentDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							<span style="margin-left: 10px;"><c:out value='${empty commentRecord.userName ? "某网友" : commentRecord.userName}' /></span>
							<div>
								<pre><c:out value='${commentRecord.content}' /></pre>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>
		</c:if>

		<div align="left" class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
			<form id="createCommentForm" action="${ctx}/comment/createComment" method="post">

				<input type="hidden" name="replyCommentId" />
				<input type="hidden" name="class2" value="${CommentRecordClass2}" />
				<input type="hidden" name="targetId" value="${maDamage.commonRecordId}" />

				<div class="col-sm-9">输入评论：</div>
				<div class="col-sm-9">
					<textarea name="content" rows="5" cols="100"></textarea>
				</div>
				<div class="col-sm-9">
					您的昵称：
					<input type="text" name="userName" value="<c:out value='${cookieUserName}' />" />
				</div>
				<div class="col-sm-9">
					<input type="button" value="发表评论" onclick="createCommentRecord();" class="btn btn-sm btn-primary" />
				</div>
			</form>
		</div>
	</div>
</div>
