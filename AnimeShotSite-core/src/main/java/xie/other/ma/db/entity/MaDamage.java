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

	public String getAdvancedSettingFlag() {
		return advancedSettingFlag;
	}

	public void setAdvancedSettingFlag(String advancedSettingFlag) {
		this.advancedSettingFlag = advancedSettingFlag;
	}

	public Integer getPanelValue() {
		return panelValue;
	}

	public void setPanelValue(Integer panelValue) {
		this.panelValue = panelValue;
	}

	public Integer getPhysicalDef() {
		return physicalDef;
	}

	public void setPhysicalDef(Integer physicalDef) {
		this.physicalDef = physicalDef;
	}

	public Integer getMagicDef() {
		return magicDef;
	}

	public void setMagicDef(Integer magicDef) {
		this.magicDef = magicDef;
	}

	public Integer getSpecialDef() {
		return specialDef;
	}

	public void setSpecialDef(Integer specialDef) {
		this.specialDef = specialDef;
	}

	public Integer getSpecialDefHuo() {
		return specialDefHuo;
	}

	public void setSpecialDefHuo(Integer specialDefHuo) {
		this.specialDefHuo = specialDefHuo;
	}

	public Integer getSpecialDefFeng() {
		return specialDefFeng;
	}

	public void setSpecialDefFeng(Integer specialDefFeng) {
		this.specialDefFeng = specialDefFeng;
	}

	public Integer getSpecialDefShui() {
		return specialDefShui;
	}

	public void setSpecialDefShui(Integer specialDefShui) {
		this.specialDefShui = specialDefShui;
	}

	public Integer getSpecialDefGuang() {
		return specialDefGuang;
	}

	public void setSpecialDefGuang(Integer specialDefGuang) {
		this.specialDefGuang = specialDefGuang;
	}

	public Integer getSpecialDefAn() {
		return specialDefAn;
	}

	public void setSpecialDefAn(Integer specialDefAn) {
		this.specialDefAn = specialDefAn;
	}

	public Integer getChainNumber() {
		return chainNumber;
	}

	public void setChainNumber(Integer chainNumber) {
		this.chainNumber = chainNumber;
	}

	public Integer getCardCost() {
		return cardCost;
	}

	public void setCardCost(Integer cardCost) {
		this.cardCost = cardCost;
	}

	public Integer getCardElementAttributePercent() {
		return cardElementAttributePercent;
	}

	public void setCardElementAttributePercent(Integer cardElementAttributePercent) {
		this.cardElementAttributePercent = cardElementAttributePercent;
	}

	public Integer getCardPhysicalPercent() {
		return cardPhysicalPercent;
	}

	public void setCardPhysicalPercent(Integer cardPhysicalPercent) {
		this.cardPhysicalPercent = cardPhysicalPercent;
	}

	public Integer getCardMagicPercent() {
		return cardMagicPercent;
	}

	public void setCardMagicPercent(Integer cardMagicPercent) {
		this.cardMagicPercent = cardMagicPercent;
	}

	public Integer getCardLifePercent() {
		return cardLifePercent;
	}

	public void setCardLifePercent(Integer cardLifePercent) {
		this.cardLifePercent = cardLifePercent;
	}

	public Integer getCardValuePercent() {
		return cardValuePercent;
	}

	public void setCardValuePercent(Integer cardValuePercent) {
		this.cardValuePercent = cardValuePercent;
	}

	public Integer getCardPenetratePercent() {
		return cardPenetratePercent;
	}

	public void setCardPenetratePercent(Integer cardPenetratePercent) {
		this.cardPenetratePercent = cardPenetratePercent;
	}

	public String getCardChainUpFlag() {
		return cardChainUpFlag;
	}

	public void setCardChainUpFlag(String cardChainUpFlag) {
		this.cardChainUpFlag = cardChainUpFlag;
	}

	public Integer getCardAttackTimes() {
		return cardAttackTimes;
	}

	public void setCardAttackTimes(Integer cardAttackTimes) {
		this.cardAttackTimes = cardAttackTimes;
	}

	public Integer getExPhysicalPercent() {
		return exPhysicalPercent;
	}

	public void setExPhysicalPercent(Integer exPhysicalPercent) {
		this.exPhysicalPercent = exPhysicalPercent;
	}

	public Integer getExMagicPercent() {
		return exMagicPercent;
	}

	public void setExMagicPercent(Integer exMagicPercent) {
		this.exMagicPercent = exMagicPercent;
	}

	public Integer getPeopleLifeValue() {
		return peopleLifeValue;
	}

	public void setPeopleLifeValue(Integer peopleLifeValue) {
		this.peopleLifeValue = peopleLifeValue;
	}

	public Integer getPeoplePhysicalValue() {
		return peoplePhysicalValue;
	}

	public void setPeoplePhysicalValue(Integer peoplePhysicalValue) {
		this.peoplePhysicalValue = peoplePhysicalValue;
	}

	public Integer getPeopleMagicValue() {
		return peopleMagicValue;
	}

	public void setPeopleMagicValue(Integer peopleMagicValue) {
		this.peopleMagicValue = peopleMagicValue;
	}

	public Integer getPeopleRecoveryValue() {
		return peopleRecoveryValue;
	}

	public void setPeopleRecoveryValue(Integer peopleRecoveryValue) {
		this.peopleRecoveryValue = peopleRecoveryValue;
	}

	public Integer getBuffLifeValue() {
		return buffLifeValue;
	}

	public void setBuffLifeValue(Integer buffLifeValue) {
		this.buffLifeValue = buffLifeValue;
	}

	public Integer getBuffPhysicalValue() {
		return buffPhysicalValue;
	}

	public void setBuffPhysicalValue(Integer buffPhysicalValue) {
		this.buffPhysicalValue = buffPhysicalValue;
	}

	public Integer getBuffMagicValue() {
		return buffMagicValue;
	}

	public void setBuffMagicValue(Integer buffMagicValue) {
		this.buffMagicValue = buffMagicValue;
	}

	public Integer getBuffRecoveryValue() {
		return buffRecoveryValue;
	}

	public void setBuffRecoveryValue(Integer buffRecoveryValue) {
		this.buffRecoveryValue = buffRecoveryValue;
	}

	public Integer getMaxBuffValue() {
		return maxBuffValue;
	}

	public void setMaxBuffValue(Integer maxBuffValue) {
		this.maxBuffValue = maxBuffValue;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardColor() {
		return cardColor;
	}

	public void setCardColor(String cardColor) {
		this.cardColor = cardColor;
	}

	public Integer getCardBaseValue() {
		return cardBaseValue;
	}

	public void setCardBaseValue(Integer cardBaseValue) {
		this.cardBaseValue = cardBaseValue;
	}

	public Integer getDefaultDef() {
		return defaultDef;
	}

	public void setDefaultDef(Integer defaultDef) {
		this.defaultDef = defaultDef;
	}

	public Integer getExDefaultPercent() {
		return exDefaultPercent;
	}

	public void setExDefaultPercent(Integer exDefaultPercent) {
		this.exDefaultPercent = exDefaultPercent;
	}

	public Integer getTargetingPercent() {
		return targetingPercent;
	}

	public void setTargetingPercent(Integer targetingPercent) {
		this.targetingPercent = targetingPercent;
	}

}
