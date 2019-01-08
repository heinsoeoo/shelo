import com.channyeinthaw.classification.psonn.NNSwarmBuilder;
import com.channyeinthaw.classification.psonn.Network;
import com.channyeinthaw.classification.psonn.TrainingSample;
import com.channyeinthaw.optimization.pso.Particle;
import com.channyeinthaw.optimization.pso.Swarm;
import com.channyeinthaw.util.Log;
import java.util.Arrays;

public class TestNetwork {
    public static void main(String[] args) {
        TrainingSample[] samples = new TrainingSample[]{
                new TrainingSample("0,1,0", 1, 2),
                new TrainingSample("0,1,0", 1, 2),
                new TrainingSample("1,1,1", 1, 2),
                new TrainingSample("1,0,0", 1, 2),
        };

        NNSwarmBuilder b = new NNSwarmBuilder(30);
        b.setNetworkSchema(() -> Network.build(2,1));
        b.setObjectiveFunction(p -> {
            Network n = Network.build(2,1);
            n.updateWeightsAndBias(p);
            return n.computeMSE(samples);
        });

        Swarm swarm = b.build(0.4, 0.1);
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
