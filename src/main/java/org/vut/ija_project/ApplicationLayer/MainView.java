package org.vut.ija_project.ApplicationLayer;

import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.vut.ija_project.ApplicationLayer.Canvas.CanvasView;
import org.vut.ija_project.ApplicationLayer.SelectedView.SelectedView;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.DataLayer.Obstacle.Obstacle;
import org.vut.ija_project.DataLayer.Robot.Robot;

public class MainView extends HBox
{
    private EnvironmentManager environmentManager;
    private VBox leftSide;
    private VBox centralSide;
    private InformationView informationView;
    private CanvasView canvasView;
    private ButtonsView buttonsView;

    public MainView(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
        this.environmentManager.addMainView(this);

        this.leftSide = new FormsView(environmentManager);
        this.centralSide = new VBox(10);
        this.informationView = new InformationView(environmentManager);

        //width is 20% left and 80% right
        leftSide.maxWidthProperty().bind(this.widthProperty().multiply(1/5f));
        centralSide.maxWidthProperty().bind(this.widthProperty().multiply(3/5f));
        informationView.maxWidthProperty().bind(this.widthProperty().multiply(1/5f));

        canvasView = new CanvasView(environmentManager, this);
        buttonsView = new ButtonsView(environmentManager);
        this.centralSide.getChildren().addAll(buttonsView, canvasView);

        this.getChildren().addAll(leftSide, centralSide, informationView);
        HBox.setHgrow(leftSide, Priority.ALWAYS);
        HBox.setHgrow(centralSide, Priority.ALWAYS);
        HBox.setHgrow(informationView, Priority.ALWAYS);
    }


    public void setSelected(SelectedView selectedView) {informationView.setSelected(selectedView);}

    public void update() {
        canvasView.update();
        informationView.update();
    }

    public void reset() {
        canvasView.reset();
        informationView.setSelected(null);
    }

    public void deleteRobot(Robot robot) {
        canvasView.removeRobot(robot);
        informationView.setSelected(null);
    }

    public void addRobot(Robot robot) {
        canvasView.addRobot(robot);
    }

    public void addObstacle(Obstacle obstacle) {canvasView.addObstacle(obstacle);}

    public void deleteObstacle(Obstacle obstacle) {
        canvasView.removeObstacle(obstacle);
        informationView.setSelected(null);
    }

    public void showError(String errorMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }
}
