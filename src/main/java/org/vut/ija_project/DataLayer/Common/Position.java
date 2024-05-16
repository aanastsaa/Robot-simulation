package org.vut.ija_project.DataLayer.Common;

/**
 * Class Represents Position in Environment
 */
public class Position {
    private final double y;
    private final double x;

    public Position(double y, double x) {
        this.y = y;
        this.x = x;
    }

    public double getY() { return y; }

    public double getX() {
        return x;
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", y, x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        Position other = (Position) o;

        return DoubleUtils.nearlyEqual(y, other.y) && DoubleUtils.nearlyEqual(x, other.x);
    }

    @Override
    public int hashCode() {
        //multiply column by 31 here, so we could differ
        //(2, 1) from (1, 2)
        return Double.hashCode(x) * 31 + Double.hashCode(x);
    }

}
