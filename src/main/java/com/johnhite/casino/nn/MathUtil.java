package com.johnhite.casino.nn;

import java.util.Random;

public class MathUtil {
	private static Random random = new Random(System.currentTimeMillis());
	
	private MathUtil() {}
	
	public static double randomGaussian() {
		return random.nextGaussian();
	}
	
	public static double sigmoid(double a) {
		return 1.0 / (1 + Math.pow(Math.E, -1 * a));
	}
}
