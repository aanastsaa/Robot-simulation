package org.vut.ija_project.DataLayer.Common;

public class DoubleUtils {
    private static final double EPSILON = 0.00001; // Tolerance level

    public static boolean nearlyEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }
}
