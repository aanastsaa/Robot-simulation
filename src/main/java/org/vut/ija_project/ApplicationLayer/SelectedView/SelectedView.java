package org.vut.ija_project.ApplicationLayer.SelectedView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.vut.ija_project.DataLayer.Robot.RobotColor;
import org.vut.ija_project.DataLayer.Robot.RobotType;

public abstract class SelectedView extends VBox {
    protected Button updateButton;
    protected Button deleteButton;

    public abstract void update();

    protected Label createNewInfoLabel(String labelText, int bottomMargin, int fontSize) {
        Label infoLabel = new Label(labelText);
        infoLabel.setFont(new Font("Arial", fontSize));
        VBox.setMargin(infoLabel, new Insets(0, 0, bottomMargin, 10));

        this.getChildren().addAll(infoLabel);
        return infoLabel;
    }

    protected TextField createNewSetField(String fieldText, String fieldPlaceholder) {
        HBox setProperty = new HBox(10);
        VBox.setMargin(setProperty, new Insets(0, 0, 0, 10)); // Adds 15px right margin

        Label labelSetProperty = new Label(fieldText);
        labelSetProperty.setFont(new Font("Arial", 18));

        TextField fieldSetProperty = new TextField();
        HBox.setMargin(fieldSetProperty, new Insets(-7, 5, 15, 0));

        fieldSetProperty.setText(fieldPlaceholder);
        fieldSetProperty.setFont(new Font("Arial", 18));
        fieldSetProperty.setMaxSize(80, 5);
        fieldSetProperty.setFocusTraversable(false);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Allow spacer to take all available space

        setProperty.getChildren().addAll(labelSetProperty, spacer, fieldSetProperty);
        this.getChildren().addAll(setProperty);

        return fieldSetProperty;
    }

    protected void addUpdateDeleteButtons() {
        VBox buttonsBox = new VBox(20);
        VBox.setVgrow(buttonsBox, Priority.ALWAYS);
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);

        updateButton = new Button("Update");
        updateButton.setFont(new Font("Arial", 20));
        updateButton.setStyle("-fx-base: #1e90ff; -fx-text-fill: white;");
        updateButton.setFocusTraversable(false);

        deleteButton = new Button("Delete");
        deleteButton.setFont(new Font("Arial", 20));
        deleteButton.setStyle("-fx-base: #ff301e; -fx-text-fill: white;");
        deleteButton.setFocusTraversable(false);

        VBox.setMargin(deleteButton, new Insets(0, 0, 30, 0));
        buttonsBox.getChildren().addAll(updateButton, deleteButton);

        this.getChildren().addAll(buttonsBox);
    }

    protected ComboBox<String> createNewColorComboBox() {
        HBox comboBoxContainer = new HBox();
        ComboBox<String> colorComboBox = new ComboBox<>();

        colorComboBox.getItems().addAll("Red", "Orange", "Yellow", "Green", "Blue", "Purple", "White");
        colorComboBox.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font("Arial", 20));  // Set the font size to 20
                }
            }
        });

        colorComboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font("Arial", 20));  // Ensure the font size is consistent
                }
            }
        });

        colorComboBox.setFocusTraversable(false);
        comboBoxContainer.getChildren().add(colorComboBox);
        comboBoxContainer.setAlignment(Pos.CENTER);

        this.getChildren().add(comboBoxContainer);
        return colorComboBox;
    }


    protected RobotColor getRobotColor(ComboBox<String> comboBox) {
        return switch (comboBox.getSelectionModel().getSelectedItem()) {
            case "Red" -> RobotColor.RED;
            case "Orange" -> RobotColor.ORANGE;
            case "Yellow" -> RobotColor.YELLOW;
            case "Green" -> RobotColor.GREEN;
            case "Blue" -> RobotColor.BLUE;
            case "Purple" -> RobotColor.PURPLE;
            case "White" -> RobotColor.WHITE;
            default ->
                    throw new IllegalStateException("Unexpected value: "
                            + comboBox.getSelectionModel().getSelectedItem());
        };
    }

    public abstract RobotType getType();
}
