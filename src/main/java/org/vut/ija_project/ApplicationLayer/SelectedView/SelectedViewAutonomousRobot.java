package org.vut.ija_project.ApplicationLayer.SelectedView;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.vut.ija_project.ApplicationLayer.Canvas.RobotView;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Robot.Robot;
import org.vut.ija_project.DataLayer.Robot.RobotColor;
import org.vut.ija_project.DataLayer.Robot.RobotType;

import java.util.Locale;

public class SelectedViewAutonomousRobot extends SelectedView {
    private EnvironmentManager environmentManager;
    private final RobotView robotView;
    private TextField fieldSetX;
    private TextField fieldSetY;
    private TextField fieldSetVelocity;
    private TextField fieldSetAngle;
    private TextField fieldSetRotationAngle;
    private Label positionLabel;
    private Label angleLabel;
    private Label velocityLabel;
    private Label rotationAngleLabel;
    private ComboBox<String> colorComboBox;

    public SelectedViewAutonomousRobot(RobotView robotView, EnvironmentManager environmentManager) {
        this.robotView = robotView;
        this.environmentManager = environmentManager;
        VBox.setVgrow(this, Priority.ALWAYS);

        initialize();
    }

    private void initialize() {
        createNewInfoLabel("Autonomous Bug", 50, 24);
        positionLabel = createNewInfoLabel("Current Position: " + getPosition(), 0, 18);
        angleLabel = createNewInfoLabel("Current Angle: " + robotView.getRobot().angle(), 0, 18);
        velocityLabel = createNewInfoLabel("Current Velocity: " + robotView.getRobot().getVelocity(), 0, 18);
        rotationAngleLabel = createNewInfoLabel("Current Rotation Angle: " + robotView.getRobot().getRotationAngle(),
                30, 18);

        fieldSetX = createNewSetField("Set x: ", getX());
        fieldSetY = createNewSetField("Set y: ", getY());
        fieldSetAngle = createNewSetField("Set angle: ",
                Double.toString(robotView.getRobot().angle()));
        fieldSetVelocity = createNewSetField("Set velocity: ",
                Double.toString(robotView.getRobot().getVelocity()));
        fieldSetRotationAngle = createNewSetField("Set rotation: ",
                Double.toString(robotView.getRobot().getRotationAngle()));

        colorComboBox = createNewColorComboBox();
        colorComboBox.getSelectionModel().select(getColorText());

        addUpdateDeleteButtons();

        deleteButton.setOnAction(this::handleDelete);
        updateButton.setOnAction(this::handleUpdateButton);
    }

    private String getColorText() {
        return switch (robotView.getRobot().getColor()) {
            case RED -> "Red";
            case ORANGE -> "Orange";
            case YELLOW -> "Yellow";
            case GREEN -> "Green";
            case BLUE -> "Blue";
            case PURPLE -> "Purple";
            case WHITE -> "White";
        };
    }

    @Override
    public void update() {
        this.positionLabel.setText("Current Position: " + getPosition());
        this.angleLabel.setText("Current Angle: " + robotView.getRobot().angle());
        this.velocityLabel.setText("Current Velocity: " + robotView.getRobot().getVelocity());
        this.rotationAngleLabel.setText("Current Rotation Angle: " + robotView.getRobot().getRotationAngle());

        this.fieldSetX.setText(getX());
        this.fieldSetY.setText(getY());
        this.fieldSetAngle.setText(Double.toString(robotView.getRobot().angle()));
    }

    @Override
    public RobotType getType() {
        return RobotType.AUTONOMOUS;
    }

    private void handleUpdateButton(ActionEvent actionEvent) {
        var newX = Double.parseDouble(fieldSetX.getText());
        var newY = Double.parseDouble(fieldSetY.getText());
        var newAngle = (int) Double.parseDouble(fieldSetAngle.getText());
        var newVelocity = Double.parseDouble(fieldSetVelocity.getText());
        var newRotationAngle = (int) Double.parseDouble(fieldSetRotationAngle.getText());
        var newColor = getRobotColor(this.colorComboBox);
        Robot robot = robotView.getRobot();

        //if there is no changes don't update anything
        if (newX == robot.getPosition().getX() && newY == robot.getPosition().getY()
            && newAngle == robot.angle() && newVelocity == robot.getVelocity()
            && newRotationAngle == robot.getRotationAngle() && newColor == robot.getColor()) return;

        //TODO: update robot in environment
        var configuration = new ObjectConfiguration(newX, newY, newAngle, newVelocity, newRotationAngle, newColor);
        environmentManager.updateRobot(robot, configuration);
    }

    private void handleDelete(ActionEvent actionEvent) {
        environmentManager.deleteRobot(robotView.getRobot());
    }

    private String getX() {
        double x = robotView.getRobot().getPosition().getX();
        return String.format(Locale.US, "%.2f", x);
    }

    private String getY() {
        double y = robotView.getRobot().getPosition().getY();
        return String.format(Locale.US, "%.2f", y);
    }

    private String getPosition() {
        return String.format(getX() + " : " + getY());
    }
}
