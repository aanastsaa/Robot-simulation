package org.vut.ija_project.DataLayer.Environment;

import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Obstacle.Obstacle;
import org.vut.ija_project.DataLayer.Robot.Robot;

import java.util.ArrayList;
import java.util.List;

public class Room implements Environment {
    private double roomHeight;
    private double roomWidth;
    private List<Robot> robotsInRoom;
    private List<Obstacle> obstaclesInRoom;

    private Room(double roomHeight, double roomWidth) {
        this.roomHeight = roomHeight;
        this.roomWidth  = roomWidth;
        robotsInRoom    = new ArrayList<Robot>();
        obstaclesInRoom = new ArrayList<Obstacle>();
    }

    public static Room create(double rows, double cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Number of rows and cols must be greater than zero.");
        }
        return new Room(rows, cols);
    }

    @Override
    public boolean addRobot(Robot robot) {
        Position robotPos = robot.getPosition();
        if (!containsPosition(robotPos) || obstacleAt(robotPos) || robotAt(robotPos)){
            return false;
        }

        robotsInRoom.add(robot);
        return true;
    }

    @Override
    public boolean containsPosition(Position pos) {
        return pos.getX() <= roomWidth && pos.getY() <= roomHeight && pos.getX() >= 0 && pos.getY() >= 0;
    }

    @Override
    public Obstacle createObstacleAt(double y, double x) {
        Position targetPos = new Position(y, x);
        if (!containsPosition(targetPos) || obstacleAt(targetPos) || robotAt(targetPos)) {
            return null;
        }
        var newObstacle = new Obstacle(this, targetPos);
        obstaclesInRoom.add(newObstacle);
        return newObstacle;
    }

    @Override
    public boolean obstacleAt(int row, int col) {
        return obstacleAt(new Position(row, col));
    }

    @Override
    public boolean obstacleAt(Position pos) {
        if (!containsPosition(pos)) return false;

        for (Obstacle obstacle : obstaclesInRoom) {
            boolean withinHorizontalBounds =
                    pos.getX() <= (obstacle.getPosition().getX() + obstacle.getObstacleSize() / 2)
                    && pos.getX() >= (obstacle.getPosition().getX() - obstacle.getObstacleSize() / 2);
            boolean withinVerticalBounds =
                    pos.getY() <= (obstacle.getPosition().getY() + obstacle.getObstacleSize() / 2)
                    && pos.getY() >= (obstacle.getPosition().getY() - obstacle.getObstacleSize() / 2);

            if (withinHorizontalBounds && withinVerticalBounds) return true;
        }

        return false;
    }

    @Override
    public boolean robotAt(Position pos) {
        if (!containsPosition(pos)) return false;

        for (Robot robot : robotsInRoom) {
            boolean withinHorizontalBounds =
                    pos.getX() <= (robot.getPosition().getX() + robot.getRobotSize() / 2)
                            && pos.getX() >= (robot.getPosition().getX() - robot.getRobotSize() / 2);
            boolean withinVerticalBounds =
                    pos.getY() <= (robot.getPosition().getY() + robot.getRobotSize() / 2)
                            && pos.getY() >= (robot.getPosition().getY() - robot.getRobotSize() / 2);

            if (withinHorizontalBounds && withinVerticalBounds) return true;
        }

        return false;
    }

    @Override
    public boolean robotAt(Position pos, Robot excludingRobot) {
        if (!containsPosition(pos)) return false;

        for (Robot robot : robotsInRoom) {
            if (robot == excludingRobot) continue;

            boolean withinHorizontalBounds =
                    pos.getX() <= (robot.getPosition().getX() + robot.getRobotSize() / 2)
                            && pos.getX() >= (robot.getPosition().getX() - robot.getRobotSize() / 2);
            boolean withinVerticalBounds =
                    pos.getY() <= (robot.getPosition().getY() + robot.getRobotSize() / 2)
                            && pos.getY() >= (robot.getPosition().getY() - robot.getRobotSize() / 2);

            if (withinHorizontalBounds && withinVerticalBounds) return true;
        }

        return false;
    }

    @Override
    public double height() {
        return roomHeight;
    }

    @Override
    public double width() {
        return roomWidth;
    }

    @Override
    public List<Robot> robots() {
        return new ArrayList<Robot>(robotsInRoom);
    }

    @Override
    public List<Obstacle> obstacles() {return new ArrayList<Obstacle>(obstaclesInRoom);}

    @Override
    public Environment copy() {
        Environment newRoom = new Room(this.roomHeight, this.roomWidth);

        for (var robot: this.robotsInRoom) {
            newRoom.addRobot(robot.copy(newRoom));
        }

        for (var obstacle: this.obstaclesInRoom) {
            newRoom.createObstacleAt(obstacle.getPosition().getY(),
                    obstacle.getPosition().getX());
        }

        return newRoom;
    }

    @Override
    public void removeRobot(Robot robot) {
        robotsInRoom.remove(robot);
    }

    @Override
    public void removeObstacle(Obstacle obstacle) {obstaclesInRoom.remove(obstacle);}
}
