package com.example.notewizard.model.dto;

import java.util.List;

public class TuningOption {
    private String tuningName;
    private List<String> stringList;
    private List<Double> frequencies;

    public TuningOption(String tuningName, List<String> stringList, List<Double> frequencies) {
        this.tuningName = tuningName;
        this.stringList = stringList;
        this.frequencies = frequencies;
    }

    public String getTuningName() {
        return tuningName;
    }

    public void setTuningName(String tuningName) {
        this.tuningName = tuningName;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<Double> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(List<Double> frequencies) {
        this.frequencies = frequencies;
    }

    @Override
    public String toString() {
        return tuningName;
    }
}
