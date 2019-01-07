package com.channyeinthaw.util;

public class Utility {
    /**
     * Get random value between min and max range
     * @param min - minimum random number
     * @param max - maximum random number
     * @return random double number
     */
    private static double random(int min, int max) {
        return (min + Math.random() * (max - min + 1));
    }

    /**
     * Get empty m x n matrix
     * @param row - m rows
     * @param col - n columns
     * @return empty m x n double matrix
     */
    public static double[][] matrix(int row, int col) {
        return matrix(row, col, false, 0,0);
    }

    /**
     * Get randomized m x n matrix between min and max range
     * @param row - m rows
     * @param col - n columns
     * @param min - minimum random number
     * @param max - maximum random number
     * @return randomized m x n double matrix
     */
    public static double[][] matrix(int row, int col, int min, int max) {
        return matrix(row, col, true, min, max);
    }

    /**
     * Get m x n matrix with or without random value between min and max range
     * @param row - m rows
     * @param col - n columns
     * @param randomize - boolean if true will return randomize matrix otherwise empty, zero initialized
     * @param min - minimum random number
     * @param max - maximum random number
     * @return m x n double matrix randomized or empty
     */
    private static double[][] matrix(int row, int col, boolean randomize, int min, int max) {
        double[][] m = new double[row][col];

        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++)
                if (randomize)
                    m[i][j] = random(min, max);
                else
                    m[i][j] = 0;

        return m;
    }

    /**
     * Get empty vector with n length
     * @param length - length of vector
     * @return empty double vector
     */
    public static double[] vector(int length) {
        return vector(length, false, 0, 0);
    }

    /**
     * Get randomized vector with n length between min and max range
     * @param length - length of vector
     * @param min - minimum random number
     * @param max - maximum random number
     * @return randomized double vector
     */
    public static double[] vector(int length, int min, int max) {
        return vector(length, true, min, max);
    }

    /**
     * Get empty or randomized vector with n length between min and max range
     * @param length - length of vector
     * @param randomize - boolean if true will return randomized vecor otherwise empty, zero initialized
     * @param min - minimum random number
     * @param max - maximum random number
     * @return double vector
     */
    private static double[] vector(int length, boolean randomize, int min, int max) {
        double[] v = new double[length];

        for(int i = 0; i < length; i++)
            if (randomize)
                v[i] = random(min, max);
            else
                v[i] = 0;

        return v;
    }

    /**
     * Clones the provided array
     *
     * @param src
     * @return a new clone of the provided array
     */
    public static double[][] cloneNeurons(double[][] src) {
        int length = src.length;
        double[][] target = new double[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }
}
