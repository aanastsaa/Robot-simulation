package org.vut.ija_project.ApplicationLayer.SelectedView;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.vut.ija_project.ApplicationLayer.Canvas.RobotView;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Robot.Robot;
import org.vut.ija_project.DataLayer.Robot.RobotType;

import java.util.Locale;

public class SelectedViewControlledRobot extends SelectedView {
    private EnvironmentManager environmentManager;
    private final RobotView robotView;
    private TextField fieldSetX;
    private TextField fieldSetY;
    private TextField fieldSetVelocity;
    private TextField fieldSetAngle;
    private TextField fieldSetRotationAngle;
    private Button left;
    private Button center;
    private Button right;
    private Label positionLabel;
    private Label angleLabel;
    private ComboBox<String> colorComboBox;

    public SelectedViewControlledRobot(RobotView robotView, EnvironmentManager environmentManager) {
        this.robotView = robotView;
        this.environmentManager = environmentManager;
        VBox.setVgrow(this, Priority.ALWAYS);
        initialize();
    }

    private void initialize() {
        this.getChildren().clear();
        createNewInfoLabel("Controlled Bug", 50, 24);

        fieldSetX = createNewSetField("Set x: ", getX());
        fieldSetY = createNewSetField("Set y: ", getY());
        fieldSetAngle = createNewSetField("Set angle: ",
                Double.toString(robotView.getRobot().angle()));
        fieldSetVelocity = createNewSetField("Set Velocity: ",
                Double.toString(robotView.getRobot().getVelocity()));

        colorComboBox = createNewColorComboBox();
        colorComboBox.getSelectionModel().select("Yellow");

        addControlButtons();
        addUpdateDeleteButtons();

        deleteButton.setOnAction(this::handleDelete);
        updateButton.setOnAction(this::handleUpdateButton);

        left.setOnAction(event-> handleCounterClockwiseTurn());
        center.setOnAction(event-> handleForwardMove());
        right.setOnAction(event-> handleClockwiseTurn());
        setupKeyHandlers();
    }

    private void setupKeyHandlers() {
        this.setFocusTraversable(true);

        this.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT -> handleCounterClockwiseTurn();
                case UP -> handleForwardMove();
                case RIGHT -> handleClockwiseTurn();
            }
        });
    }

    @Override
    public void update() {}

    @Override
    public RobotType getType() {
        return RobotType.CONTROLLABLE;
    }

    private void handleClockwiseTurn() {
        environmentManager.turnControlledRobotClockwise(this.robotView.getRobot());
    }

    private void handleForwardMove() {
        environmentManager.moveControlledRobotForward(this.robotView.getRobot());
    }

    private void handleCounterClockwiseTurn() {
        environmentManager.turnControlledRobotCounterClockwise(this.robotView.getRobot());
    }

    private void addControlButtons() {
        HBox controlButtonHbox = new HBox(5);
        controlButtonHbox.setAlignment(Pos.CENTER);
        left = new Button("←");
        left.setFont(new Font("Arial", 30));
        left.setFocusTraversable(false);

        center = new Button("↑");
        center.setFont(new Font("Arial", 30));
        center.setFocusTraversable(false);

        right = new Button("→");
        right.setFont(new Font("Arial", 30));
        right.setFocusTraversable(false);

        controlButtonHbox.getChildren().addAll(left, center, right);
        controlButtonHbox.setPadding(new Insets(75, 0, 0, 0));
        this.getChildren().add(controlButtonHbox);
    }

    private void handleUpdateButton(ActionEvent actionEvent) {
        var newX = Double.parseDouble(fieldSetX.getText());
        var newY = Double.parseDouble(fieldSetY.getText());
        var newAngle = (int) Double.parseDouble(fieldSetAngle.getText());
        var newVelocity = Double.parseDouble(fieldSetVelocity.getText());
        var newColor = getRobotColor(colorComboBox);

        Robot robot = robotView.getRobot();

        //if there is no changes don't update anything
        if (newX == robot.getPosition().getX() && newY == robot.getPosition().getY()
                && newAngle == robot.angle() && newVelocity == robot.getVelocity()
                && newColor == robot.getColor()) return;

        var configuration = new ObjectConfiguration(newX, newY, newAngle, newVelocity, 0, newColor);
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
