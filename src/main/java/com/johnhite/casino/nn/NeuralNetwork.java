package com.johnhite.casino.nn;

import java.util.List;

import com.google.common.collect.Lists;

public class NeuralNetwork {
	private List<NeuronLayer> layers = Lists.newArrayList();
	private final int numberInputs;
	private final int numberOutputs;
	private final int numberHiddenLayers;
	private final int numberNeuronsPerHiddenLayer;
	
	public NeuralNetwork(int nInputs, int nOutputs, int nHiddenLayers, int nNeuronsPerHiddenLayer) {
		this.numberInputs = nInputs;
		this.numberOutputs = nOutputs;
		this.numberHiddenLayers = nHiddenLayers;
		this.numberNeuronsPerHiddenLayer = nNeuronsPerHiddenLayer;
		//add the input layers
		for (int i=0; i < nHiddenLayers; i++) {
			layers.add(new NeuronLayer(nNeuronsPerHiddenLayer, nInputs));
		}
		
		//Add the output layer;
		layers.add(new NeuronLayer(nOutputs, nInputs));
	}
	
	public List<Double> update(List<Double> inputs) {
		List<Double> outputs = inputs;
		for (NeuronLayer layer : layers) {
			outputs = layer.update(outputs);
		}
		return outputs;
	}
	
	public List<Double> getWeights() {
		List<Double> weights = Lists.newArrayList();
		for (NeuronLayer layer: layers) {
			weights.addAll(layer.getWeights());
		}
		return weights;
	}
	
	public void setWeights(List<Double> weights) {
		int lastIndex = 0;
		for (NeuronLayer layer : layers) {
			layer.setWeights(weights.subList(lastIndex, lastIndex + layer.getNumberWeights()));
			lastIndex += layer.getNumberWeights();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NeuralNetwork [layers=").append(layers)
				.append(", numberInputs=").append(numberInputs)
				.append(", numberOutputs=").append(numberOutputs)
				.append(", numberHiddenLayers=").append(numberHiddenLayers)
				.append(", numberNeuronsPerHiddenLayer=")
				.append(numberNeuronsPerHiddenLayer).append("]");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		NeuralNetwork brain = new NeuralNetwork(3, 2, 1, 4);
		System.out.println(brain.toString());
		
		List<Double> weights = brain.getWeights();
		System.out.println( "\n\n"+ weights.size() + " : " + weights.toString() + "\n\n");
		
		List<Double> newWeights = Lists.newArrayList();
		for (int i= 0; i < weights.size(); i++) {
			newWeights.add(0.1);
		}
		
		brain.setWeights(newWeights);
		System.out.println(brain.toString());
	}
	
}
