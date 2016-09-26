package xie.other.ma.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * MA伤害数值
 */
@Entity
@Table(name = MaDamage.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MaDamage extends CommonRecordDetail {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "ma_damage";

	/** 是否使用高级设置 */
	private String advancedSettingFlag;

	/** 最终面板数值 */
	private Long panelValue;

	/** 防御数值 */
	private Long defaultDef;

	/** 物理防御数值 */
	private Long physicalDef;

	/** 魔法防御数值 */
	private Long magicDef;

	/** 耐性防御数值 */
	private Long specialDef;

	/** 耐性防御数值 */
	private Long specialDefHuo;

	/** 耐性防御数值 */
	private Long specialDefFeng;

	/** 耐性防御数值 */
	private Long specialDefShui;

	/** 耐性防御数值 */
	private Long specialDefGuang;

	/** 耐性防御数值 */
	private Long specialDefAn;

	/** 锁定加伤百分比 */
	private Long targetingPercent;

	/** chain数量 */
	private Long chainNumber;

	/** 卡牌Cost */
	private Long cardCost;

	/** 克制属性增益百分比 默认为200% */
	private Long cardElementAttributePercent;

	/** 卡牌吃物理数值百分比 */
	private Long cardPhysicalPercent;

	/** 卡牌吃魔法数值百分比 */
	private Long cardMagicPercent;

	/** 卡牌吃血量数值百分比 */
	private Long cardLifePercent;

	/** 卡牌特定条件伤害提升百分比 */
	private Long cardValuePercent;

	/** 卡牌穿防百分比 */
	private Long cardPenetratePercent;

	/** 卡牌是否有chainUp */
	private String cardChainUpFlag;

	/** 卡牌攻击次数 */
	private Long cardAttackTimes;

	/** ex补正百分比 */
	private Long exDefaultPercent;

	/** ex物理补正百分比 */
	private Long exPhysicalPercent;

	/** ex魔法补正百分比 */
	private Long exMagicPercent;

	// 以下为高级属性
	/** 人物基础血量数值 */
	private Long peopleLifeValue;

	/** 人物基础物理数值 */
	private Long peoplePhysicalValue;

	/** 人物基础魔法数值 */
	private Long peopleMagicValue;

	/** 人物基础回复数值 */
	private Long peopleRecoveryValue;

	/** 增益基础血量数值 */
	private Long buffLifeValue;

	/** 增益物理数值 */
	private Long buffPhysicalValue;

	/** 增益魔法数值 */
	private Long buffMagicValue;

	/** 增益回复数值 */
	private Long buffRecoveryValue;

	/** 最大增益数值 */
	private Long maxBuffValue;

	/** 卡牌类型 1:物理 2:魔法 */
	private String cardType;

	/** 卡牌颜色 1:火 2:水 3:风 4:光 5:暗 */
	private String cardColor;

	/** 卡牌基础伤害数值 */
	private Long cardBaseValue;

	/**
	 * 获取 是否使用高级设置.
	 *
	 * @return 是否使用高级设置
	 */
	public String getAdvancedSettingFlag() {
		return advancedSettingFlag;
	}

	/**
	 * 设置 是否使用高级设置.
	 *
	 * @param advancedSettingFlag 是否使用高级设置
	 */
	public void setAdvancedSettingFlag(String advancedSettingFlag) {
		this.advancedSettingFlag = advancedSettingFlag;
	}

	/**
	 * 获取 最终面板数值.
	 *
	 * @return 最终面板数值
	 */
	public Long getPanelValue() {
		return panelValue;
	}

	/**
	 * 设置 最终面板数值.
	 *
	 * @param panelValue 最终面板数值
	 */
	public void setPanelValue(Long panelValue) {
		this.panelValue = panelValue;
	}

	/**
	 * 获取 物理防御数值.
	 *
	 * @return 物理防御数值
	 */
	public Long getPhysicalDef() {
		return physicalDef;
	}

	/**
	 * 设置 物理防御数值.
	 *
	 * @param physicalDef 物理防御数值
	 */
	public void setPhysicalDef(Long physicalDef) {
		this.physicalDef = physicalDef;
	}

	/**
	 * 获取 魔法防御数值.
	 *
	 * @return 魔法防御数值
	 */
	public Long getMagicDef() {
		return magicDef;
	}

	/**
	 * 设置 魔法防御数值.
	 *
	 * @param magicDef 魔法防御数值
	 */
	public void setMagicDef(Long magicDef) {
		this.magicDef = magicDef;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Long getSpecialDef() {
		return specialDef;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDef 耐性防御数值
	 */
	public void setSpecialDef(Long specialDef) {
		this.specialDef = specialDef;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Long getSpecialDefHuo() {
		return specialDefHuo;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefHuo 耐性防御数值
	 */
	public void setSpecialDefHuo(Long specialDefHuo) {
		this.specialDefHuo = specialDefHuo;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Long getSpecialDefFeng() {
		return specialDefFeng;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefFeng 耐性防御数值
	 */
	public void setSpecialDefFeng(Long specialDefFeng) {
		this.specialDefFeng = specialDefFeng;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Long getSpecialDefShui() {
		return specialDefShui;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefShui 耐性防御数值
	 */
	public void setSpecialDefShui(Long specialDefShui) {
		this.specialDefShui = specialDefShui;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Long getSpecialDefGuang() {
		return specialDefGuang;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefGuang 耐性防御数值
	 */
	public void setSpecialDefGuang(Long specialDefGuang) {
		this.specialDefGuang = specialDefGuang;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Long getSpecialDefAn() {
		return specialDefAn;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefAn 耐性防御数值
	 */
	public void setSpecialDefAn(Long specialDefAn) {
		this.specialDefAn = specialDefAn;
	}

	/**
	 * 获取 chain数量.
	 *
	 * @return chain数量
	 */
	public Long getChainNumber() {
		return chainNumber;
	}

	/**
	 * 设置 chain数量.
	 *
	 * @param chainNumber chain数量
	 */
	public void setChainNumber(Long chainNumber) {
		this.chainNumber = chainNumber;
	}

	/**
	 * 获取 卡牌Cost.
	 *
	 * @return 卡牌Cost
	 */
	public Long getCardCost() {
		return cardCost;
	}

	/**
	 * 设置 卡牌Cost.
	 *
	 * @param cardCost 卡牌Cost
	 */
	public void setCardCost(Long cardCost) {
		this.cardCost = cardCost;
	}

	/**
	 * 获取 克制属性增益百分比 默认为200%.
	 *
	 * @return 克制属性增益百分比 默认为200%
	 */
	public Long getCardElementAttributePercent() {
		return cardElementAttributePercent;
	}

	/**
	 * 设置 克制属性增益百分比 默认为200%.
	 *
	 * @param cardElementAttributePercent 克制属性增益百分比 默认为200%
	 */
	public void setCardElementAttributePercent(Long cardElementAttributePercent) {
		this.cardElementAttributePercent = cardElementAttributePercent;
	}

	/**
	 * 获取 卡牌吃物理数值百分比.
	 *
	 * @return 卡牌吃物理数值百分比
	 */
	public Long getCardPhysicalPercent() {
		return cardPhysicalPercent;
	}

	/**
	 * 设置 卡牌吃物理数值百分比.
	 *
	 * @param cardPhysicalPercent 卡牌吃物理数值百分比
	 */
	public void setCardPhysicalPercent(Long cardPhysicalPercent) {
		this.cardPhysicalPercent = cardPhysicalPercent;
	}

	/**
	 * 获取 卡牌吃魔法数值百分比.
	 *
	 * @return 卡牌吃魔法数值百分比
	 */
	public Long getCardMagicPercent() {
		return cardMagicPercent;
	}

	/**
	 * 设置 卡牌吃魔法数值百分比.
	 *
	 * @param cardMagicPercent 卡牌吃魔法数值百分比
	 */
	public void setCardMagicPercent(Long cardMagicPercent) {
		this.cardMagicPercent = cardMagicPercent;
	}

	/**
	 * 获取 卡牌吃血量数值百分比.
	 *
	 * @return 卡牌吃血量数值百分比
	 */
	public Long getCardLifePercent() {
		return cardLifePercent;
	}

	/**
	 * 设置 卡牌吃血量数值百分比.
	 *
	 * @param cardLifePercent 卡牌吃血量数值百分比
	 */
	public void setCardLifePercent(Long cardLifePercent) {
		this.cardLifePercent = cardLifePercent;
	}

	/**
	 * 获取 卡牌特定条件伤害提升百分比.
	 *
	 * @return 卡牌特定条件伤害提升百分比
	 */
	public Long getCardValuePercent() {
		return cardValuePercent;
	}

	/**
	 * 设置 卡牌特定条件伤害提升百分比.
	 *
	 * @param cardValuePercent 卡牌特定条件伤害提升百分比
	 */
	public void setCardValuePercent(Long cardValuePercent) {
		this.cardValuePercent = cardValuePercent;
	}

	/**
	 * 获取 卡牌穿防百分比.
	 *
	 * @return 卡牌穿防百分比
	 */
	public Long getCardPenetratePercent() {
		return cardPenetratePercent;
	}

	/**
	 * 设置 卡牌穿防百分比.
	 *
	 * @param cardPenetratePercent 卡牌穿防百分比
	 */
	public void setCardPenetratePercent(Long cardPenetratePercent) {
		this.cardPenetratePercent = cardPenetratePercent;
	}

	/**
	 * 获取 卡牌是否有chainUp.
	 *
	 * @return 卡牌是否有chainUp
	 */
	public String getCardChainUpFlag() {
		return cardChainUpFlag;
	}

	/**
	 * 设置 卡牌是否有chainUp.
	 *
	 * @param cardChainUpFlag 卡牌是否有chainUp
	 */
	public void setCardChainUpFlag(String cardChainUpFlag) {
		this.cardChainUpFlag = cardChainUpFlag;
	}

	/**
	 * 获取 卡牌攻击次数.
	 *
	 * @return 卡牌攻击次数
	 */
	public Long getCardAttackTimes() {
		return cardAttackTimes;
	}

	/**
	 * 设置 卡牌攻击次数.
	 *
	 * @param cardAttackTimes 卡牌攻击次数
	 */
	public void setCardAttackTimes(Long cardAttackTimes) {
		this.cardAttackTimes = cardAttackTimes;
	}

	/**
	 * 获取 ex物理补正百分比.
	 *
	 * @return ex物理补正百分比
	 */
	public Long getExPhysicalPercent() {
		return exPhysicalPercent;
	}

	/**
	 * 设置 ex物理补正百分比.
	 *
	 * @param exPhysicalPercent ex物理补正百分比
	 */
	public void setExPhysicalPercent(Long exPhysicalPercent) {
		this.exPhysicalPercent = exPhysicalPercent;
	}

	/**
	 * 获取 ex魔法补正百分比.
	 *
	 * @return ex魔法补正百分比
	 */
	public Long getExMagicPercent() {
		return exMagicPercent;
	}

	/**
	 * 设置 ex魔法补正百分比.
	 *
	 * @param exMagicPercent ex魔法补正百分比
	 */
	public void setExMagicPercent(Long exMagicPercent) {
		this.exMagicPercent = exMagicPercent;
	}

	/**
	 * 获取 人物基础血量数值.
	 *
	 * @return 人物基础血量数值
	 */
	public Long getPeopleLifeValue() {
		return peopleLifeValue;
	}

	/**
	 * 设置 人物基础血量数值.
	 *
	 * @param peopleLifeValue 人物基础血量数值
	 */
	public void setPeopleLifeValue(Long peopleLifeValue) {
		this.peopleLifeValue = peopleLifeValue;
	}

	/**
	 * 获取 人物基础物理数值.
	 *
	 * @return 人物基础物理数值
	 */
	public Long getPeoplePhysicalValue() {
		return peoplePhysicalValue;
	}

	/**
	 * 设置 人物基础物理数值.
	 *
	 * @param peoplePhysicalValue 人物基础物理数值
	 */
	public void setPeoplePhysicalValue(Long peoplePhysicalValue) {
		this.peoplePhysicalValue = peoplePhysicalValue;
	}

	/**
	 * 获取 人物基础魔法数值.
	 *
	 * @return 人物基础魔法数值
	 */
	public Long getPeopleMagicValue() {
		return peopleMagicValue;
	}

	/**
	 * 设置 人物基础魔法数值.
	 *
	 * @param peopleMagicValue 人物基础魔法数值
	 */
	public void setPeopleMagicValue(Long peopleMagicValue) {
		this.peopleMagicValue = peopleMagicValue;
	}

	/**
	 * 获取 人物基础回复数值.
	 *
	 * @return 人物基础回复数值
	 */
	public Long getPeopleRecoveryValue() {
		return peopleRecoveryValue;
	}

	/**
	 * 设置 人物基础回复数值.
	 *
	 * @param peopleRecoveryValue 人物基础回复数值
	 */
	public void setPeopleRecoveryValue(Long peopleRecoveryValue) {
		this.peopleRecoveryValue = peopleRecoveryValue;
	}

	/**
	 * 获取 增益基础血量数值.
	 *
	 * @return 增益基础血量数值
	 */
	public Long getBuffLifeValue() {
		return buffLifeValue;
	}

	/**
	 * 设置 增益基础血量数值.
	 *
	 * @param buffLifeValue 增益基础血量数值
	 */
	public void setBuffLifeValue(Long buffLifeValue) {
		this.buffLifeValue = buffLifeValue;
	}

	/**
	 * 获取 增益物理数值.
	 *
	 * @return 增益物理数值
	 */
	public Long getBuffPhysicalValue() {
		return buffPhysicalValue;
	}

	/**
	 * 设置 增益物理数值.
	 *
	 * @param buffPhysicalValue 增益物理数值
	 */
	public void setBuffPhysicalValue(Long buffPhysicalValue) {
		this.buffPhysicalValue = buffPhysicalValue;
	}

	/**
	 * 获取 增益魔法数值.
	 *
	 * @return 增益魔法数值
	 */
	public Long getBuffMagicValue() {
		return buffMagicValue;
	}

	/**
	 * 设置 增益魔法数值.
	 *
	 * @param buffMagicValue 增益魔法数值
	 */
	public void setBuffMagicValue(Long buffMagicValue) {
		this.buffMagicValue = buffMagicValue;
	}

	/**
	 * 获取 增益回复数值.
	 *
	 * @return 增益回复数值
	 */
	public Long getBuffRecoveryValue() {
		return buffRecoveryValue;
	}

	/**
	 * 设置 增益回复数值.
	 *
	 * @param buffRecoveryValue 增益回复数值
	 */
	public void setBuffRecoveryValue(Long buffRecoveryValue) {
		this.buffRecoveryValue = buffRecoveryValue;
	}

	/**
	 * 获取 最大增益数值.
	 *
	 * @return 最大增益数值
	 */
	public Long getMaxBuffValue() {
		return maxBuffValue;
	}

	/**
	 * 设置 最大增益数值.
	 *
	 * @param maxBuffValue 最大增益数值
	 */
	public void setMaxBuffValue(Long maxBuffValue) {
		this.maxBuffValue = maxBuffValue;
	}

	/**
	 * 获取 卡牌类型 1:物理 2:魔法.
	 *
	 * @return 卡牌类型 1:物理 2:魔法
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * 设置 卡牌类型 1:物理 2:魔法.
	 *
	 * @param cardType 卡牌类型 1:物理 2:魔法
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * 获取 卡牌颜色 1:火 2:水 3:风 4:光 5:暗.
	 *
	 * @return 卡牌颜色 1:火 2:水 3:风 4:光 5:暗
	 */
	public String getCardColor() {
		return cardColor;
	}

	/**
	 * 设置 卡牌颜色 1:火 2:水 3:风 4:光 5:暗.
	 *
	 * @param cardColor 卡牌颜色 1:火 2:水 3:风 4:光 5:暗
	 */
	public void setCardColor(String cardColor) {
		this.cardColor = cardColor;
	}

	/**
	 * 获取 卡牌基础伤害数值.
	 *
	 * @return 卡牌基础伤害数值
	 */
	public Long getCardBaseValue() {
		return cardBaseValue;
	}

	/**
	 * 设置 卡牌基础伤害数值.
	 *
	 * @param cardBaseValue 卡牌基础伤害数值
	 */
	public void setCardBaseValue(Long cardBaseValue) {
		this.cardBaseValue = cardBaseValue;
	}

	/**
	 * 获取 防御数值.
	 *
	 * @return 防御数值
	 */
	public Long getDefaultDef() {
		return defaultDef;
	}

	/**
	 * 设置 防御数值.
	 *
	 * @param defaultDef 防御数值
	 */
	public void setDefaultDef(Long defaultDef) {
		this.defaultDef = defaultDef;
	}

	/**
	 * 获取 ex补正百分比.
	 *
	 * @return ex补正百分比
	 */
	public Long getExDefaultPercent() {
		return exDefaultPercent;
	}

	/**
	 * 设置 ex补正百分比.
	 *
	 * @param exDefaultPercent ex补正百分比
	 */
	public void setExDefaultPercent(Long exDefaultPercent) {
		this.exDefaultPercent = exDefaultPercent;
	}

	/**
	 * 获取 锁定加伤百分比.
	 *
	 * @return 锁定加伤百分比
	 */
	public Long getTargetingPercent() {
		return targetingPercent;
	}

	/**
	 * 设置 锁定加伤百分比.
	 *
	 * @param targetingPercent 锁定加伤百分比
	 */
	public void setTargetingPercent(Long targetingPercent) {
		this.targetingPercent = targetingPercent;
	}

}
