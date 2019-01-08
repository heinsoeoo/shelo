package com.channyeinthaw.optimization.pso;

public class Particle {
    double[] positions;
    double[] pBestPositions;

    private double[] velocities;
    private double c1, c2, inertia;

    private Swarm swarm;

    private ObjectiveFunction f;

    Particle(double[] l, double a1, double a2) {
        positions = l;
        pBestPositions = l;
        velocities = new double[l.length];
        c1 = a1;
        c2 = a2;
    }

    Particle(String particle) {
        String[] parts = particle.split(" ");
        String[] c12Inertia = parts[0].split(",");
        String[] poss = parts[1].split(",");
        String[] pBest = parts[2].split(",");
        String[] velos = parts[3].split(",");

        positions = new double[poss.length];
        pBestPositions = new double[pBest.length];
        velocities = new double[velos.length];

        for(int i = 0; i < poss.length; i++) {
            positions[i] = Double.parseDouble(poss[i]);
            pBestPositions[i] = Double.parseDouble(pBest[i]);
            velocities[i] = Double.parseDouble(velos[i]);
        }

        c1 = Double.parseDouble(c12Inertia[0]);
        c2 = Double.parseDouble(c12Inertia[1]);
        inertia = Double.parseDouble(c12Inertia[2]);
    }

    public String toString() {
        StringBuilder poss = new StringBuilder();
        StringBuilder pBest = new StringBuilder();
        StringBuilder velos = new StringBuilder();

        String c12Inertia = String.format("%f,%f,%f", c1, c2, inertia);

        int i = 0;
        for(double p: positions) {
            poss.append(p);
            poss.append(",");

            pBest.append(pBestPositions[i]);
            pBest.append(",");

            velos.append(velocities[i]);
            velos.append(",");

            i++;
        }

        String positions = poss.substring(0, poss.length() - 1);
        String pBestPositions = pBest.substring(0, pBest.length() - 1);
        String velocities = velos.substring(0, velos.length() - 1);

        return String.format("%s %s %s %s", c12Inertia, positions, velocities, pBestPositions);
    }

    public void updatePersonalBest() {
        double currentResult = f.calculate(positions);
        double pBestResult = calculate();

        if (currentResult < pBestResult)
            pBestPositions = positions;
    }

    public void updatePositions() {
        for(int i = 0; i < positions.length; i++)
            updatePositionAt(i);
    }

    public void updateVelocities() {
        for(int i = 0; i < positions.length; i++)
            updateVelocityAt(i);
    }

    private void updateVelocityAt(int index) {
        double gBestPosition = swarm.getGBestPositionAt(index);
        double oldVelocity = velocities[index];
        double oldPosition = positions[index];
        double r1 = Math.random();
        double r2 = Math.random();
        double pBestPosition = pBestPositions[index];

        double newVelocity = inertia * oldVelocity +
                (c1 * r1 * (pBestPosition - oldPosition)) +
                (c2 * r2 * (gBestPosition - oldPosition));

        velocities[index] = newVelocity;
    }

    private void updatePositionAt(int index) {
        positions[index] = positions[index] + velocities[index];
    }

    public double calculate() {
        return f.calculate(pBestPositions);
    }

    void setObjectFunction(ObjectiveFunction of) {
        f = of;
    }

    void setSwarm(Swarm s) {
        swarm = s;
    }

    void setInertia(double i) {
        inertia = i;
    }

    public double[] getPositions() {
        return pBestPositions;
    }
}
