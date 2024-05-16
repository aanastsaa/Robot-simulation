package org.vut.ija_project.DataLayer.Robot;

import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Environment.Environment;

public class AutonomousRobot implements Robot {
    private final double robotSize = 1;
    private double velocity;
    private final Environment env;
    private Position pos;
    private int angle;
    private int turningAngle;
    private double maxDistanceFromBlock;
    private Direction turningDirection;
    private RobotColor color;

    public enum Direction {
        COUNTERCLOCKWISE,
        CLOCKWISE
    }

    public AutonomousRobot(Environment env, Position pos, int angle) {
        this.env = env;
        this.pos = pos;
        this.angle = angle;
        this.velocity = 0.1;
        this.maxDistanceFromBlock = this.velocity;
        this.turningAngle = 5;
        this.turningDirection = Direction.CLOCKWISE;
        this.color = RobotColor.RED;
    }

    public AutonomousRobot(Environment env, Position pos, int angle, int turningAngle, RobotColor color) {
        this(env, pos, angle);
        this.turningAngle = turningAngle;
        this.color = color;
    }

    public static AutonomousRobot create(Environment env, Position pos) {
        if (env == null || pos == null) return null;
        if (!env.containsPosition(pos) ||env.obstacleAt(pos) || env.robotAt(pos)) return null;

        AutonomousRobot newRobot = new AutonomousRobot(env, pos, 0);
        env.addRobot(newRobot);
        return newRobot;
    }

    public static AutonomousRobot create(Environment env, Position pos, int angle, int rotateAngle, RobotColor color) {
        if (env == null || pos == null) return null;
        if (env.obstacleAt(pos) || env.robotAt(pos)) return null;

        AutonomousRobot newRobot = new AutonomousRobot(env, pos, angle, rotateAngle, color);
        env.addRobot(newRobot);
        return newRobot;
    }

    @Override
    public int angle() {
        return angle;
    }

    private void turn() {
        if (turningDirection == Direction.COUNTERCLOCKWISE) {
            turnCounterClockwise();
        } else {
            turnClockwise();
        }
    }

    private void turnClockwise() {
        angle = (angle + turningAngle) % 360;
    }

    private void turnCounterClockwise() {
        angle = (angle - turningAngle + 360) % 360;
    }

    private boolean move() {
        if (!canMove()) return false;

        pos = getTargetPosition();
        return true;
    }

    @Override
    public boolean canMove() {
        Position targetPos = getTargetPosition();
        return env.containsPosition(targetPos)
                && !env.obstacleAt(targetPos) && !env.robotAt(targetPos, this);
    }

    private Position getTargetPosition() {
        // trigonometric angle is rotating counter-clockwise => multiply by -1
        // angle 0 is angle 90 on trigonometric circular => add 90
        // we need to multiply by maxDistanceFromBlock to check for desired position of robot
        double angleRadians = Math.toRadians(-angle + 90);
        double deltaX = Math.cos(angleRadians) * velocity;
        double deltaY = Math.sin(angleRadians) * (-1) * velocity;

        return new Position(pos.getY() + deltaY, pos.getX() + deltaX);
    }

    public void setTurningAngle(int newTurningAngle) {
        turningAngle = newTurningAngle;
    }

    public void setMaxDistanceFromBlock(int newMaxDistanceFromBlock) {
        maxDistanceFromBlock = newMaxDistanceFromBlock;
    }

    public void setTurningDirection(Direction newDirection) {
        turningDirection = newDirection;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public void updatePosition() {
        if (!move()) turn();
    }

    @Override
    public Robot copy(Environment env) {
        return new AutonomousRobot(env, this.pos, this.angle, this.turningAngle, this.color);
    }

    @Override
    public double getVelocity() {return velocity;}

    @Override
    public int getRotationAngle() {
        return turningAngle;
    }

    @Override
    public void setConfiguration(ObjectConfiguration configuration) {
        pos = new Position(configuration.newY, configuration.newX);
        angle = configuration.newAngle;
        velocity = configuration.newVelocity;
        maxDistanceFromBlock = velocity;
        turningAngle = configuration.newRotationAngle;
        color = configuration.newColor;
    }

    @Override
    public double getRobotSize() {
        return this.robotSize;
    }

    @Override
    public RobotType getType() {return RobotType.AUTONOMOUS;}

    @Override
    public RobotColor getColor() {
        return this.color;
    }
}
