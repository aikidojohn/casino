package com.johnhite.casino.nn;

import java.util.List;
import java.util.OptionalDouble;
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
	
	public static double std(List<Double> values) {
		double average = average(values);
		if (average == 0) {
			return 0;
		}
		
		OptionalDouble var = values.parallelStream().mapToDouble(Double::doubleValue).map(d -> Math.pow(d - average, 2)).average();
		if (var.isPresent()) {
			return Math.sqrt(var.getAsDouble());
		}
		return 0;
	}
	
	public static double average(List<Double> values) {
		OptionalDouble average = values.parallelStream().mapToDouble(Double::doubleValue).average();
		return average.orElse(0);
	}
} 
