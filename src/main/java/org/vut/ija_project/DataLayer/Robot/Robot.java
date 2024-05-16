package org.vut.ija_project.DataLayer.Robot;

import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Environment.Environment;

public interface Robot {

    /*
     * @return Current angle of robot.
     */
    public int angle();

    /*
     * Checks if robot can change position by
     * 1 field in front of robot's view.
     */
    public boolean canMove();

    /*
     * @return Current robot's position.
     */
    public Position getPosition();

    /*
     * Updates current robot's position.
     */
    public void updatePosition();
    public Robot copy(Environment env);
    public double getVelocity();

    public int getRotationAngle();
    public void setConfiguration(ObjectConfiguration configuration);
    public double getRobotSize();
    public RobotType getType();
    public RobotColor getColor();
}
