package com.example.ap_project;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.input.KeyEvent;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Timeline timeline;
    @FXML
    private Pane mainPane;
    private Line currentLine;
    private AtomicBoolean spacePressed = new AtomicBoolean(false);

    @FXML
    private void handleButtonPress() {
        currentLine = new Line(100, 250, 100, 250); // Change coordinates as needed
        currentLine.setStrokeWidth(3);
        mainPane.getChildren().add(currentLine);
        mainPane.addEventFilter(KeyEvent.KEY_PRESSED,event -> {
            if (event.getCode() == KeyCode.SPACE) {
                double newEndY = currentLine.getEndY() - 2; // Adjust length as needed
                currentLine.setEndY(newEndY);
            }
        });
    }

    @FXML
    private void increaseLength(){
        double newEndY = currentLine.getEndY() - 2; // Adjust length as needed
        currentLine.setEndY(newEndY);
    }

    private void rotateLine(){
        currentLine.setEndY(250);
        currentLine.setEndX(currentLine.getEndY()-150);
    }
    @FXML
    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}