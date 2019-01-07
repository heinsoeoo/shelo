package com.channyeinthaw.classification.psonn;

import com.channyeinthaw.classification.Activations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Neural network builder class for building ready made networks, saving and loading saved networks
 */
public class Network {
    private Activations.ActivationType type;
    private Layer[] layers;

    public Network(Layer[] layers) {
        this(layers, Activations.ActivationType.Sigmoid);
    }

    public Network(Layer[] layers, Activations.ActivationType type) {
        this.layers = layers;
        this.type = type;
    }

    public double[] getPositionVector() {
        int size = 0;
        for(Layer l: layers)
            size += l.getSubPositionVectorSize();

        int i = 0;
        double[] positionVector = new double[size];

        for(Layer l: layers) {
            for(double position: l.getSubPositionVector())
                positionVector[i++] = position;
        }

        return positionVector;
    }

    public void updateWeightsAndBias(double[] positionVector) {
        int i = 0;
        for(Layer l: layers) {
            double[] subPositionVector = new double[l.getSubPositionVectorSize()];
            for(int j = 0; j < subPositionVector.length; j++)
                subPositionVector[j] = positionVector[i++];

            l.updateWeightsAndBias(subPositionVector);
        }
    }

    /**
     * Feed forward computation
     * @param inputs - 1 dimensional array of inputs
     * @return output
     */
    public double[] compute(double[] inputs) {
        for(Layer l: layers) {
            /*
             * feeding output of layer to next layer
             */
            inputs = l.compute(inputs, type);
        }

        /*
         * final output
         */
        return inputs;
    }

    public double computeMSE(TrainingSample[] samples) {
        double error = 0;

        for(TrainingSample s: samples) {
            double[] inputs = s.getInputs();
            double[] desiredOutputs = s.getOutputs();
            double[] outputs = compute(inputs);

            error += Math.pow(layers[layers.length - 1].calculateTotalError(desiredOutputs), 2);
        }

        return error/samples.length;
    }

    /**
     * Save network data weights and bias to file
     * @param path - file path
     * @throws IOException io
     */
    public void save(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();

        StringBuilder network = new StringBuilder();
        network.append(type.name());
        network.append("\n");

        for(Layer layer: layers) {
            network.append(layer);
            network.append("\n");
        }

        BufferedWriter buf = new BufferedWriter(new FileWriter(file));
        buf.write(network.substring(0, network.length() - 1));

        buf.close();
    }

    /**
     * Load network data weights and bias from file
     * @param path - file path
     * @throws IOException io
     */
    public static Network load(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) return null;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<Layer> layers = new ArrayList<>();
        String activationType = "";

        boolean metaNetworkRead = false;
        while((line = reader.readLine()) != null) {
            if (!metaNetworkRead) {
                metaNetworkRead = true;

                activationType = line;
            } else {
                layers.add(new Layer(line));
            }
        }

        return new Network(layers.toArray(new Layer[]{}), Activations.ActivationType.valueOf(activationType));
    }

    /**
     * Build network with default weight and bias range
     * @param neurons - neurons, eg: 2,2,2 for 2 neuron for first layer (input), 2 for second layer (hidden) and 2 for output layer
     * @return Randomly configured network
     */
    public static Network build(int ...neurons) {
        return build(Activations.ActivationType.Sigmoid, neurons);
    }

    /**
     * Build network with default weight and bias range
     * @param type - activation type
     * @param neurons - neurons, eg: 2,2,2 for 2 neuron for first layer (input), 2 for second layer (hidden) and 2 for output layer
     * @return Randomly configured network
     */
    public static Network build(Activations.ActivationType type, int ...neurons) {
        return build(type, 0, 1, -2, -2, neurons);
    }

    /**
     * Build network with weight range and bias range
     * @param type - activation type
     * @param neurons - neurons, eg: 2,2,2 for 2 neuron for first layer (input), 2 for second layer (hidden) and 2 for output layer
     * @return Randomly configured network
     */
    public static Network build(Activations.ActivationType type, int wmin, int wmax, int bmin, int bmax, int ...neurons) {
        ArrayList<Layer> layers = new ArrayList<>();
        for(int i = 1; i < neurons.length; i++) {
            Layer layer = new Layer();

            layer.setup(neurons[i], neurons[i-1], wmin, wmax, bmin, bmax);

            layers.add(layer);
        }

        return new Network(layers.toArray(new Layer[]{}), type);
    }
}
