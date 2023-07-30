package com.example.notewizard.model.guitarmodel;

import com.example.notewizard.model.dto.TuningOption;

public class Tab {
    private final int strings = 6;
    private final int steps = 100;
    private final String[][] tab;
    private int initialLength = 8;

    public Tab(TuningOption tuningOption) {
        tab = new String[strings][steps];
        initializeTab(tuningOption);
    }

    public void initializeTab(TuningOption tuningOption) {
        // initialize the tab with dashes ("-")
        for (int i = 0; i < strings; i++) {
            tab[i][0] = tuningOption.getStringList().get(i);
            tab[i][1] = "|";
            tab[i][2] = "-";
        }
        for (int i = 0; i < strings; i++) {
            for (int j = 3; j < steps; j++) {
                tab[i][j] = "-";
            }
        }
    }

    // add a note to the tab
    public void addNote(Note note, int step) {
        if (step >= initialLength) {
            initialLength += 8;
        }
        tab[note.getStringValue()][step] = Integer.toString(note.getFret());
        if (note.getFret() > 9) {
            for (int i = 0; i < 6; i++) {
                if (i != note.getStringValue()) {
                    tab[i][step] = "--";
                }
            }
        }
    }

    // get the tab as a formatted string
    public String getTab() {
        StringBuilder builder = new StringBuilder();
        for (int i = strings - 1; i >= 0; i--) {
            for (int j = 0; j < initialLength; j++) {
                if (tab[i][j] != null) {
                    builder.append(tab[i][j]).append('-');
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
