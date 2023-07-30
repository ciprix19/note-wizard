package com.example.notewizard.model.guitarmodel;

import com.example.notewizard.model.dto.TuningOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuitarNotes {
    private final HashMap<String, HashMap<Integer, Note>> guitarNotes;
    private static final int NUMBER_OF_FRETS = 22;
    private static final double SEMITONE_RATIO = Math.pow(2, 1.0 / 12.0);

    public GuitarNotes(TuningOption tuningOption) {
        List<String> tuningStrings = tuningOption.getStringList();
        List<Double> tuningFreqs = tuningOption.getFrequencies();

        guitarNotes = new HashMap<>();
        // Populating frequencies for each string and each fret
        for (int i = 0; i < 6; i++) {
            String stringName = tuningStrings.get(i);
            double openStringFreq = tuningFreqs.get(i);
            guitarNotes.put(stringName, populateFrequencies(stringName, i, openStringFreq));
        }
    }

    private HashMap<Integer, Note> populateFrequencies(String stringName, int stringValue, double openStringFreq) {
        HashMap<Integer, Note> stringFrequencies = new HashMap<>();
        for (int i = 0; i <= NUMBER_OF_FRETS; i++) {
            double freq = openStringFreq * Math.pow(SEMITONE_RATIO, i);
            stringFrequencies.put(i, new Note(stringName, stringValue, i, freq));
        }
        return stringFrequencies;
    }

    public Note getNote(String string, int fret) {
        return guitarNotes.get(string).get(fret);
    }

    public List<String> getGuitarStrings() {
        return new ArrayList<>(guitarNotes.keySet());
    }
}