package com.example.notewizard.model.guitarmodel;

public class Note {
    private String stringName;
    private int stringValue;
    private int fret;
    private double frequency;

    public Note(String stringName, int stringValue, int fret, double frequency) {
        this.stringName = stringName;
        this.stringValue = stringValue;
        this.fret = fret;
        this.frequency = frequency;
    }

    public String getStringName() {
        return stringName;
    }

    public void setStringName(String stringName) {
        this.stringName = stringName;
    }

    public int getStringValue() {
        return stringValue;
    }

    public void setStringValue(int stringValue) {
        this.stringValue = stringValue;
    }

    public int getFret() {
        return fret;
    }

    public void setFret(int fret) {
        this.fret = fret;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Note{" +
                "stringName='" + stringName + '\'' +
                ", stringValue=" + stringValue +
                ", fret=" + fret +
                ", frequency=" + frequency +
                '}';
    }
}
