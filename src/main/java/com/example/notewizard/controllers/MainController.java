package com.example.notewizard.controllers;

import com.example.notewizard.TunerApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static javafx.event.Event.fireEvent;

public class MainController {
    @FXML
    private Button homeButton;

    @FXML
    private Button tunerButton;

    @FXML
    private Button tabWriterButton;

    @FXML
    private Pane secondPane;

    @FXML
    public void initialize() {
        loadHome();
    }

    public void loadHome() {
        try {
            secondPane.getChildren().clear();
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/com/example/notewizard/views/home-view.fxml"));
            secondPane.getChildren().add(newLoadedPane);
            secondPane.fireEvent(new ViewChangeEvent("home-view"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTuner() {
        try {
            secondPane.getChildren().clear();
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/com/example/notewizard/views/tuner-view.fxml"));
            secondPane.getChildren().add(newLoadedPane);
            secondPane.fireEvent(new ViewChangeEvent("tuner-view"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWriter() {
        try {
            secondPane.getChildren().clear();
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource("/com/example/notewizard/views/writer-view.fxml"));
            secondPane.getChildren().add(newLoadedPane);
            secondPane.fireEvent(new ViewChangeEvent("writer-view"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
