import com.channyeinthaw.classification.psonn.Network;
import com.channyeinthaw.classification.psonn.TrainingSample;
import com.channyeinthaw.optimization.pso.Particle;
import com.channyeinthaw.optimization.pso.Swarm;
import com.channyeinthaw.util.Log;
import com.channyeinthaw.util.Logger;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TestNetwork {
    public static void main(String[] args) {
        Log.setLogger(new Logger(Logger.Type.Console, "PSOTEST"));

//        TrainingSample.load("C:\test.txt", 1, 2);

        TrainingSample[] samples = new TrainingSample[]{
                new TrainingSample("0,1,0", 1, 2),
                new TrainingSample("0,1,0", 1, 2),
                new TrainingSample("1,1,1", 1, 2),
                new TrainingSample("1,0,0", 1, 2),
        };

        double[][] initialSwarmData = new double[30][];
        for(int i = 0; i < initialSwarmData.length; i++) {
            initialSwarmData[i] = Network.build(2,1).getPositionVector();
        }

        Swarm swarm = new Swarm(30);
        swarm.setObjectiveFunction(positions -> {
            Network n = Network.build(2,1);
            n.updateWeightsAndBias(positions);
            return n.computeMSE(samples);
        });
        swarm.initializeSwarm(initialSwarmData, 0.30, 0.4);

        swarm.run(0.00000001, 0.9, 0.303);

        for(Particle p: swarm.getSwarm()) {
            System.out.println(p.calculate());
            Network n = Network.build(2,1);
            n.updateWeightsAndBias(p.getPositions());

            for(TrainingSample s: samples)
                System.out.println(Arrays.toString(n.compute(s.getInputs())));

            Log.divider();
        }
    }
}
