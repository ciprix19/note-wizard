package com.example.notewizard.controllers;

import com.example.notewizard.detection.audioprocessing.*;
import com.example.notewizard.model.dto.TuningOption;
import com.example.notewizard.updaters.WriterUpdater;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class WriterController {
    private static AudioProcessorHandler audioProcessorHandler; // static ca nu vrea altfel :))
    private WriterUpdater writerUpdater;

    @FXML
    private Button startRecordingButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button clearButton;
    @FXML
    private ComboBox<TuningOption> tuningOptionComboBox;
    @FXML
    private ComboBox<String> startingStringComboBox;
    @FXML
    private ComboBox<String> modeOptionComboBox;
    @FXML
    private TextArea tabTextArea;
    @FXML
    private Label feedbackLabel;

    @FXML
    public void initialize() {
        //todo IDEE: textarea editabil dupa ce se opreste recordingul: se pot adauga h, p, s, bend ...
        tuningOptionComboBox.getItems().addAll(
                // E2 A2 D3 G3 B3 E4 - standard tuning
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
        startingStringComboBox.getItems().addAll(
                "e",
                "B",
                "G",
                "D",
                "A",
                "E"
        );
        modeOptionComboBox.getItems().addAll("Arpeggio", "Ascending Scale", "Descending Scale");
        writerUpdater = new WriterUpdater(tabTextArea, feedbackLabel, modeOptionComboBox.getValue());
        setTuning();
        audioProcessorHandler = new AudioProcessorHandlerTarsosDSP(writerUpdater);
        startRecordingButton.setDisable(true);
    }

    public void startRecording() {
        if (Objects.equals(startRecordingButton.getText(), "Start Recording") && startingStringComboBox.getValue() != null) {
            startRecordingButton.setText("Stop Recording");
            exportButton.setDisable(true);
            clearButton.setDisable(true);
            audioProcessorHandler.startProcessing();
        } else {
            tabTextArea.setEditable(true);
            startRecordingButton.setText("Start Recording");
            exportButton.setDisable(false);
            clearButton.setDisable(false);
            audioProcessorHandler.stopProcessing();
        }
    }

    public void setStartingString() {
        feedbackLabel.setText(startingStringComboBox.getValue() + " string chosen as starting string");
        writerUpdater.setStartingString(startingStringComboBox.getValue());
        startRecordingButton.setDisable(false);
    }

    public void setTuning() {
        writerUpdater.setTuningOption(tuningOptionComboBox.getValue());
        startingStringComboBox.getItems().clear();
        startingStringComboBox.getItems().addAll(
                tuningOptionComboBox.getValue().getStringList().get(5),
                tuningOptionComboBox.getValue().getStringList().get(4),
                tuningOptionComboBox.getValue().getStringList().get(3),
                tuningOptionComboBox.getValue().getStringList().get(2),
                tuningOptionComboBox.getValue().getStringList().get(1),
                tuningOptionComboBox.getValue().getStringList().get(0)
        );
        tuningOptionComboBox.setValue(tuningOptionComboBox.getItems().get(0));
    }

    public void setModeOption() {
        writerUpdater.setModeOption(modeOptionComboBox.getValue());
    }

    public void clearTab() {
        tabTextArea.setEditable(false);
        writerUpdater.initializeTab();
    }

    public void exportTo() throws IOException {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        Stage stage = (Stage) exportButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(tabTextArea.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
