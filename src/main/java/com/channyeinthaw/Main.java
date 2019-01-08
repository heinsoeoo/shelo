package com.channyeinthaw;

import com.channyeinthaw.classification.psonn.NNSwarmBuilder;
import com.channyeinthaw.classification.psonn.Network;
import com.channyeinthaw.classification.psonn.TrainingSample;
import com.channyeinthaw.optimization.pso.Swarm;
import com.channyeinthaw.util.Log;
import com.channyeinthaw.util.Logger;

import java.io.*;
import java.util.Arrays;

public class Main {
    private static void displayHelp() {
        System.out.println(
                "<<CONFIG FILE FORMAT>>\n" +
                "DS PATH\n" +
                "MODEL SAVE PATH (DIR)\n" +
                "TypeNew|TypeResume\n" +
                "PSO SAVE PATH (DIR)\n" +
                "PSO LOAD PATH (DIR) <NULLABLE>\n" +
                "NETWORK SCHEMA)\n" +
                "SWARM_SIZE, C1, C2, W0, WLB\n" +
                "ERROR LIMIT"
        );
    }

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            displayHelp();
            return;
        };

        Log.setLogger(new Logger(Logger.Type.Console, "SHELO"));

        String filePath = args[0];
        File file = new File(filePath);

        if (!file.exists()) {
            Log.error("CONFIG_FILE_NOT_FOUND");
            displayHelp();

            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));

        try {
            File dataSet = new File(reader.readLine());
            File networkDir = new File(reader.readLine());
            String type = reader.readLine();
            String saveDir = reader.readLine();
            String loadDir = reader.readLine();
            String[] networkSchema = reader.readLine().split(",");
            String[] psoConstants = reader.readLine().split(",");
            double errorLimit = Double.parseDouble(reader.readLine());

            if (!dataSet.exists()) {
                Log.error("DATASET_FILE_NOT_FOUND");

                return;
            }

            switch(type) {
                case "TypeNew": typeNew(
                        dataSet.getAbsolutePath(),
                        networkDir.getAbsolutePath(),
                        saveDir, networkSchema,
                        psoConstants, errorLimit
                ); break;
                case "TypeResume": typeResume(
                        dataSet.getAbsolutePath(),
                        networkDir.getAbsolutePath(),
                        saveDir, loadDir, networkSchema,
                        psoConstants, errorLimit
                );break;
            }

        } catch (IOException e) {
            Log.error("INVALID_CONFIG_FILE");
        }
    }

    private static TrainingSample[] loadDataset(String path, int ols, int li) {
        TrainingSample[] dataset;

        try {
            dataset = TrainingSample.load(path, ols, li);
        } catch (Exception e) {
            Log.error("INVALID_DATASET");

            return null;
        }

        return dataset;
    }

    private static void typeResume(String dataSetPath, String networkSaveDir, String saveDir, String loadDir, String[] _networkSchema, String[] _psoConstants, double errorLimit) {
        int[] networkSchema = new int[_networkSchema.length]; int i = 0;
        for(String s: _networkSchema) networkSchema[i++] = Integer.parseInt(s);

        double[] psoConstants = new double[_psoConstants.length]; i = 0;
        for(String s: _psoConstants) psoConstants[i++] = Double.parseDouble(s);

        TrainingSample[] dataset = loadDataset(dataSetPath, networkSchema[networkSchema.length - 1], networkSchema[0] + 1);
        if (dataset == null) return;

        double w0 = psoConstants[3], wlb = psoConstants[4];

        Swarm swarm;
        try {
            swarm = new Swarm(loadDir, p -> {
                Network n = Network.build(networkSchema);
                n.updateWeightsAndBias(p);
                return n.computeMSE(dataset);
            });
        } catch (Exception e) {
            Log.error("INVALID_PSO_FILE");

            return;
        }

        swarm.setSaveDir(saveDir);
        swarm.run(errorLimit, w0, wlb);

        i = 1;
        for(Network n: NNSwarmBuilder.getBestNetworks(swarm, () -> Network.build(networkSchema))) {
            try {
                n.save(networkSaveDir + "/network_" + i + ".model");
                i++;
            } catch (Exception e) {}
        }

        Log.info("MODELS_SAVED");
    }

    private static void typeNew(String dataSetPath, String networkSaveDir, String saveDir, String[] _networkSchema, String[] _psoConstants, double errorLimit) {
        int[] networkSchema = new int[_networkSchema.length]; int i = 0;
        for(String s: _networkSchema) networkSchema[i++] = Integer.parseInt(s);

        double[] psoConstants = new double[_psoConstants.length]; i = 0;
        for(String s: _psoConstants) psoConstants[i++] = Double.parseDouble(s);

        TrainingSample[] dataset = loadDataset(dataSetPath, networkSchema[networkSchema.length - 1], networkSchema[0]);
        if (dataset == null) return;

        int swarmSize = (int) psoConstants[0];
        double c1 = psoConstants[1], c2 = psoConstants[2];
        double w0 = psoConstants[3], wlb = psoConstants[4];

        NNSwarmBuilder b = new NNSwarmBuilder(swarmSize);
        b.setNetworkSchema(() -> Network.build(networkSchema));
        b.setObjectiveFunction(p -> {
            Network n = Network.build(2,1);
            n.updateWeightsAndBias(p);
            return n.computeMSE(dataset);
        });

        Swarm swarm = b.build(c1, c2);
        swarm.setSaveDir(saveDir);
        swarm.run(errorLimit, w0, wlb);

        i = 1;
        for(Network n: b.getBestNetworks()) {
            try {
                n.save(networkSaveDir + "/network_" + i + ".model");
                i++;
            } catch (Exception e) {}
        }

        Log.info("MODELS_SAVED");
    }
}
