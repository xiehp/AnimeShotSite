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
	private Integer panelValue;

	/** 防御数值 */
	private Integer defaultDef;

	/** 物理防御数值 */
	private Integer physicalDef;

	/** 魔法防御数值 */
	private Integer magicDef;

	/** 耐性防御数值 */
	private Integer specialDef;

	/** 耐性防御数值 */
	private Integer specialDefHuo;

	/** 耐性防御数值 */
	private Integer specialDefFeng;

	/** 耐性防御数值 */
	private Integer specialDefShui;

	/** 耐性防御数值 */
	private Integer specialDefGuang;

	/** 耐性防御数值 */
	private Integer specialDefAn;

	/** 锁定加伤百分比 */
	private Integer targetingPercent;

	/** chain数量 */
	private Integer chainNumber;

	/** 卡牌Cost */
	private Integer cardCost;

	/** 克制属性增益百分比 默认为200% */
	private Integer cardElementAttributePercent;

	/** 卡牌吃物理数值百分比 */
	private Integer cardPhysicalPercent;

	/** 卡牌吃魔法数值百分比 */
	private Integer cardMagicPercent;

	/** 卡牌吃血量数值百分比 */
	private Integer cardLifePercent;

	/** 卡牌特定条件伤害提升百分比 */
	private Integer cardValuePercent;

	/** 卡牌穿防百分比 */
	private Integer cardPenetratePercent;

	/** 卡牌是否有chainUp */
	private String cardChainUpFlag;

	/** 卡牌攻击次数 */
	private Integer cardAttackTimes;

	/** ex补正百分比 */
	private Integer exDefaultPercent;

	/** ex物理补正百分比 */
	private Integer exPhysicalPercent;

	/** ex魔法补正百分比 */
	private Integer exMagicPercent;

	// 以下为高级属性
	/** 人物基础血量数值 */
	private Integer peopleLifeValue;

	/** 人物基础物理数值 */
	private Integer peoplePhysicalValue;

	/** 人物基础魔法数值 */
	private Integer peopleMagicValue;

	/** 人物基础回复数值 */
	private Integer peopleRecoveryValue;

	/** 增益基础血量数值 */
	private Integer buffLifeValue;

	/** 增益物理数值 */
	private Integer buffPhysicalValue;

	/** 增益魔法数值 */
	private Integer buffMagicValue;

	/** 增益回复数值 */
	private Integer buffRecoveryValue;

	/** 最大增益数值 */
	private Integer maxBuffValue;

	/** 卡牌类型 1:物理 2:魔法 */
	private String cardType;

	/** 卡牌颜色 1:火 2:水 3:风 4:光 5:暗 */
	private String cardColor;

	/** 卡牌基础伤害数值 */
	private Integer cardBaseValue;

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
	public Integer getPanelValue() {
		return panelValue;
	}

	/**
	 * 设置 最终面板数值.
	 *
	 * @param panelValue 最终面板数值
	 */
	public void setPanelValue(Integer panelValue) {
		this.panelValue = panelValue;
	}

	/**
	 * 获取 物理防御数值.
	 *
	 * @return 物理防御数值
	 */
	public Integer getPhysicalDef() {
		return physicalDef;
	}

	/**
	 * 设置 物理防御数值.
	 *
	 * @param physicalDef 物理防御数值
	 */
	public void setPhysicalDef(Integer physicalDef) {
		this.physicalDef = physicalDef;
	}

	/**
	 * 获取 魔法防御数值.
	 *
	 * @return 魔法防御数值
	 */
	public Integer getMagicDef() {
		return magicDef;
	}

	/**
	 * 设置 魔法防御数值.
	 *
	 * @param magicDef 魔法防御数值
	 */
	public void setMagicDef(Integer magicDef) {
		this.magicDef = magicDef;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Integer getSpecialDef() {
		return specialDef;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDef 耐性防御数值
	 */
	public void setSpecialDef(Integer specialDef) {
		this.specialDef = specialDef;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Integer getSpecialDefHuo() {
		return specialDefHuo;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefHuo 耐性防御数值
	 */
	public void setSpecialDefHuo(Integer specialDefHuo) {
		this.specialDefHuo = specialDefHuo;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Integer getSpecialDefFeng() {
		return specialDefFeng;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefFeng 耐性防御数值
	 */
	public void setSpecialDefFeng(Integer specialDefFeng) {
		this.specialDefFeng = specialDefFeng;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Integer getSpecialDefShui() {
		return specialDefShui;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefShui 耐性防御数值
	 */
	public void setSpecialDefShui(Integer specialDefShui) {
		this.specialDefShui = specialDefShui;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Integer getSpecialDefGuang() {
		return specialDefGuang;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefGuang 耐性防御数值
	 */
	public void setSpecialDefGuang(Integer specialDefGuang) {
		this.specialDefGuang = specialDefGuang;
	}

	/**
	 * 获取 耐性防御数值.
	 *
	 * @return 耐性防御数值
	 */
	public Integer getSpecialDefAn() {
		return specialDefAn;
	}

	/**
	 * 设置 耐性防御数值.
	 *
	 * @param specialDefAn 耐性防御数值
	 */
	public void setSpecialDefAn(Integer specialDefAn) {
		this.specialDefAn = specialDefAn;
	}

	/**
	 * 获取 chain数量.
	 *
	 * @return chain数量
	 */
	public Integer getChainNumber() {
		return chainNumber;
	}

	/**
	 * 设置 chain数量.
	 *
	 * @param chainNumber chain数量
	 */
	public void setChainNumber(Integer chainNumber) {
		this.chainNumber = chainNumber;
	}

	/**
	 * 获取 卡牌Cost.
	 *
	 * @return 卡牌Cost
	 */
	public Integer getCardCost() {
		return cardCost;
	}

	/**
	 * 设置 卡牌Cost.
	 *
	 * @param cardCost 卡牌Cost
	 */
	public void setCardCost(Integer cardCost) {
		this.cardCost = cardCost;
	}

	/**
	 * 获取 克制属性增益百分比 默认为200%.
	 *
	 * @return 克制属性增益百分比 默认为200%
	 */
	public Integer getCardElementAttributePercent() {
		return cardElementAttributePercent;
	}

	/**
	 * 设置 克制属性增益百分比 默认为200%.
	 *
	 * @param cardElementAttributePercent 克制属性增益百分比 默认为200%
	 */
	public void setCardElementAttributePercent(Integer cardElementAttributePercent) {
		this.cardElementAttributePercent = cardElementAttributePercent;
	}

	/**
	 * 获取 卡牌吃物理数值百分比.
	 *
	 * @return 卡牌吃物理数值百分比
	 */
	public Integer getCardPhysicalPercent() {
		return cardPhysicalPercent;
	}

	/**
	 * 设置 卡牌吃物理数值百分比.
	 *
	 * @param cardPhysicalPercent 卡牌吃物理数值百分比
	 */
	public void setCardPhysicalPercent(Integer cardPhysicalPercent) {
		this.cardPhysicalPercent = cardPhysicalPercent;
	}

	/**
	 * 获取 卡牌吃魔法数值百分比.
	 *
	 * @return 卡牌吃魔法数值百分比
	 */
	public Integer getCardMagicPercent() {
		return cardMagicPercent;
	}

	/**
	 * 设置 卡牌吃魔法数值百分比.
	 *
	 * @param cardMagicPercent 卡牌吃魔法数值百分比
	 */
	public void setCardMagicPercent(Integer cardMagicPercent) {
		this.cardMagicPercent = cardMagicPercent;
	}

	/**
	 * 获取 卡牌吃血量数值百分比.
	 *
	 * @return 卡牌吃血量数值百分比
	 */
	public Integer getCardLifePercent() {
		return cardLifePercent;
	}

	/**
	 * 设置 卡牌吃血量数值百分比.
	 *
	 * @param cardLifePercent 卡牌吃血量数值百分比
	 */
	public void setCardLifePercent(Integer cardLifePercent) {
		this.cardLifePercent = cardLifePercent;
	}

	/**
	 * 获取 卡牌特定条件伤害提升百分比.
	 *
	 * @return 卡牌特定条件伤害提升百分比
	 */
	public Integer getCardValuePercent() {
		return cardValuePercent;
	}

	/**
	 * 设置 卡牌特定条件伤害提升百分比.
	 *
	 * @param cardValuePercent 卡牌特定条件伤害提升百分比
	 */
	public void setCardValuePercent(Integer cardValuePercent) {
		this.cardValuePercent = cardValuePercent;
	}

	/**
	 * 获取 卡牌穿防百分比.
	 *
	 * @return 卡牌穿防百分比
	 */
	public Integer getCardPenetratePercent() {
		return cardPenetratePercent;
	}

	/**
	 * 设置 卡牌穿防百分比.
	 *
	 * @param cardPenetratePercent 卡牌穿防百分比
	 */
	public void setCardPenetratePercent(Integer cardPenetratePercent) {
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
	public Integer getCardAttackTimes() {
		return cardAttackTimes;
	}

	/**
	 * 设置 卡牌攻击次数.
	 *
	 * @param cardAttackTimes 卡牌攻击次数
	 */
	public void setCardAttackTimes(Integer cardAttackTimes) {
		this.cardAttackTimes = cardAttackTimes;
	}

	/**
	 * 获取 ex物理补正百分比.
	 *
	 * @return ex物理补正百分比
	 */
	public Integer getExPhysicalPercent() {
		return exPhysicalPercent;
	}

	/**
	 * 设置 ex物理补正百分比.
	 *
	 * @param exPhysicalPercent ex物理补正百分比
	 */
	public void setExPhysicalPercent(Integer exPhysicalPercent) {
		this.exPhysicalPercent = exPhysicalPercent;
	}

	/**
	 * 获取 ex魔法补正百分比.
	 *
	 * @return ex魔法补正百分比
	 */
	public Integer getExMagicPercent() {
		return exMagicPercent;
	}

	/**
	 * 设置 ex魔法补正百分比.
	 *
	 * @param exMagicPercent ex魔法补正百分比
	 */
	public void setExMagicPercent(Integer exMagicPercent) {
		this.exMagicPercent = exMagicPercent;
	}

	/**
	 * 获取 人物基础血量数值.
	 *
	 * @return 人物基础血量数值
	 */
	public Integer getPeopleLifeValue() {
		return peopleLifeValue;
	}

	/**
	 * 设置 人物基础血量数值.
	 *
	 * @param peopleLifeValue 人物基础血量数值
	 */
	public void setPeopleLifeValue(Integer peopleLifeValue) {
		this.peopleLifeValue = peopleLifeValue;
	}

	/**
	 * 获取 人物基础物理数值.
	 *
	 * @return 人物基础物理数值
	 */
	public Integer getPeoplePhysicalValue() {
		return peoplePhysicalValue;
	}

	/**
	 * 设置 人物基础物理数值.
	 *
	 * @param peoplePhysicalValue 人物基础物理数值
	 */
	public void setPeoplePhysicalValue(Integer peoplePhysicalValue) {
		this.peoplePhysicalValue = peoplePhysicalValue;
	}

	/**
	 * 获取 人物基础魔法数值.
	 *
	 * @return 人物基础魔法数值
	 */
	public Integer getPeopleMagicValue() {
		return peopleMagicValue;
	}

	/**
	 * 设置 人物基础魔法数值.
	 *
	 * @param peopleMagicValue 人物基础魔法数值
	 */
	public void setPeopleMagicValue(Integer peopleMagicValue) {
		this.peopleMagicValue = peopleMagicValue;
	}

	/**
	 * 获取 人物基础回复数值.
	 *
	 * @return 人物基础回复数值
	 */
	public Integer getPeopleRecoveryValue() {
		return peopleRecoveryValue;
	}

	/**
	 * 设置 人物基础回复数值.
	 *
	 * @param peopleRecoveryValue 人物基础回复数值
	 */
	public void setPeopleRecoveryValue(Integer peopleRecoveryValue) {
		this.peopleRecoveryValue = peopleRecoveryValue;
	}

	/**
	 * 获取 增益基础血量数值.
	 *
	 * @return 增益基础血量数值
	 */
	public Integer getBuffLifeValue() {
		return buffLifeValue;
	}

	/**
	 * 设置 增益基础血量数值.
	 *
	 * @param buffLifeValue 增益基础血量数值
	 */
	public void setBuffLifeValue(Integer buffLifeValue) {
		this.buffLifeValue = buffLifeValue;
	}

	/**
	 * 获取 增益物理数值.
	 *
	 * @return 增益物理数值
	 */
	public Integer getBuffPhysicalValue() {
		return buffPhysicalValue;
	}

	/**
	 * 设置 增益物理数值.
	 *
	 * @param buffPhysicalValue 增益物理数值
	 */
	public void setBuffPhysicalValue(Integer buffPhysicalValue) {
		this.buffPhysicalValue = buffPhysicalValue;
	}

	/**
	 * 获取 增益魔法数值.
	 *
	 * @return 增益魔法数值
	 */
	public Integer getBuffMagicValue() {
		return buffMagicValue;
	}

	/**
	 * 设置 增益魔法数值.
	 *
	 * @param buffMagicValue 增益魔法数值
	 */
	public void setBuffMagicValue(Integer buffMagicValue) {
		this.buffMagicValue = buffMagicValue;
	}

	/**
	 * 获取 增益回复数值.
	 *
	 * @return 增益回复数值
	 */
	public Integer getBuffRecoveryValue() {
		return buffRecoveryValue;
	}

	/**
	 * 设置 增益回复数值.
	 *
	 * @param buffRecoveryValue 增益回复数值
	 */
	public void setBuffRecoveryValue(Integer buffRecoveryValue) {
		this.buffRecoveryValue = buffRecoveryValue;
	}

	/**
	 * 获取 最大增益数值.
	 *
	 * @return 最大增益数值
	 */
	public Integer getMaxBuffValue() {
		return maxBuffValue;
	}

	/**
	 * 设置 最大增益数值.
	 *
	 * @param maxBuffValue 最大增益数值
	 */
	public void setMaxBuffValue(Integer maxBuffValue) {
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
	public Integer getCardBaseValue() {
		return cardBaseValue;
	}

	/**
	 * 设置 卡牌基础伤害数值.
	 *
	 * @param cardBaseValue 卡牌基础伤害数值
	 */
	public void setCardBaseValue(Integer cardBaseValue) {
		this.cardBaseValue = cardBaseValue;
	}

	/**
	 * 获取 防御数值.
	 *
	 * @return 防御数值
	 */
	public Integer getDefaultDef() {
		return defaultDef;
	}

	/**
	 * 设置 防御数值.
	 *
	 * @param defaultDef 防御数值
	 */
	public void setDefaultDef(Integer defaultDef) {
		this.defaultDef = defaultDef;
	}

	/**
	 * 获取 ex补正百分比.
	 *
	 * @return ex补正百分比
	 */
	public Integer getExDefaultPercent() {
		return exDefaultPercent;
	}

	/**
	 * 设置 ex补正百分比.
	 *
	 * @param exDefaultPercent ex补正百分比
	 */
	public void setExDefaultPercent(Integer exDefaultPercent) {
		this.exDefaultPercent = exDefaultPercent;
	}

	/**
	 * 获取 锁定加伤百分比.
	 *
	 * @return 锁定加伤百分比
	 */
	public Integer getTargetingPercent() {
		return targetingPercent;
	}

	/**
	 * 设置 锁定加伤百分比.
	 *
	 * @param targetingPercent 锁定加伤百分比
	 */
	public void setTargetingPercent(Integer targetingPercent) {
		this.targetingPercent = targetingPercent;
	}

}
