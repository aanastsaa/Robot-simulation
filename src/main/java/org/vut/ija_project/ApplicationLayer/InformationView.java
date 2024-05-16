package org.vut.ija_project.ApplicationLayer;

import javafx.scene.layout.VBox;
import org.vut.ija_project.ApplicationLayer.SelectedView.SelectedView;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.DataLayer.Robot.RobotType;

public class InformationView extends VBox {
    private final EnvironmentManager environmentManager;
    private SelectedView selectedView;

    public InformationView(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
        this.setSpacing(10);
    }

    public void setSelected(SelectedView selectedView) {
        this.getChildren().clear();
        this.selectedView = selectedView;
        if (this.selectedView == null) return;

        this.selectedView.requestFocus();
        this.selectedView.update();
        this.getChildren().add(selectedView);
    }

    public void update() {
        //because Events and UI rendering are made in same thread, events in controlled robot
        //are not registered in time, after many attempts decided to not update controlled robot info
        //during simulation, so it could be controllable
        if (selectedView == null) this.getChildren().clear();
        if (selectedView == null || selectedView.getType() == RobotType.CONTROLLABLE) return;

        this.getChildren().clear();
        //if there is no selected view don't display anything
        selectedView.update();
        this.getChildren().add(selectedView);
    }
}
