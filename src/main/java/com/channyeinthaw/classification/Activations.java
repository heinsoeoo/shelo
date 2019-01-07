package com.channyeinthaw.classification;

public class Activations {
    public enum ActivationType {
        Sigmoid,
        Hyperbolic
    }

    /**
     * Activation function (sigmoid - adjust any number between 0 to 1)
     * @param number any number
     * @return sigmoid value of number
     */
    private static double sigmoid(double number) {
        return 1 / (1 + Math.exp(-(number)));
    }

    /**
     * Derivative or sigmoid function. The rate of change of output with respect to net
     * @param number any number
     * @return sigmoid derivative value of number
     */
    private static double sigmoidDerivative(double number) {
        return number * (1 - number);
    }

    /**
     * Activation function (hyperbolic tanh - adjust any number between -1 to 1)
     * @param number any number
     * @return sigmoid value of number
     */
    private static double hyperbolic(double number) {
        return Math.tanh(number);
    }

    /**
     * Derivative or hyperbolic tanh function. The rate of change of output with respect to net
     * @param number any number
     * @return sigmoid derivative value of number
     */
    private static double hyperbolicDerivative(double number) {
        return 1 - Math.pow(number, 2);
    }

    public static double activate(double number, ActivationType type) {
        switch(type) {
            case Sigmoid:
                return sigmoid(number);
            case Hyperbolic:
                return hyperbolic(number);
            default:
                return 0;
        }
    }

    public static double derivative(double number, ActivationType type) {
        switch(type) {
            case Sigmoid:
                return sigmoidDerivative(number);
            case Hyperbolic:
                return hyperbolicDerivative(number);
            default:
                return 0;
        }
    }
}
