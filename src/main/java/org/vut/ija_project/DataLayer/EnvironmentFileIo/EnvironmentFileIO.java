package org.vut.ija_project.DataLayer.EnvironmentFileIo;

import org.apache.commons.csv.CSVPrinter;
import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Robot.ControlledRobot;
import org.vut.ija_project.DataLayer.Environment.Environment;
import java.io.*;
import java.util.Objects;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.vut.ija_project.DataLayer.Environment.Room;
import org.vut.ija_project.DataLayer.Robot.AutonomousRobot;
import org.vut.ija_project.DataLayer.Robot.Robot;
import org.vut.ija_project.DataLayer.Robot.RobotColor;

public class EnvironmentFileIO
{
    private enum ObjectType {
        OBSTACLE,
        CONTROLLED_ROBOT,
        AUTONOMOUS_ROBOT,
        NOT_FOUND
    }
    private static Environment environment;

    public static void createFileFromEnvironment(Environment currEnvironment, File file) {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("type", "x", "y", "angle", "rotate_angle", "color");

        try (FileWriter fileWriter = new FileWriter(file);
             CSVPrinter printer = new CSVPrinter(fileWriter, format)) {
            printer.printRecord("e", currEnvironment.width(), currEnvironment.height(), "0", "0", "white");
            for (var obstacle : currEnvironment.obstacles()) {
                printer.printRecord("o", obstacle.getPosition().getX(),
                        obstacle.getPosition().getY(), "0", "0", "white");
            }

            for (var robot : currEnvironment.robots()) {
                printer.printRecord(getRobotType(robot), robot.getPosition().getX(),
                        robot.getPosition().getY(), robot.angle(),
                        robot.getRotationAngle(), getRobotColorFromType(robot.getColor()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getRobotColorFromType(RobotColor color) {
        return switch (color) {
            case RED -> "red";
            case ORANGE -> "orange";
            case YELLOW -> "yellow";
            case GREEN -> "green";
            case BLUE -> "blue";
            case PURPLE -> "purple";
            case WHITE -> "white";
        };
    }

    private static String getRobotType(Robot robot) {
        return switch (robot.getType()) {
            case CONTROLLABLE -> "c";
            case AUTONOMOUS -> "a";
            case NOT_ROBOT -> throw new RuntimeException();
        };
    }

    public static Environment createEnvironmentFromSource(File source) throws RuntimeException
    {
        environment = null;

        try (Reader fileReader = new FileReader(source);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {
            boolean isFirst = true;
            for (CSVRecord record : csvParser) {
                if (isFirst) {
                    if (!Objects.equals(record.get("type"), "e"))
                        throw new RuntimeException("there is no Environment specifier");

                    double x = Double.parseDouble(record.get("x"));
                    double y = Double.parseDouble(record.get("y"));
                    environment = Room.create(y, x);
                    isFirst = false;
                } else {
                    ObjectType type = GetType(record.get("type"));
                    if (type == ObjectType.NOT_FOUND) throw new RuntimeException("Unknown type: " + record.get("type"));
                    double x = Double.parseDouble(record.get("x"));
                    double y = Double.parseDouble(record.get("y"));
                    int angle = Integer.parseInt(record.get("angle"));
                    int angleRotate = Integer.parseInt(record.get("rotate_angle"));
                    RobotColor color = getRobotColorFromString(record.get("color"));

                    addObjectToEnvironment(type, x, y, angle, angleRotate, color);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return environment;
    }

    private static RobotColor getRobotColorFromString(String color) {
        return switch (color) {
            case "red" -> RobotColor.RED;
            case "orange" -> RobotColor.ORANGE;
            case "yellow" -> RobotColor.YELLOW;
            case "green" -> RobotColor.GREEN;
            case "blue" -> RobotColor.BLUE;
            case "purple" -> RobotColor.PURPLE;
            case "white" -> RobotColor.WHITE;
            default -> throw new IllegalStateException("Unexpected value: " + color);
        };
    }


    private static ObjectType GetType(String type) {
        return switch (type) {
            case "c" -> ObjectType.CONTROLLED_ROBOT;
            case "a" -> ObjectType.AUTONOMOUS_ROBOT;
            case "o" -> ObjectType.OBSTACLE;
            default -> ObjectType.NOT_FOUND;
        };
    }

    private static void addObjectToEnvironment(ObjectType type, double x,
                                               double y, int angle, int rotateAngle, RobotColor color) {
        switch (type) {
            case OBSTACLE -> environment.createObstacleAt(y, x);
            case CONTROLLED_ROBOT -> ControlledRobot.create(environment, new Position(y, x), angle, color);
            case AUTONOMOUS_ROBOT -> AutonomousRobot.create(environment, new Position(y, x), angle, rotateAngle, color);
            default -> {}
        }
    }
}
