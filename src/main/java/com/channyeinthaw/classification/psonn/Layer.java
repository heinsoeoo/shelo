package com.channyeinthaw.classification.psonn;

import com.channyeinthaw.classification.Activations;
import com.channyeinthaw.util.Utility;

/**
 * A neural network layer contains bias for each neurons in 1 dimensional array (m vector), neurons with synapses (weights)
 * in 2 dimensional array (m x n matrix). Each row as a neuron with column synapses (weights)
 * Example: 3 x 2 matrix = 3 neurons with 2 synapses foreach. 3 bias one for each neurons.
 *
 * If you want crystal clear step by step example go to the link below
 * https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
 */
public class Layer {
    private double[] outputs, bias;
    private double[][] neurons;

    Layer() {}
    Layer(String layer) {
        String[] data = layer.split(" ");
        String[] meta = data[0].split(",");
        String[] weights = data[1].split(",");

        int b = Integer.parseInt(meta[0]);
        int r = Integer.parseInt(meta[1]), c = Integer.parseInt(meta[2]);

        double[] bias = new double[b];
        double[][] neurons = new double[r][c];

        int w = 0;
        for(int i = 0; i < b; i++) {
            bias[i] = Double.parseDouble(weights[w++]);
        }

        for(int ro = 0; ro < r; ro++) {
            for(int co = 0; co < c; co++) {
                neurons[ro][co] = Double.parseDouble(weights[w++]);
            }
        }

        setup(neurons, bias);
    }

    /**
     * Manually setup layer
     * @param neurons m x n matrix
     * @param bias m length vector
     */
    private void setup(double[][] neurons, double[] bias) {
        this.neurons = neurons;
        this.bias = bias;
        outputs = new double[neurons.length];
    }

    /**
     * Randomize setup layer
     * @param neurons number of neurons (m in m x n matrix)
     * @param synapses number of synapses for each neuron (n in m x n matrix)
     * @param wmin Weight min
     * @param wmax Weight max
     * @param bmin Bias min
     * @param bmax Bias max
     */
    void setup(int neurons, int synapses, int wmin, int wmax, int bmin, int bmax) {
        setup(
                Utility.matrix(neurons, synapses, wmin, wmax),
                Utility.vector(neurons, bmin, bmax)
        );
    }

    /**
     * Compute current layer outputs for given inputs
     * @param inputs 1 dimensional array
     * @return 1 dimensional array
     */
    double[] compute(double[] inputs, Activations.ActivationType type) {
        double[] outputs = new double[this.outputs.length];

        int n = 0;
        for(double[] synapses: neurons) {
            double net = 0;

            /*
             * net computation for single neuron
             * (a0 * w0 + a1 * w1 + ... + an * wn)
             * where a0, a1, ... , an are inputs
             * w0, w1, ... , wn are synapses (weights)
             */
            int s = 0;
            for(double weight: synapses) {
                net += weight * inputs[s++];
            }

            net += bias[n];

            /*
             * activation for single neuron
             */
            double activatedValue = Activations.activate(net, type);
            this.outputs[n] = activatedValue;
            outputs[n++] = activatedValue;
        }

        return outputs;
    }

    int getSubPositionVectorSize() {
        return (neurons.length * neurons[0].length) + bias.length;
    }

    double[] getSubPositionVector() {
        double[] subPositions = new double[getSubPositionVectorSize()];

        int i = 0;
        for(; i < bias.length; i++)
            subPositions[i] = bias[i];

        for(double[] weights: neurons) {
            for(double weight: weights) {
                subPositions[i++] = weight;
            }
        }

        return subPositions;
    }

    void updateWeightsAndBias(double[] subPositionVector) {
        int i = 0;
        for(; i < bias.length; i++)
            bias[i] = subPositionVector[i];

        for(int j = 0; j < neurons.length; j++)
            for(int k = 0; k < neurons[0].length; k++)
                neurons[j][k] = subPositionVector[i++];
    }

    /**
     * etotal = summation(1/2 x (error ^ 2))
     * where error is desiredOutput - output (The rate of change or error total with respect to output)
     */
    double calculateTotalError(double[] targets) {
        double error = 0;
        int i = 0;
        for(double target: targets) {
            double loss = Math.abs(target - outputs[i++]);
            error += 0.5 * Math.pow(loss, 2);
        }

        return error;
    }

    public String toString() {
        StringBuilder layer = new StringBuilder();
        layer.append(String.format("%s,%s,%s ", bias.length, neurons.length, neurons[0].length));

        for(double bias: this.bias) {
            layer.append(bias);
            layer.append(",");
        }

        for(double[] neuron: neurons) {
            for(double weight: neuron) {
                layer.append(weight);
                layer.append(",");
            }
        }

        return layer.substring(0, layer.length() - 1);
    }
}
