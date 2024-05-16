package org.vut.ija_project.DataLayer.Robot;

import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Environment.Environment;

public class ControlledRobot implements Robot {
    private double velocity;
    private final Environment env;
    private Position pos;
    private int angle;
    private State currState;
    private double robotSize;
    private RobotColor color;

    public enum State {
        TURN_COUNTERCLOCKWISE,
        TURN_CLOCKWISE,
        MOVE_FORWARD,
        NOTHING
    }

    private ControlledRobot(Environment env, Position pos) {
        this.env = env;
        this.pos = pos;
        this.angle = 0;
        this.robotSize = 1;
        this.velocity = 0.1;
        this.color = RobotColor.YELLOW;
        currState = State.NOTHING;
    }

    private ControlledRobot(Environment env, Position pos, int angle, RobotColor color) {
        this(env, pos);
        this.angle = angle;
        this.color = color;
    }

    public static ControlledRobot create(Environment env, Position pos) {
        if (env == null || pos == null) return null;
        if (!env.containsPosition(pos) || env.obstacleAt(pos) || env.robotAt(pos)) return null;

        ControlledRobot newRobot = new ControlledRobot(env, pos);
        env.addRobot(newRobot);
        return newRobot;
    }

    public static ControlledRobot create(Environment env, Position pos, int angle, RobotColor color) {
        if (env == null || pos == null) return null;
        if (env.obstacleAt(pos) || env.robotAt(pos)) return null;

        ControlledRobot newRobot = new ControlledRobot(env, pos, angle, color);
        env.addRobot(newRobot);
        return newRobot;
    }

    @Override
    public int angle() {
        return angle;
    }

    private void turnClockWise() {
        angle = (angle + 45) % 360;
        currState = State.NOTHING;
    }

    private void turnCounterClockwise() {
        angle = (angle - 45 + 360) % 360;
        currState = State.NOTHING;
    }

    @Override
    public boolean canMove() {
        Position targetPos = getTargetPosition();
        return env.containsPosition(targetPos) && !env.obstacleAt(targetPos) && !env.robotAt(targetPos, this);
    }

    private Position getTargetPosition() {
        // trigonometric angle is rotating counter-clockwise => multiply by -1
        // angle 0 is angle 90 on trigonometric circular => add 90
        double angleRadians = Math.toRadians(-angle + 90);
        double deltaX = Math.cos(angleRadians) * this.velocity;
        double deltaY = Math.sin(angleRadians) * (-1) * this.velocity;

        return new Position(deltaY + pos.getY(), deltaX + pos.getX());
    }

    @Override
    public void updatePosition() {
        switch (currState) {
            case MOVE_FORWARD:
                move();
                break;
            case TURN_COUNTERCLOCKWISE:
                turnCounterClockwise();
                break;
            case TURN_CLOCKWISE:
                turnClockWise();
                break;
            case NOTHING:
                break;
        }
    }

    @Override
    public Robot copy(Environment env) {
        return new ControlledRobot(env, this.pos, this.angle, this.color);
    }

    //TODO: change velocity
    @Override
    public double getVelocity() {
        return this.velocity;
    }

    @Override
    public int getRotationAngle() {
        return 0;
    }

    @Override
    public void setConfiguration(ObjectConfiguration configuration) {
        this.pos = new Position(configuration.newY, configuration.newX);
        this.angle = configuration.newAngle;
        this.velocity = configuration.newVelocity;
        this.color = configuration.newColor;
    }
    @Override
    public Position getPosition() {
        return pos;
    }
    @Override
    public double getRobotSize() {return this.robotSize;}

    @Override
    public RobotType getType() {return RobotType.CONTROLLABLE;}

    @Override
    public RobotColor getColor() {return this.color;}

    public void setState(State state) {
        currState = state;
    }

    private void move() {
        currState = State.NOTHING;
        if (!canMove()) {return;}

        pos = getTargetPosition();
        currState = State.NOTHING;
    }
}
