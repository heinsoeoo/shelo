package com.channyeinthaw.optimization.pso;

import com.channyeinthaw.util.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Swarm {
    private Particle[] swarm;
    private double[] gBestPositions;

    private ObjectiveFunction f;
    private File saveDir;

    public Swarm(int swarmSize) {
        swarm = new Particle[swarmSize];

        Log.setTag("PSO/SWARM");
    }

    public Swarm(String path, ObjectiveFunction f) throws IOException {
        this.f = f;
        BufferedReader r = new BufferedReader(new FileReader(new File(path)));

        String[] gbests = r.readLine().split(",");
        gBestPositions = new double[gbests.length];

        int i = 0;
        for(String p: gbests) {
            gBestPositions[i++] = Double.parseDouble(p);
        }

        List<Particle> particles = new ArrayList<>();
        String line;
        while((line = r.readLine()) != null) {
            Particle p = new Particle(line);
            p.setObjectFunction(f);
            p.setSwarm(this);
            particles.add(p);
        }

        swarm = particles.toArray(new Particle[]{});

        Log.setTag("PSO/SWARM");
    }

    public String toString() {
        StringBuilder gBests = new StringBuilder();

        for(double p: gBestPositions) {
            gBests.append(p);
            gBests.append(",");
        }

        String gBestPositions = gBests.substring(0, gBests.length() - 1) + "\n";
        StringBuilder _swarm = new StringBuilder();

        for(Particle p: swarm) {
            _swarm.append(p.toString());
            _swarm.append("\n");
        }

        return gBestPositions + _swarm.substring(0, _swarm.length() - 1);
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
        int i = 1, j = 0;
        do {
            Log.info("Iteration " + i ++);
            j++;
        } while (run(w0, wlb, j) > mseLimit);
    }

    public void run(int iteration, double w0, double wlb) {
        for(int i = 0; i < iteration; i++) {
            Log.info("Iteration " + i);
            run(w0, wlb, i);
        }
    }

    private double run(double w0, double wlb, int iter) {
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

        try {
            if (saveDir != null) {
                if (saveDir.exists() && saveDir.isDirectory()) {
                    Log.info("Saving ...");

                    File saveFile = new File(saveDir.getAbsolutePath() + "/iter_" + iter + ".pso");

                    if (saveFile.createNewFile()) {
                        PrintWriter pw = new PrintWriter(new FileWriter(saveFile));
                        pw.write(toString());

                        pw.close();
                    }
                }
            }

        } catch (IOException e) { }
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

    public void setSaveDir(String sDir) {
        saveDir = new File(sDir);
    }
}
