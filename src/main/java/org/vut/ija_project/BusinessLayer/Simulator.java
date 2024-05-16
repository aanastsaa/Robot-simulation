package org.vut.ija_project.BusinessLayer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class Simulator {
    private EnvironmentManager environmentManager;
    private Timeline timeline;
    public Simulator(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
        this.timeline = new Timeline(new KeyFrame(Duration.millis(20), this::step));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        this.timeline.play();
    }

    public void stop() {
        this.timeline.stop();
    }

    private void step(ActionEvent actionEvent) {
        this.environmentManager.simulationStep();
    }
}
