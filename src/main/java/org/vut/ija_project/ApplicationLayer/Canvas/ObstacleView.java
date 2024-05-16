package org.vut.ija_project.ApplicationLayer.Canvas;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Obstacle.Obstacle;

public class ObstacleView implements SelectableView, DraggableView {
    private final double scaledImageWidth;
    private final double scaledImageHeight;
    private Obstacle obstacle;
    private CanvasView canvasView;
    private EnvironmentManager environmentManager;
    private final Image obstacleImage;
    private boolean selected;
    private double currPixelX;
    private double currPixelY;

    public ObstacleView(Obstacle obstacle, CanvasView canvasView, EnvironmentManager environmentManager) {
        this.obstacle = obstacle;
        this.canvasView = canvasView;
        this.environmentManager = environmentManager;
        this.obstacleImage = new Image(getClass().getResourceAsStream("/img/bush.png"));
        this.selected = false;

        double imageScaleX = canvasView.getCellWidth() / obstacleImage.getWidth();
        double imageScaleY = canvasView.getCellHeight() / obstacleImage.getHeight();
        double scaleFactor = Math.min(imageScaleY, imageScaleX);

        scaledImageWidth = obstacleImage.getWidth() * scaleFactor;
        scaledImageHeight = obstacleImage.getHeight() * scaleFactor;

        update();
    }

    public void update() {
        Position obstaclePos = obstacle.getPosition();
        // top-left pixel coordinates for the given obstacle's position
        currPixelX = canvasView.getCellWidth()  * obstaclePos.getX();
        currPixelY = canvasView.getCellHeight() * obstaclePos.getY();

        double imageScaleX = canvasView.getCellWidth() / obstacleImage.getWidth();
        double imageScaleY = canvasView.getCellHeight() / obstacleImage.getHeight();
        double scaleFactor = Math.min(imageScaleY, imageScaleX);

        // Center the image in the cell by offsetting the position
        // by half the difference of the cell size and the image size (after scaling)
        double scaledImageWidth = obstacleImage.getWidth() * scaleFactor;
        double scaledImageHeight = obstacleImage.getHeight() * scaleFactor;
        double offsetX = (canvasView.getCellWidth() - scaledImageWidth) / 2;
        double offsetY = (canvasView.getCellHeight() - scaledImageHeight) / 2;

        if (selected) {
            DropShadow glow = new DropShadow();
            glow.setColor(Color.GREEN);
            glow.setRadius(15);
            glow.setSpread(0.1);
            // Set the glow effect
            canvasView.getContext().setEffect(glow);
        }

        canvasView.getContext().drawImage(obstacleImage,
                currPixelX + offsetX, currPixelY + offsetY, scaledImageWidth, scaledImageHeight);
        canvasView.getContext().setEffect(null);
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    @Override
    public boolean isClicked(double x, double y) {
        boolean withinHorizontalBoundary =
                x >= currPixelX && x <= (currPixelX + scaledImageWidth);
        boolean withinVerticalBoundary =
                y >= currPixelY && y <= (currPixelY + scaledImageHeight);

        return  withinHorizontalBoundary && withinVerticalBoundary;
    }

    public Obstacle getObstacle() {return obstacle;}

    @Override
    public void drawAt(double x, double y) {
        //center the obstacle's image
        double offsetX = -scaledImageWidth / 2;
        double offsetY = -scaledImageHeight / 2;

        DropShadow glow = new DropShadow();
        glow.setColor(Color.GREEN);
        glow.setRadius(15);
        glow.setSpread(0.1);
        // Set the glow effect
        canvasView.getContext().setEffect(glow);

        canvasView.getContext().drawImage(obstacleImage,
                x + offsetX, y + offsetY, scaledImageWidth, scaledImageHeight);
        canvasView.getContext().setEffect(null);
    }

    @Override
    public String getType() {return "Obstacle";}
}
