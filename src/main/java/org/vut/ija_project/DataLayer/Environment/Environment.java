package org.vut.ija_project.DataLayer.Environment;

import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Obstacle.Obstacle;
import org.vut.ija_project.DataLayer.Robot.Robot;

import java.util.List;

public interface Environment {
    /*
     * Adds robot to its position
     * @return success of operation
     */
    public boolean addRobot(Robot robot);

    /*
     * Checks if position is inside Environment
     * @return success of operation
     */
    public boolean containsPosition(Position pos);

    /*
     * Creates obstacle at given position
     * @return success of operation
     */
    public Obstacle createObstacleAt(double y, double x);

    /*
     * Checks if there is obstacle at this position
     * @return success of operation
     */
    public boolean obstacleAt(int row, int col);
    /*
     * Checks if there is obstacle at this position
     * @return success of operation
     */
    public boolean obstacleAt(Position pos);
    /*
     * Checks if there is robot this at position
     * @return success of operation
     */
    public boolean robotAt(Position pos);
    public boolean robotAt(Position pos, Robot excludingRobot);
    public double height();
    public double width();
    public List<Robot> robots();
    public List<Obstacle> obstacles();
    public Environment copy();
    public void removeRobot(Robot robot);
    public void removeObstacle(Obstacle obstacle);
}