package com.example.notewizard.controllers;

import com.example.notewizard.detection.audioprocessing.*;
import com.example.notewizard.model.dto.DetectionOption;
import com.example.notewizard.model.dto.TuningOption;
import com.example.notewizard.updaters.TunerUpdater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.util.*;

public class TunerController {
    ObservableList<Label> stringsTextList = FXCollections.observableArrayList();
    ObservableList<Circle> stringsCircleList = FXCollections.observableArrayList();
    private static AudioProcessorHandler audioProcessorHandler;
    private static boolean isHandlerAdded = false;

    @FXML
    private ComboBox<TuningOption> tuningOptionComboBox;
    @FXML
    private ComboBox<DetectionOption> detectionAlgorithmComboBox;

    @FXML
    private StackPane guitarImageStackPane;

    @FXML
    private Pane parentPane;

    @FXML
    private ImageView guitarImageView;

    @FXML
    private Circle string1circle;
    @FXML
    private Circle string2circle;
    @FXML
    private Circle string3circle;
    @FXML
    private Circle string4circle;
    @FXML
    private Circle string5circle;
    @FXML
    private Circle string6circle;

    @FXML
    private Label string1text;
    @FXML
    private Label string2text;
    @FXML
    private Label string3text;
    @FXML
    private Label string4text;
    @FXML
    private Label string5text;
    @FXML
    private Label string6text;
    @FXML
    private Label freqIndicatorText;

    @FXML
    public void initialize() {
        // initialize circles
        stringsTextList.addAll(string1text, string2text, string3text, string4text, string5text, string6text);
        stringsCircleList.addAll(string1circle, string2circle, string3circle, string4circle, string5circle, string6circle);
        tuningOptionComboBox.getItems().addAll(
                new TuningOption("Standard", Arrays.asList("E", "A", "D", "G", "B", "e"), Arrays.asList(82.41, 110.00, 146.83, 196.00, 246.94, 329.63)),
                new TuningOption("Drop D", Arrays.asList("D", "A", "D", "G", "B", "E"), Arrays.asList(73.42, 110.00, 146.83, 196.00, 246.94, 329.63)),
                new TuningOption("D# Standard", Arrays.asList("D#", "G#", "C#", "F#", "A#", "d#"), Arrays.asList(77.78, 103.83, 138.59, 184.99, 233.08, 311.13)),
                new TuningOption("D Standard", Arrays.asList("D", "G", "C", "F", "A", "d"), Arrays.asList(73.42, 98.00, 130.81, 174.61, 220.00, 293.66)),
                new TuningOption("Open Dmadd9", Arrays.asList("D", "A", "D", "F", "A", "E"), Arrays.asList(73.42, 110.00, 146.83, 174.61, 220.00, 329.63)),
                new TuningOption("Drop C", Arrays.asList("C", "G", "C", "F", "A", "D"), Arrays.asList(65.00, 98.00, 131.00, 175.00, 220.00, 294.00)),
                new TuningOption("C# Standard", Arrays.asList("C#", "F#", "B", "E", "G#", "c#"), Arrays.asList(69.30, 92.50, 123.47, 164.81, 207.65, 277.18)),
                new TuningOption("C Standard", Arrays.asList("C", "F", "Bb", "Eb", "G", "c"), Arrays.asList(65.41, 87.31, 116.54, 155.56, 196.00, 261.63)),
                new TuningOption("Drop A", Arrays.asList("A", "A", "D", "G", "B", "E"), Arrays.asList(55.00, 110.00, 146.83, 196.00, 246.94, 329.63))
        );
        tuningOptionComboBox.setValue(tuningOptionComboBox.getItems().get(0));
        setTuning();

        // initialize detection algorithms
        TunerUpdater tunerUpdater = new TunerUpdater(stringsCircleList, tuningOptionComboBox, freqIndicatorText);
        detectionAlgorithmComboBox.getItems().addAll(
                new DetectionOption("TarsosDSP", new AudioProcessorHandlerTarsosDSP(tunerUpdater)),
                new DetectionOption("FFT", new AudioProcessorHandlerFFT(tunerUpdater))
        );
        detectionAlgorithmComboBox.setValue(detectionAlgorithmComboBox.getItems().get(0));
        setDetection();
        if (!isHandlerAdded) {
            parentPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.addEventHandler(ViewChangeEvent.VIEW_CHANGE_EVENT, this::handleViewSwitch);
                }
            });
            isHandlerAdded = true;
        }
    }

    private void handleViewSwitch(ViewChangeEvent event) {
        System.out.println(event.getViewName());
        if (!event.getViewName().equals("tuner-view")) {
            audioProcessorHandler.stopProcessing();
            System.out.println("processing stopped");
        }
    }

    public void setTuning() {
        for (int i = 0; i < 6; i++) {
            stringsTextList.get(i).setText(tuningOptionComboBox.getValue().getStringList().get(i));
        }
    }

    public void setDetection() {
        if (audioProcessorHandler != null) {
            audioProcessorHandler.stopProcessing();
        }
        audioProcessorHandler = detectionAlgorithmComboBox.getValue().getAudioProcessorHandler();
        audioProcessorHandler.startProcessing();
    }
}
