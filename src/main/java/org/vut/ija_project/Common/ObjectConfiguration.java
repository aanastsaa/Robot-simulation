package org.vut.ija_project.Common;


import org.vut.ija_project.DataLayer.Robot.RobotColor;

/**
 * Data transfer object for transfer of environment object state
 */
public class ObjectConfiguration {
    public double newX;
    public double newY;
    public int newAngle;
    public double newVelocity;
    public int newRotationAngle;
    public RobotColor newColor;

    public ObjectConfiguration(double newX, double newY) {
        this.newX = newX;
        this.newY = newY;
    }

    public ObjectConfiguration(double newX, double newY,
                               int newAngle, double newVelocity,
                               int newRotationAngle, RobotColor newColor) {
        this.newX = newX;
        this.newY = newY;
        this.newAngle = newAngle;
        this.newVelocity = newVelocity;
        this.newRotationAngle = newRotationAngle;
        this.newColor = newColor;
    }
}
