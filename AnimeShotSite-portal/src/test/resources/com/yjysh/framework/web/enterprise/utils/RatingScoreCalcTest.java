package com.yjysh.framework.web.enterprise.utils;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;


public class RatingScoreCalcTest  {

	
	@Test
	public void test1(){

		RatingMethod ratingScoreMethod = new RatingMethod();
		ratingScoreMethod.setMinValue(new BigDecimal(100));
		ratingScoreMethod.setMaxValue(new BigDecimal(200));
		ratingScoreMethod.setInitScore(new BigDecimal(1));
		ratingScoreMethod.setTotalScore(new BigDecimal(12));

		
		
		RatingCalc ratingScoreCalc = new RatingCalc();

		System.out.println("");
		System.out.println("-----------test1--------------");
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(99), ratingScoreMethod ));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(1), ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("1.011"), ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("1.11"), ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("7.05"), ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("11.89"), ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("11.989"), ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));

		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(1), ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("1"), ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("1"), ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("7"), ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("11"), ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("11"), ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));
	}

	
	@Test
	public void test2(){

		RatingMethod ratingScoreMethod = new RatingMethod();
		ratingScoreMethod.setMinValue(new BigDecimal(100));
		ratingScoreMethod.setMaxValue(new BigDecimal(200));
		ratingScoreMethod.setInitScore(new BigDecimal(0));
		ratingScoreMethod.setTotalScore(new BigDecimal(12));

		
		
		RatingCalc ratingScoreCalc = new RatingCalc();


		System.out.println("");
		System.out.println("-----------test2--------------");
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(0), ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("0.012"), ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("0.12"), ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("6.6"), ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("11.88"), ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("11.988"), ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));

		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(0), ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("0"), ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("6"), ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("11"), ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("11"), ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
		assertBigdecimal(new BigDecimal("12"), ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));
	}

	
	@Test
	public void test3(){

		RatingMethod ratingScoreMethod = new RatingMethod();
		ratingScoreMethod.setMinValue(new BigDecimal(100));
		ratingScoreMethod.setMaxValue(new BigDecimal(200));
		ratingScoreMethod.setInitScore(new BigDecimal(1));
		ratingScoreMethod.setTotalScore(new BigDecimal(12));
		ratingScoreMethod.setCompareOrder(RatingMethod.COMPARE_ORDER_DESC);

		
		
		RatingCalc ratingScoreCalc = new RatingCalc();

		System.out.println("");
		System.out.println("-----------test3--------------");
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
		System.out.println(ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(12), ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(12), ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(12), ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(11.989), ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(11.89), ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(5.95), ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(1.11), ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(1.011), ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(1), ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(0), ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
//		assertBigdecimal(new BigDecimal(0), ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));

		assertBigdecimal(new BigDecimal(12), ratingScoreCalc.calcScore(new BigDecimal(99),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(12), ratingScoreCalc.calcScore(new BigDecimal(99.9),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(12), ratingScoreCalc.calcScore(new BigDecimal(100),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(11), ratingScoreCalc.calcScore(new BigDecimal(100.1),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(11), ratingScoreCalc.calcScore(new BigDecimal(101),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(5), ratingScoreCalc.calcScore(new BigDecimal(155),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(1), ratingScoreCalc.calcScore(new BigDecimal(199),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(1), ratingScoreCalc.calcScore(new BigDecimal(199.9),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(1), ratingScoreCalc.calcScore(new BigDecimal(200),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(0), ratingScoreCalc.calcScore(new BigDecimal(200.1),ratingScoreMethod));
		assertBigdecimal(new BigDecimal(0), ratingScoreCalc.calcScore(new BigDecimal(201),ratingScoreMethod));
	}
	
	private void assertBigdecimal(BigDecimal bigDecimal1, BigDecimal bigDecimal2) {
		if (bigDecimal1 != null && bigDecimal1 != null) {
			bigDecimal1 = bigDecimal1.setScale(5, BigDecimal.ROUND_HALF_UP);
			bigDecimal2 = bigDecimal2.setScale(5, BigDecimal.ROUND_HALF_UP);
		}

		Assert.assertEquals(bigDecimal1, bigDecimal2);
	}
}
