package org.vut.ija_project.BusinessLayer;

import org.vut.ija_project.ApplicationLayer.MainView;
import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Environment.Environment;
import org.vut.ija_project.DataLayer.Environment.Room;
import org.vut.ija_project.DataLayer.EnvironmentFileIo.EnvironmentFileIO;
import org.vut.ija_project.DataLayer.Obstacle.Obstacle;
import org.vut.ija_project.DataLayer.Robot.AutonomousRobot;
import org.vut.ija_project.DataLayer.Robot.ControlledRobot;
import org.vut.ija_project.DataLayer.Robot.Robot;

import java.io.File;
import java.util.List;

/**
 * The main controller for the application.
 * This class manages the interactions between the user interface
 * and the environment in the application.
 */
public class EnvironmentManager
{
    private MainView mainView;
    private Environment currEnvironment;
    private Environment initialEnvironment;

    public EnvironmentManager(Environment environment) {
        this.initialEnvironment = environment;
        this.currEnvironment = this.initialEnvironment.copy();
    }

    public void addMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public void simulationReset() {
        this.currEnvironment = this.initialEnvironment.copy();
        mainView.reset();
        mainView.update();
    }

    public void simulationStep() {
        List<Robot> robots = this.currEnvironment.robots();

        for (var robot : robots) {
            robot.updatePosition();
        }

        mainView.update();
    }

    public Simulator getSimulator() {
        return new Simulator(this);
    }

    public List<Robot> getRobots() {
        return this.currEnvironment.robots();
    }

    public double getHeight() {
        return this.currEnvironment.height();
    }

    public double getWidth() {
        return this.currEnvironment.width();
    }

    public List<Obstacle> getObstacles() {return this.currEnvironment.obstacles();}

    public void addRobot(double y, double x, String type) {
        Position newRobotPos = new Position(y, x);
        Robot newRobot = null;

        if (type.equals("Autonomous")) {
            newRobot = AutonomousRobot.create(currEnvironment, newRobotPos);
        } else if (type.equals("Controllable")) {
            newRobot = ControlledRobot.create(currEnvironment, newRobotPos);
        }

        if (newRobot == null) {
            System.out.println("Robot is not created");
            mainView.showError("Invalid robot's position");
            return;
        }

        //if we want to add robot, make current environment position initial
        this.initialEnvironment = this.currEnvironment.copy();
        mainView.addRobot(newRobot);
    }

    public void addObstacle(double y, double x) {
        Obstacle created = this.currEnvironment.createObstacleAt(y, x);

        if (created == null) {
            System.out.println("Robot is not created");
            mainView.showError("Invalid obstacle's position");
            return;
        }

        //if we want to add obstacle, make current environment position initial
        this.initialEnvironment = this.currEnvironment.copy();
        mainView.addObstacle(created);
    }

    public void deleteRobot(Robot robot) {
        this.currEnvironment.removeRobot(robot);

        this.initialEnvironment = this.currEnvironment.copy();
        mainView.deleteRobot(robot);
    }

    public void updateRobot(Robot robot, ObjectConfiguration configuration) {
        if (!isConfigurationPositionValid(configuration, robot)) {
            System.out.println("Robot is not updated");
            mainView.showError("Invalid robot's position");
            return;
        }

        robot.setConfiguration(configuration);

        this.initialEnvironment = this.currEnvironment.copy();
        mainView.update();
    }

    public void deleteObstacle(Obstacle obstacle) {
        this.currEnvironment.removeObstacle(obstacle);

        this.initialEnvironment = this.currEnvironment.copy();
        mainView.deleteObstacle(obstacle);
    }

    public void updateObstacle(Obstacle obstacle, ObjectConfiguration configuration) {
        if (!isConfigurationPositionValid(configuration, null)) {
            System.out.println("Obstacle is not updated");
            mainView.showError("Invalid obstacle's position");
            return;
        }

        obstacle.setConfiguration(configuration);

        this.initialEnvironment = this.currEnvironment.copy();
        mainView.update();
    }

    private boolean isConfigurationPositionValid(ObjectConfiguration configuration, Robot excludingRobot) {
        Position newPos = new Position(configuration.newY, configuration.newX);
        return this.currEnvironment.containsPosition(newPos)
                && !this.currEnvironment.obstacleAt(newPos) && !this.currEnvironment.robotAt(newPos, excludingRobot);
    }

    public void turnControlledRobotCounterClockwise(Robot robot) {
        ControlledRobot controlledRobot = (ControlledRobot) robot;
        controlledRobot.setState(ControlledRobot.State.TURN_COUNTERCLOCKWISE);
    }

    public void turnControlledRobotClockwise(Robot robot) {
        ControlledRobot controlledRobot = (ControlledRobot) robot;
        controlledRobot.setState(ControlledRobot.State.TURN_CLOCKWISE);
    }

    public void moveControlledRobotForward(Robot robot) {
        ControlledRobot controlledRobot = (ControlledRobot) robot;
        controlledRobot.setState(ControlledRobot.State.MOVE_FORWARD);
    }

    public void getEnvironmentFromFile(File chosenFile) {
        //backup environment, if something went wrong
        Environment backupEnvironment = this.currEnvironment.copy();
        try {
            Environment environmentFromFile = EnvironmentFileIO.createEnvironmentFromSource(chosenFile);
            //delete everything from current main view
            deleteEverythingFromMainView();

            this.initialEnvironment = environmentFromFile.copy();
            this.currEnvironment = this.initialEnvironment.copy();
            //add everything from file's environment
            this.currEnvironment.robots().forEach(r->mainView.addRobot(r));
            this.currEnvironment.obstacles().forEach(o->mainView.addObstacle(o));

            mainView.update();
        } catch (RuntimeException ignored) {
            this.initialEnvironment = backupEnvironment;
            this.currEnvironment = this.initialEnvironment.copy();
        }
    }

    public void createEnvironment(double y, double x) {
        deleteEverythingFromMainView();
        this.initialEnvironment = Room.create(y, x);
        this.currEnvironment = this.initialEnvironment.copy();
        mainView.update();
    }

    private void deleteEverythingFromMainView() {
        //delete everything from current main view
        this.currEnvironment.robots().forEach(r->mainView.deleteRobot(r));
        this.currEnvironment.obstacles().forEach(o->mainView.deleteObstacle(o));
    }

    public void exportEnvironmentToCsv(File chosenFile) {
        EnvironmentFileIO.createFileFromEnvironment(this.currEnvironment, chosenFile);
    }
}
