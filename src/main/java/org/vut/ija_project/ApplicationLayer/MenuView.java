package org.vut.ija_project.ApplicationLayer;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;

import java.io.File;

public class MenuView extends MenuBar {
    private final Stage stage;
    private File chosenFile;
    private EnvironmentManager environmentManager;
    public MenuView(EnvironmentManager environmentManager, Stage stage) {
        this.environmentManager = environmentManager;
        this.stage = stage;

        Menu fileMenu = new Menu("File");
        MenuItem openEnvMenu = new MenuItem("Open existing environment");
        MenuItem saveEnvMenu = new MenuItem("Save current environment");
        fileMenu.getItems().addAll(openEnvMenu, saveEnvMenu);

        openEnvMenu.setOnAction(this::openFile);
        saveEnvMenu.setOnAction(this::saveEnvironment);

        this.getMenus().add(fileMenu);
    }

    private void saveEnvironment(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File to save environment to");

        chosenFile = fileChooser.showSaveDialog(stage);

        if (chosenFile != null) {
            environmentManager.exportEnvironmentToCsv(chosenFile);
        }
    }

    private void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Environment File");

        chosenFile = fileChooser.showOpenDialog(stage);

        if (chosenFile != null) {
            environmentManager.getEnvironmentFromFile(chosenFile);
        }
    }
}
