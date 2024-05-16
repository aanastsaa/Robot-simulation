package org.vut.ija_project.ApplicationLayer;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;

public class FormsView extends VBox {
    private final TextField heightField;
    private TextField widthField;
    private EnvironmentManager environmentManager;
    private TextField xRobotPosField;
    private TextField yRobotPosField;
    private TextField xObstaclePosField;
    private TextField yObstaclePosField;
    private ComboBox<String> robotVariantList;

    public FormsView(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
        this.setSpacing(10);

        // -----------------------------------------------------------------------------
        // ----------- FORMS FOR SETTING DIMENSIONS AND ADDING ROBOT/OBSTACLE ---------
        // -----------------------------------------------------------------------------
        // Form for setting the dimensions of the environment
        Label titleLabel = new Label("Set the dimensions of your environment:");
        titleLabel.setStyle("-fx-font-weight: bold;");
        Label widthLabel = new Label("Width:");
        widthField = new TextField();
        widthField.setFocusTraversable(false);
        widthField.setMaxSize(80, 10);
        Label heightLabel = new Label("Height:");
        heightField = new TextField();
        heightField.setFocusTraversable(false);
        heightField.setMaxSize(80, 10);
        Button setDimensionsButton = new Button("Set Dimensions");
        setDimensionsButton.setFocusTraversable(false);
        VBox dimensionForm = new VBox(10);
        dimensionForm.setPadding(new Insets(10));
        dimensionForm.getChildren().addAll(titleLabel, widthLabel, widthField, heightLabel, heightField, setDimensionsButton);

        // Form for adding robot on the canvas with the given position
        Label addRobotLabel = new Label("Set the position of the bug:");
        addRobotLabel.setStyle("-fx-font-weight: bold;");

        Label xLabelRobotPos = new Label("X:");
        xRobotPosField = new TextField();
        xRobotPosField.setMaxSize(80, 10);
        xRobotPosField.setFocusTraversable(false);

        Label yLabelRobotPos = new Label("Y:");
        yRobotPosField = new TextField();
        yRobotPosField.setMaxSize(80, 10);
        yRobotPosField.setFocusTraversable(false);

        HBox addRobotButtonHbox = new HBox(10);
        Button addRobotButton = new Button("Add Bug");
        addRobotButton.setFocusTraversable(false);
        robotVariantList = new ComboBox<>();
        robotVariantList.setFocusTraversable(false);
        robotVariantList.getItems().addAll("Autonomous", "Controllable");
        robotVariantList.getSelectionModel().select("Autonomous");
        addRobotButtonHbox.getChildren().addAll(addRobotButton, robotVariantList);

        VBox addRobotForm = new VBox(10);
        addRobotForm.setPadding(new Insets(10));
        addRobotForm.getChildren().addAll(addRobotLabel, xLabelRobotPos,
                xRobotPosField, yLabelRobotPos, yRobotPosField, addRobotButtonHbox);

        // Form for adding obstacle on the canvas with the given position
        Label addObstacleLabel = new Label("Set the position of the obstacle:");
        addObstacleLabel.setStyle("-fx-font-weight: bold;");
        Label xLabelObstaclePos = new Label("X:");
        xObstaclePosField = new TextField();
        xObstaclePosField.setFocusTraversable(false);
        xObstaclePosField.setMaxSize(80, 10);

        Label yLabelObstaclePos = new Label("Y:");
        yObstaclePosField = new TextField();
        yObstaclePosField.setMaxSize(80, 10);
        yObstaclePosField.setFocusTraversable(false);

        Button addObstacleButton = new Button("Add Obstacle");
        addObstacleButton.setFocusTraversable(false);
        VBox addObstacleForm = new VBox(10);
        addObstacleForm.setPadding(new Insets(10));
        addObstacleForm.getChildren().addAll(addObstacleLabel, xLabelObstaclePos,
                xObstaclePosField, yLabelObstaclePos, yObstaclePosField, addObstacleButton);

        // For setting the dimensions of the environment
        setDimensionsButton.setOnAction(event -> createEnvironment());
        // For adding robot on the canvas
        addRobotButton.setOnAction(event -> addRobot());
        // For adding obstacle on the canvas
        addObstacleButton.setOnAction(event -> addObstacle());

        this.getChildren().addAll(dimensionForm, addRobotForm, addObstacleForm);
    }

    private void createEnvironment() {
        double x = Double.parseDouble(widthField.getText());
        double y = Double.parseDouble(heightField.getText());

        environmentManager.createEnvironment(y, x);
    }

    private void addObstacle() {
        double x = Double.parseDouble(xObstaclePosField.getText());
        double y = Double.parseDouble(yObstaclePosField.getText());

        environmentManager.addObstacle(y, x);
    }

    private void addRobot() {
        double x = Double.parseDouble(xRobotPosField.getText());
        double y = Double.parseDouble(yRobotPosField.getText());

        String type = robotVariantList.getSelectionModel().getSelectedItem();
        environmentManager.addRobot(y, x, type);
    }
}
