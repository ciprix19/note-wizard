package com.example.notewizard.model.dto;

import com.example.notewizard.detection.audioprocessing.AudioProcessorHandler;

public class DetectionOption {
    private String detectionName;
    AudioProcessorHandler audioProcessorHandler;

    public DetectionOption(String detectionName, AudioProcessorHandler audioProcessorHandler) {
        this.detectionName = detectionName;
        this.audioProcessorHandler = audioProcessorHandler;
    }

    public String getDetectionName() {
        return detectionName;
    }

    public void setDetectionName(String detectionName) {
        this.detectionName = detectionName;
    }

    public AudioProcessorHandler getAudioProcessorHandler() {
        return audioProcessorHandler;
    }

    public void setAudioProcessorHandler(AudioProcessorHandler audioProcessorHandler) {
        this.audioProcessorHandler = audioProcessorHandler;
    }

    @Override
    public String toString() {
        return detectionName;
    }
}
