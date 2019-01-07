package com.channyeinthaw.optimization.pso;

import com.channyeinthaw.util.Log;

public class Swarm {
    private Particle[] swarm;
    private double[] gBestPositions;

    private ObjectiveFunction f;

    public Swarm(int swarmSize) {
        swarm = new Particle[swarmSize];

        Log.setTag("PSO/SWARM");
    }

    public void initializeSwarm(double[][] initialPositions, double c1, double c2) {
        gBestPositions = new double[initialPositions[0].length];

        for(int i = 0; i < swarm.length; i++){
            Particle p = new Particle(initialPositions[i], c1, c2);
            p.setObjectFunction(f);
            p.setSwarm(this);

            swarm[i] = p;
        }
    }

    public void run(double mseLimit, double w0, double wlb) {
        int i = 1;
        do {
            Log.info("Iteration " + i ++);
        } while (run(w0, wlb) > mseLimit);
    }

    public void run(int iteration, double w0, double wlb) {
        for(int i = 0; i < iteration; i++) {
            Log.info("Iteration " + i);
            run(w0, wlb);
        }
    }

    private double run(double w0, double wlb) {
        Log.info("Updating pBest and gBest");

        for(Particle p: swarm) {
            p.updatePersonalBest();

            if (f.calculate(p.pBestPositions) < f.calculate(gBestPositions))
                gBestPositions = p.pBestPositions;
        }

        double sumMSE = 0;

        Log.info("Updating velocities and positions");
        for(Particle p: swarm) {
            double inertia = calcNewInertia(w0, wlb, calcRelateImprovement(p));

            p.setInertia(inertia);
            p.updateVelocities();
            p.updatePositions();

            sumMSE += p.calculate();
        }

        Log.divider();

        return sumMSE / swarm.length;
    }

    private double calcNewInertia(double w0, double wlb, double m) {
        return w0 + (wlb - w0) * ((Math.exp(m) - 1)/(Math.exp(m) + 1));
    }

    private double calcRelateImprovement(Particle p) {
        double gBestResult = f.calculate(gBestPositions);
        double result = f.calculate(p.positions);

        return (gBestResult - result) / (gBestResult + result);
    }

    public void setObjectiveFunction(ObjectiveFunction of) {
        f = of;
    }

    public Particle[] getSwarm() {
        return swarm;
    }

    double getGBestPositionAt(int index) {
        return gBestPositions[index];
    }
}
