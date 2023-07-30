package com.example.notewizard.updaters;

import com.example.notewizard.detection.audioprocessing.PitchListener;
import com.example.notewizard.model.guitarmodel.GuitarNotes;
import com.example.notewizard.model.guitarmodel.Note;
import com.example.notewizard.model.guitarmodel.Tab;
import com.example.notewizard.model.dto.TuningOption;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class WriterUpdater implements PitchListener {
    private String startingString;
    private Tab tab;
    private TuningOption tuningOption;
    private GuitarNotes guitarNotes;
    private boolean firstStringCase = true;
    private final Queue<Double> pitchQueue = new LinkedList<>();
    private final List<Double> pitchList = new ArrayList<>();
    private int stepIncrementor = 3;
    private Note lastNote = null;
    String modeOption;

    @FXML
    private final TextArea tabTextArea;
    @FXML
    private final Label feedbackLabel;

    public WriterUpdater(TextArea tabTextArea, Label feedbackLabel, String modeOption) {
        this.tabTextArea = tabTextArea;
        this.feedbackLabel = feedbackLabel;
        this.modeOption = modeOption;
    }

    public void initializeTab() {
        tabTextArea.clear();
        tab = new Tab(tuningOption);
        guitarNotes = new GuitarNotes(tuningOption);
        tabTextArea.setText(tab.getTab());
        stepIncrementor = 3;
    }

    private final int QUEUE_DURATION_MS = 800; // Duration of queue in milliseconds
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledTask;

    @Override
    public void onPitchDetected(double pitch) {
        Platform.runLater(() -> {
            // Add the detected pitch to the queue
            pitchList.add(pitch);

            // If a timer isn't already set, start one
            if (scheduledTask == null || scheduledTask.isDone()) {
                // Start a new timer
                scheduledTask = scheduler.schedule(this::processList, QUEUE_DURATION_MS, TimeUnit.MILLISECONDS);
                pitchList.clear();
            }
        });
    }

    public void processList() {
        Platform.runLater(() -> {
            // Calculate the mode (most frequent value) of the queue
            double modePitch = calculateModePitch();
            Note note;
            if (firstStringCase) {
                note = findRootNote(modePitch);
            } else {
                if (Objects.equals(modeOption, "Arpeggio")) {
                    note = findArpeggioNote(modePitch, lastNote);
                } else {
                    note = findScaleNote(modePitch, lastNote);
                }
            }

            if (note != null) {
                System.out.println("Found:" + note + " " + modePitch);
                lastNote = note;
                updateTextArea(note);
                firstStringCase = false;
            }
            pitchQueue.clear();
        });
    }

    public Note findRootNote(double pitch) {
        for (int fret = 0; fret <= 22; fret++) {
            Note note = guitarNotes.getNote(startingString, fret);
            if (Math.abs(note.getFrequency() - pitch) / note.getFrequency() < 0.05) { // 5% tolerance
                return note;
            }
        }
        return null;
    }

    public Note findArpeggioNote(double pitch, Note lastNote) {
        Note closestNote = null;
        int maxFretDifference = 2;
        double smallestDiff = Double.MAX_VALUE;
        List<String> strings = guitarNotes.getGuitarStrings();
        for (int i = 0; i < strings.size(); i++) {
            if (!strings.get(i).equals(lastNote.getStringName())) {
                closestNote = searchOnString(strings.get(i), pitch, lastNote, closestNote, maxFretDifference, smallestDiff);
                if (closestNote != null) {
                    smallestDiff = Math.abs(closestNote.getFrequency() - pitch);
                }
            }
        }
        // same string
        closestNote = searchOnString(lastNote.getStringName(), pitch, lastNote, closestNote, maxFretDifference, smallestDiff);
        if (smallestDiff / pitch < 0.05) {  // 5% tolerance
            return closestNote;
        } else {
            return null;
        }
    }

    public Note findScaleNote(double pitch, Note lastNote) {
        Note closestNoteSameString = null;
        Note closestNoteOtherStrings = null;
        double smallestDiffSameString = Double.MAX_VALUE;
        double smallestDiffOtherStrings = Double.MAX_VALUE;
        int maxFretDifference = 4;
        List<String> strings = guitarNotes.getGuitarStrings();
        int currentStringIndex = strings.indexOf(lastNote.getStringName());

        // same string
        closestNoteSameString = searchOnString(lastNote.getStringName(), pitch, lastNote, closestNoteSameString, maxFretDifference, smallestDiffSameString);
        if (closestNoteSameString != null) {
            smallestDiffSameString = Math.abs(closestNoteSameString.getFrequency() - pitch);
        }
        if (Objects.equals(modeOption, "Ascending Scale")) {
            // the other strings (down)
            for (int i = currentStringIndex - 1; i >= 0; i--) {
                closestNoteOtherStrings = searchOnString(strings.get(i), pitch, lastNote, closestNoteOtherStrings, maxFretDifference, smallestDiffOtherStrings);
                if (closestNoteOtherStrings != null) {
                    smallestDiffOtherStrings = Math.abs(closestNoteOtherStrings.getFrequency() - pitch);
                }
            }
            // the other strings (up)
            for (int i = currentStringIndex + 1; i < strings.size(); i++) {
                closestNoteOtherStrings = searchOnString(strings.get(i), pitch, lastNote, closestNoteOtherStrings, maxFretDifference, smallestDiffOtherStrings);
                if (closestNoteOtherStrings != null) {
                    smallestDiffOtherStrings = Math.abs(closestNoteOtherStrings.getFrequency() - pitch);
                }
            }
        } else {
            // the other strings (up)
            for (int i = currentStringIndex + 1; i < strings.size(); i++) {
                closestNoteOtherStrings = searchOnString(strings.get(i), pitch, lastNote, closestNoteOtherStrings, maxFretDifference, smallestDiffOtherStrings);
                if (closestNoteOtherStrings != null) {
                    smallestDiffOtherStrings = Math.abs(closestNoteOtherStrings.getFrequency() - pitch);
                }
            }
            // the other strings (down)
            for (int i = currentStringIndex - 1; i >= 0; i--) {
                closestNoteOtherStrings = searchOnString(strings.get(i), pitch, lastNote, closestNoteOtherStrings, maxFretDifference, smallestDiffOtherStrings);
                if (closestNoteOtherStrings != null) {
                    smallestDiffOtherStrings = Math.abs(closestNoteOtherStrings.getFrequency() - pitch);
                }
            }
        }
        // compare and return the note that is more appropriate (frequency wise) to the last note
        if (smallestDiffSameString / pitch < 0.05 && (closestNoteOtherStrings == null || smallestDiffSameString <= smallestDiffOtherStrings)) {
            return closestNoteSameString;
        } else if (closestNoteOtherStrings != null && smallestDiffOtherStrings / pitch < 0.05) {
            return closestNoteOtherStrings;
        } else {
            return null;
        }
    }

    private Note searchOnString(String string, double pitch, Note lastNote, Note closestNote, int maxFretDifference, double smallestDiff) {
        int startFret = Math.max(0, lastNote.getFret() - maxFretDifference);
        int endFret = Math.min(22, lastNote.getFret() + maxFretDifference);

        for (int fret = startFret; fret <= endFret; fret++) {
            Note note = guitarNotes.getNote(string, fret);
            double diff = Math.abs(note.getFrequency() - pitch);

            if (diff < smallestDiff) {
                smallestDiff = diff;
                closestNote = note;
            }
        }
        return closestNote;
    }

    private double calculateModePitch() {
        List<Integer> intList = new ArrayList<>();
        for (Double d : pitchList) {
            int convertedInt = d.intValue();
            intList.add(convertedInt);
        }
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (Integer number : intList) {
            frequencyMap.put(number, frequencyMap.getOrDefault(number, 0) + 1);
        }

        int mostFrequentNumber = 0;
        int highestFrequency = 0;

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            int number = entry.getKey();
            int frequency = entry.getValue();

            if (frequency > highestFrequency) {
                highestFrequency = frequency;
                mostFrequentNumber = number;
            }
        }
        return mostFrequentNumber;
    }

    private void updateTextArea(Note note) {
        tab.addNote(note, stepIncrementor++);
        tabTextArea.setText(tab.getTab());
    }

    public void setStartingString(String startingString) {
        this.startingString = startingString;
    }

    public void setTuningOption(TuningOption tuningOption) {
        this.tuningOption = tuningOption;
        tab = new Tab(tuningOption);
        guitarNotes = new GuitarNotes(tuningOption);
        tabTextArea.setText(tab.getTab());
    }

    public void setModeOption(String option) {
        this.modeOption = option;
    }
}
