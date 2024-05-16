package org.vut.ija_project.ApplicationLayer.SelectedView;

import org.vut.ija_project.ApplicationLayer.Canvas.ObstacleView;
import org.vut.ija_project.ApplicationLayer.Canvas.RobotView;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.DataLayer.Robot.RobotType;

public class SelectedViewFactory {
    public static SelectedView create(RobotView robotView, EnvironmentManager environmentManager) {
        SelectedView selectedView = null;
        if (robotView.getRobot().getType() == RobotType.AUTONOMOUS) {
            selectedView = new SelectedViewAutonomousRobot(robotView, environmentManager);
        } else if (robotView.getRobot().getType() == RobotType.CONTROLLABLE) {
            selectedView = new SelectedViewControlledRobot(robotView, environmentManager);
        }
        return  selectedView;
    }

    public static SelectedView create(ObstacleView obstacleView, EnvironmentManager environmentManager) {
        return new SelectedViewObstacle(obstacleView, environmentManager);
    }
}
