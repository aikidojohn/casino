package com.johnhite.casino.nn;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class NeuronLayer implements Iterable<Neuron> {
	private List<Neuron> neurons = Lists.newArrayList();
	private final int numberInputs;
	private final int numberWeights;
	
	public NeuronLayer(int nNeurons, int nInputs) {
		int nWeights = 0;
		for (int i= 0; i < nNeurons; i++) {
			Neuron n = new Neuron(nInputs);
			nWeights += n.getNumberWeights();
			neurons.add(n);
		}
		this.numberInputs = nInputs;
		this.numberWeights = nWeights;
	}

	public List<Double> update(List<Double> inputs) {
		List<Double> outputs = Lists.newArrayList();
		for (Neuron n : neurons) {
			outputs.add(n.update(inputs));
		}
		return outputs;
	}
	
	public List<Double> getWeights() {
		List<Double> weights = Lists.newArrayList();
		for (Neuron n : neurons) {
			weights.addAll(n.getWeights());
		}
		return weights;
	}
	
	public void setWeights(List<Double> weights) {
		for (int i = 0; i < neurons.size(); i++) {
			Neuron n = neurons.get(i);
			int index = i * n.getNumberWeights();
			neurons.get(i).setWeights(weights.subList(index, index + n.getNumberWeights()));
		}
	}
	
	public int getNumberWeights() {
		return numberWeights;
	}
	
	@Override
	public Iterator<Neuron> iterator() {
		return neurons.iterator();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NeuronLayer [neurons=").append(neurons)
				.append(", numberInputs=").append(numberInputs)
				.append(", numberWeights=").append(numberWeights).append("]\n");
		return builder.toString();
	}
}
