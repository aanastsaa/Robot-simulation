package org.vut.ija_project.ApplicationLayer.Canvas;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import org.vut.ija_project.ApplicationLayer.MainView;
import org.vut.ija_project.ApplicationLayer.SelectedView.SelectedView;
import org.vut.ija_project.ApplicationLayer.SelectedView.SelectedViewFactory;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.Common.ObjectConfiguration;
import org.vut.ija_project.DataLayer.Obstacle.Obstacle;
import org.vut.ija_project.DataLayer.Robot.Robot;

import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CanvasView extends HBox {
    private final MainView mainView;
    private Canvas canvas;
    protected int canvasWidth = 900;
    protected int canvasHeight = 600;
    private GraphicsContext gc;
    private EnvironmentManager environmentManager;
    private int previousRow;
    private int previousCol;
    private double cellWidth;
    private double cellHeight;
    private List<RobotView> robotViewList;
    private List<ObstacleView> obstacleViewList;
    private DraggableView dragged;

    public CanvasView(EnvironmentManager environmentManager, MainView mainView) {
        robotViewList = new ArrayList<RobotView>();
        obstacleViewList = new ArrayList<ObstacleView>();

        this.environmentManager = environmentManager;
        this.mainView = mainView;
        this.setSpacing(10);
        // Make canvas
        canvas = new Canvas(canvasWidth, canvasHeight);
        this.cellWidth = canvasWidth / (environmentManager.getWidth() + 1);
        this.cellHeight = canvasHeight / (environmentManager.getHeight() + 1);

        canvas.setOnMouseMoved(this::handleMoveMouse);
        canvas.setOnMouseEntered(this::handleEnterMouse);
        canvas.setOnMouseExited(this::handleExitMouse);
        canvas.setOnMousePressed(this::handlePressMouse);
        canvas.setOnMouseDragged(this::handleDrag);
        canvas.setOnMouseReleased(this::handleReleaseMouse);

        gc = canvas.getGraphicsContext2D();
        eraseObjects();

        this.getChildren().add(canvas);
        this.setAlignment(Pos.CENTER);
    }

    private void eraseObjects() {
        gc.setFill(Color.LIGHTGRAY);

        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setLineWidth(0.2);
        for (int x = 0; x <= environmentManager.getWidth() + 1; x++) {
            double xPos = x * cellWidth;
            gc.strokeLine(xPos, 0, xPos, canvasHeight);
        }

        // Draw horizontal lines (rows)
        for (int y = 0; y <= environmentManager.getHeight() + 1; y++) {
            double yPos = y * cellHeight;
            gc.strokeLine(0, yPos, canvasWidth, yPos);
        }
    }

    public void update() {
        this.cellWidth = canvasWidth / (environmentManager.getWidth() + 1);
        this.cellHeight = canvasHeight / (environmentManager.getHeight() + 1);
        eraseObjects();

        robotViewList.forEach(RobotView::update);
        obstacleViewList.forEach(ObstacleView::update);
    }


    private void updateCanvasWithoutDraggedObj(DraggableView dragged) {
        eraseObjects();

        for (var robotView : robotViewList) {
            if (robotView == dragged) continue;
            robotView.update();
        }
        for (var obstacleView : obstacleViewList) {
            if (obstacleView == dragged) continue;
            obstacleView.update();
        }
    }

    public void addRobot(Robot robot) {
        var newRobotView = new RobotView(robot, this, environmentManager);
        robotViewList.add(newRobotView);
        update();
    }

    public void addObstacle(Obstacle obstacle) {
        var newObstacleView = new ObstacleView(obstacle, this, environmentManager);
        obstacleViewList.add(newObstacleView);
        update();
    }

    public void removeRobot(Robot robot) {
        Optional<RobotView> robotViewToRemoveOptional = robotViewList.stream()
                .filter(r -> r.getRobot() == robot)
                .findFirst();

        if (robotViewToRemoveOptional.isPresent()) {
            RobotView robotViewToRemove = robotViewToRemoveOptional.get();
            robotViewList.remove(robotViewToRemove);
        } else {
            System.out.println("Can't be here (I hope)");
        }
        update();
    }


    public void removeObstacle(Obstacle obstacle) {
        Optional<ObstacleView> obstacleViewToRemoveOptional = obstacleViewList.stream()
                .filter(r -> r.getObstacle() == obstacle)
                .findFirst();

        if (obstacleViewToRemoveOptional.isPresent()) {
            ObstacleView obstacleViewToRemove = obstacleViewToRemoveOptional.get();
            obstacleViewList.remove(obstacleViewToRemove);
        } else {
            System.out.println("Can't be here (I hope)");
        }

        update();
    }

    public void reset() {
        robotViewList = new ArrayList<RobotView>();
        List<Robot> robots = this.environmentManager.getRobots();
        for (var robot : robots) {
            addRobot(robot);
        }

        obstacleViewList = new ArrayList<ObstacleView>();
        List<Obstacle> obstacles = this.environmentManager.getObstacles();
        for (var obstacle: obstacles) {
            addObstacle(obstacle);
        }
    }


    private void handlePressMouse(MouseEvent mouseEvent) {
        SelectedView selectedView = null;

        RobotView selectedRobotView = null;
        robotViewList.forEach(robotView -> robotView.setSelected(false));
        for (var robotView : robotViewList) {
            if (robotView.isClicked(mouseEvent.getX(), mouseEvent.getY())) {
                selectedRobotView = robotView; break;
            }
        }

        ObstacleView selectedObstacleView = null;
        obstacleViewList.forEach(obstacleView -> obstacleView.setSelected(false));
        for (var obstacleView : obstacleViewList) {
            if (obstacleView.isClicked(mouseEvent.getX(), mouseEvent.getY())) {
                selectedObstacleView = obstacleView; break;
            }
        }

        if (selectedRobotView != null) {
            selectedRobotView.setSelected(true);
            dragged = selectedRobotView;
            selectedView = SelectedViewFactory.create(selectedRobotView, environmentManager);
        } else if (selectedObstacleView != null) {
            selectedObstacleView.setSelected(true);
            dragged = selectedObstacleView;
            selectedView = SelectedViewFactory.create(selectedObstacleView, environmentManager);
        }

        mainView.setSelected(selectedView);
    }

    private void handleDrag(MouseEvent mouseEvent) {
        if (dragged != null) {
            updateCanvasWithoutDraggedObj(dragged);
            dragged.drawAt(mouseEvent.getX(), mouseEvent.getY());
        }
    }


    private void handleReleaseMouse(MouseEvent mouseEvent) {
        if (dragged == null) {return;}

        int x = (int) (mouseEvent.getX() / cellWidth);
        int y = (int) (mouseEvent.getY() / cellHeight);

        if (dragged.getType().equals("Obstacle")) {
            ObstacleView obstacleView = (ObstacleView) dragged;
            var newObstacleConfiguration = new ObjectConfiguration(x, y);
            environmentManager.updateObstacle(obstacleView.getObstacle(), newObstacleConfiguration);
        } else {
            RobotView robotView = (RobotView) dragged;
            Robot robot = robotView.getRobot();
            var newObstacleConfiguration = new ObjectConfiguration(x, y,
                    robot.angle(), robot.getVelocity(), robot.getRotationAngle(), robot.getColor());
            environmentManager.updateRobot(robotView.getRobot(), newObstacleConfiguration);
        }

        dragged = null;
    }

    private void handleMoveMouse(MouseEvent mouseEvent) {
        int currCol = (int) (mouseEvent.getX() / cellWidth);
        int currRow = (int) (mouseEvent.getY() / cellHeight);

        if (currCol == previousCol && currRow == previousRow) return;

        // Redraw the canvas to clear the previous highlight
        update();

        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW);
        glow.setRadius(10); // Adjust the radius for the desired glow size
        glow.setSpread(0.05); // How much the glow spreads from its source, value from 0 to 1

        // Set the glow effect
        gc.setEffect(glow);

        // Set a semi-transparent fill color
        gc.setFill(Color.rgb(255, 255, 0, 0.1)); // Adjust transparency as needed

        gc.fillRect(cellWidth * currCol, cellHeight * currRow, cellWidth, cellHeight);
        gc.setEffect(null);
    }

    private void handleEnterMouse(MouseEvent mouseEvent) {
        previousCol = (int) (mouseEvent.getX() / cellWidth);
        previousRow = (int) (mouseEvent.getY() / cellHeight);
    }

    private void handleExitMouse(MouseEvent mouseEvent) {
        update();
    }

    public GraphicsContext getContext() {return gc;}
    public double getCellWidth() {return cellWidth;}
    public double getCellHeight() {return cellHeight;}

}
