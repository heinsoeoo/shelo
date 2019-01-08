package com.channyeinthaw.classification.psonn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrainingSample {
    private double[] inputs, outputs;

    public TrainingSample(double[] inputs, double[] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public TrainingSample(String inputAndOutput) {
        String[] ios = inputAndOutput.split(" ");
        String[] is = ios[0].split(",");
        String[] os = ios[1].split(",");

        inputs = new double[is.length];
        int i = 0;

        for(String in: is) inputs[i++] = Double.parseDouble(in);

        outputs = new double[os.length];
        i = 0;

        for(String ou: os) {
            outputs[i++] = Double.parseDouble(ou);
        }
    }

    public TrainingSample(String inputAndOutput, int outputLayerSize, int labelIndex) {
        String[] in = inputAndOutput.split(",");

        outputs = new double[outputLayerSize];
        inputs = new double[in.length];

        for(int i = 0; i < labelIndex; i++) {
            inputs[i] = Double.parseDouble(in[i]);
        }

        outputs[labelIndex - (outputLayerSize + 1)] = Double.parseDouble(in[labelIndex]);
    }

    public String toString() {
        StringBuilder isb = new StringBuilder(),
                osb = new StringBuilder();

        for(double input: inputs) {
            isb.append(input);
            isb.append(",");
        }

        for(double output: outputs) {
            osb.append(output);
            osb.append(",");
        }

        return String.format("%s %s", isb.substring(0, isb.length() - 1), osb.substring(0, osb.length() - 1));
    }

    public double[] getInputs() {
        return inputs;
    }

    public double[] getOutputs() {
        return outputs;
    }

    public static TrainingSample[] load(String file, int outputLayerSize, int labelIndex) throws IOException  {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        List<TrainingSample> samples = new ArrayList<>();

        while((line = reader.readLine()) != null) {
            if (outputLayerSize == -1)
                samples.add(new TrainingSample(line));
            else
                samples.add(new TrainingSample(line, outputLayerSize, labelIndex));
        }

        return samples.toArray(new TrainingSample[]{});
    }
}
