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
