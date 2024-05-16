package org.vut.ija_project.ApplicationLayer.SelectedView;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.vut.ija_project.ApplicationLayer.Canvas.ObstacleView;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Obstacle.Obstacle;
import org.vut.ija_project.DataLayer.Robot.RobotType;

import java.util.Locale;

public class SelectedViewObstacle extends SelectedView {
    private final ObstacleView obstacleView;
    private final EnvironmentManager environmentManager;
    private TextField fieldSetX;
    private TextField fieldSetY;

    public SelectedViewObstacle(ObstacleView obstacleView, EnvironmentManager environmentManager) {
        this.obstacleView = obstacleView;
        this.environmentManager = environmentManager;
        VBox.setVgrow(this, Priority.ALWAYS);
    }

    @Override
    public void update() {
        this.getChildren().clear();
        createNewInfoLabel("Obstacle", 100, 24);
        createNewInfoLabel("Current Position: " + getPosition(), 100, 16);

        fieldSetX = createNewSetField("Set x: ", getX());
        fieldSetY = createNewSetField("Set y: ", getY());

        addUpdateDeleteButtons();

        deleteButton.setOnAction(this::handleDelete);
        updateButton.setOnAction(this::handleUpdateButton);
    }

    @Override
    public RobotType getType() {
        return RobotType.NOT_ROBOT;
    }

    private void handleUpdateButton(ActionEvent actionEvent) {
        var newX = Double.parseDouble(fieldSetX.getText());
        var newY = Double.parseDouble(fieldSetY.getText());
        Obstacle obstacle = obstacleView.getObstacle();

        //if there is no changes don't update anything
        if (newX == obstacle.getPosition().getX() && newY == obstacle.getPosition().getY()) return;

        var configuration = new ObjectConfiguration(newX, newY);
        environmentManager.updateObstacle(obstacle, configuration);
    }

    private void handleDelete(ActionEvent actionEvent) {
        environmentManager.deleteObstacle(obstacleView.getObstacle());
    }

    private String getX() {
        double x = obstacleView.getObstacle().getPosition().getX();
        return String.format(Locale.US, "%.2f", x);
    }

    private String getY() {
        double y = obstacleView.getObstacle().getPosition().getY();
        return String.format(Locale.US, "%.2f", y);
    }

    private String getPosition() {
        return String.format(getX() + " : " + getY());
    }
}
