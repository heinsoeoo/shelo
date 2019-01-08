import com.channyeinthaw.classification.psonn.NNSwarmBuilder;
import com.channyeinthaw.classification.psonn.Network;
import com.channyeinthaw.classification.psonn.TrainingSample;
import com.channyeinthaw.optimization.pso.Swarm;
import com.channyeinthaw.util.Log;
import com.channyeinthaw.util.Logger;

import java.io.IOException;
import java.util.Arrays;

public class TestLoadNetwork {
    public static void main(String[] args) throws IOException {
        Log.setLogger(new Logger(Logger.Type.Console, "PSONN"));

        TrainingSample[] samples = new TrainingSample[]{
                new TrainingSample("0,1,0", 1, 2),
                new TrainingSample("0,1,0", 1, 2),
                new TrainingSample("1,1,1", 1, 2),
                new TrainingSample("1,0,0", 1, 2),
        };

        Swarm s = new Swarm("C:\\Users\\chany\\Desktop\\pso\\iter_20.pso", p -> {
            Network n = Network.build(2,1);
            n.updateWeightsAndBias(p);
            return n.computeMSE(samples);
        });

        s.setSaveDir("C:\\Users\\chany\\Desktop\\pso1");
        s.run(0.00000001, 0.9, 0.303);


        for(Network n: NNSwarmBuilder.getBestNetworks(s, () -> Network.build(2,1))) {
            for(TrainingSample ss: samples)
                System.out.println(Arrays.toString(n.compute(ss.getInputs())));

            Log.divider();
        }
    }
}
