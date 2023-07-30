package com.example.notewizard.updaters;

import com.example.notewizard.detection.audioprocessing.PitchListener;
import com.example.notewizard.model.dto.TuningOption;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class TunerUpdater implements PitchListener {
    private final ObservableList<Circle> stringsCircleList;
    private final ComboBox<TuningOption> tuningOptionComboBox;
    private final Label freqIndicatorText;

    public TunerUpdater(ObservableList<Circle> stringsCircleList, ComboBox<TuningOption> tuningOptionComboBox, Label freqIndicatorText) {
        this.stringsCircleList = stringsCircleList;
        this.tuningOptionComboBox = tuningOptionComboBox;
        this.freqIndicatorText = freqIndicatorText;
    }

    @Override
    public void onPitchDetected(double pitch) {
        Platform.runLater(() -> {
            freqIndicatorText.setText(String.valueOf(pitch));

            List<Double> frequencies = tuningOptionComboBox.getValue().getFrequencies();

            double minDifference = Double.MAX_VALUE;
            int closestStringIndex = -1;

            for (int i = 0; i < frequencies.size(); i++) {
                double difference = Math.abs(frequencies.get(i) - pitch);
                if (difference < minDifference) {
                    minDifference = difference;
                    closestStringIndex = i;
                }
            }

            for (Circle circle : stringsCircleList) {
                circle.setFill(Color.web("1E1E1E"));
            }

            if (closestStringIndex != -1) {
                double closestFrequency = frequencies.get(closestStringIndex);
                int difference = (int) Math.round(pitch - closestFrequency);
                String differenceText;
                stringsCircleList.get(closestStringIndex).setFill(Color.RED);
                if (difference > 0) {
                    differenceText = "+" + difference;
                } else {
                    differenceText = String.valueOf(difference);
                    if (difference == 0) {
                        stringsCircleList.get(closestStringIndex).setFill(Color.LIME);
                    }
                }
                freqIndicatorText.setText(differenceText);
            }
        });
    }
}

