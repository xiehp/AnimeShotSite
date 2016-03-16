package xie.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import xie.common.Constants;

public class NumberUtil {

	public static final String NUMBER_INT = "###";
	public static final String NUMBER_FLOAT = "####.##";

	public static final String NUMBER_FORMAT1 = "#,###.00";
	public static final String NUMBER_FORMAT2 = "#,##0.####";
	public static final String NUMBER_FORMAT3 = "#,###";

	public static String getNumberFormat(BigDecimal number) {
		return getNumberFormat(number, NUMBER_FORMAT1);
	}

	public static String getNumberFormat2(BigDecimal number) {
		return getNumberFormat(number, NUMBER_FORMAT3);
	}

	public static String getNumberFormat(BigDecimal number, String format) {
		DecimalFormat df = new DecimalFormat(format);
		String decimalStr = df.format(number);
		return decimalStr;
	}

	/**
	 * 等于
	 */
	public static boolean equal(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2) == 0;
	}

	/**
	 * 大于
	 */
	public static boolean greaterThan(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2) > 0;
	}

	/**
	 * 大于等于
	 */
	public static boolean greaterEqualThan(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2) >= 0;
	}

	/**
	 * 小于
	 */
	public static boolean lessThan(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2) < 0;
	}

	/**
	 * 小于等于
	 */
	public static boolean lessEqualThan(BigDecimal number1, BigDecimal number2) {
		return number1.compareTo(number2) <= 0;
	}

	/**
	 * 相加
	 */
	public static BigDecimal add(BigDecimal number1, BigDecimal number2) {
		if (number1 == null) {
			number1 = new BigDecimal(0);
		}
		if (number2 == null) {
			number2 = new BigDecimal(0);
		}

		return number1.add(number2);
	}

	/**
	 * 相减
	 */
	public static BigDecimal subtract(BigDecimal number1, BigDecimal number2) {
		if (number1 == null) {
			number1 = new BigDecimal(0);
		}
		if (number2 == null) {
			number2 = new BigDecimal(0);
		}

		return number1.subtract(number2);
	}

	/**
	 * 是否为yes flag
	 */
	public static boolean yesFlg(Integer integer) {
		if (integer == null) {
			integer = new Integer(0);
		}
		return integer == Constants.FLAG_INT_YES;
	}

	/**
	 * 是否为yes flag
	 */
	public static boolean noFlg(Integer integer) {
		if (integer == null) {
			integer = new Integer(0);
		}
		return integer == Constants.FLAG_INT_NO;
	}
}
