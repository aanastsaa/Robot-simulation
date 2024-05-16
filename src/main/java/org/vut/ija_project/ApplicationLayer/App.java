package org.vut.ija_project.ApplicationLayer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.vut.ija_project.BusinessLayer.EnvironmentManager;
import org.vut.ija_project.DataLayer.Environment.Environment;
import org.vut.ija_project.DataLayer.Environment.Room;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        Environment room = Room.create(9, 14);
        EnvironmentManager manager = new EnvironmentManager(room);
        MainView mainView = new MainView(manager);
        mainView.setPadding(new Insets(30, 0, 0, 0));
        MenuView menuView = new MenuView(manager, primaryStage);

        BorderPane root = new BorderPane();
        root.setTop(menuView); // Set MenuBar at the top
        root.setCenter(mainView); // MainView as the central content

        Scene scene = new Scene(root, 1500, 760);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Bugs world");
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Throwable t) {
            if (t.getCause() != null) {
                t.getCause().printStackTrace();
            } else {
                t.printStackTrace();
            }
        }
    }
}
