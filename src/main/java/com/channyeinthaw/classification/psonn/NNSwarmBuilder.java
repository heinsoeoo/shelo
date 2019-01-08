package com.channyeinthaw.classification.psonn;

import com.channyeinthaw.optimization.pso.ObjectiveFunction;
import com.channyeinthaw.optimization.pso.Particle;
import com.channyeinthaw.optimization.pso.Swarm;
import com.channyeinthaw.util.Log;
import com.channyeinthaw.util.Logger;

import java.util.Arrays;

public class NNSwarmBuilder {
    private NNSchema schema;

    private int swarmSize;
    private ObjectiveFunction f;
    private Swarm swarm;

    public NNSwarmBuilder(int _swarmSize) {
        swarmSize = _swarmSize;

        Log.setLogger(new Logger(Logger.Type.Console, "PSONN"));
    }

    public void setObjectiveFunction(ObjectiveFunction ff) {
        f = ff;
    }

    public Swarm build(double c1, double c2) {
        Swarm s = new Swarm(swarmSize);
        s.setObjectiveFunction(f);
        s.initializeSwarm(getInitialSwarmData(), c1, c2);

        swarm = s;
        return s;
    }

    public Network[] getBestNetworks() {
        int i = 0;
        Network[] networks = new Network[swarmSize];
        for(Particle p: swarm.getSwarm()) {
            Network n = schema.get();
            n.updateWeightsAndBias(p.getPositions());
            networks[i++] = n;
        }

        return networks;
    }

    private double[][] getInitialSwarmData() {
        double[][] initialSwarmData = new double[swarmSize][];

        for(int i = 0; i < initialSwarmData.length; i++) {
            initialSwarmData[i] = schema.get().getPositionVector();
        }

        return initialSwarmData;
    }

    public void setNetworkSchema(NNSchema n) { schema = n; }

    public interface NNSchema { Network get();}
}
