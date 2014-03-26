package com.johnhite.casino.nn;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.collect.Lists;

public class MathUtilTest {

	@Test
	public void testStd() {
		List<Double> values = Lists.newArrayList(2.0,4.0,4.0,4.0,5.0,5.0,7.0,9.0);
		assertEquals(5, MathUtil.average(values), .0000001);
		assertEquals(2, MathUtil.std(values), .0000001);
	}
	
	@Test
	public void testStdNegative() {
		List<Double> values = Lists.newArrayList(-2.0,-4.0,-4.0,-4.0,-5.0,-5.0,-7.0,-9.0);
		assertEquals(-5, MathUtil.average(values), .0000001);
		assertEquals(2, MathUtil.std(values), .0000001);
	}
}
