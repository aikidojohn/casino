package com.johnhite.casino.nn;

import java.util.List;

import com.google.common.collect.Lists;

public class Neuron {
	private List<Double> weights = Lists.newArrayList();
	
	public Neuron(int numberInputs) {
		for (int i=0; i <= numberInputs; i++) {
			weights.add(MathUtil.randomGaussian());
		}
	}
	
	public int getNumberWeights() {
		return weights.size();
	}
	
	public List<Double> getWeights() {
		return weights;
	}
	
	public void setWeights(List<Double> weights) {
		this.weights = Lists.newArrayList(weights);
	}
	
	public double update(List<Double> inputs) {
		double output = 0;
		for (int i=0; i < weights.size() - 1; i++) {
			output += weights.get(i) * inputs.get(i);
		}
		//add in the bias (weights[len-1] * -1)
		output -= weights.get(weights.size() -1);
		return MathUtil.sigmoid(output);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Neuron [weights=").append(weights).append("]\n");
		return builder.toString();
	}
}
