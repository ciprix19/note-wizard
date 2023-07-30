package com.example.notewizard.detection.audioprocessing;

import com.example.notewizard.detection.algorithms.Fft;
import com.example.notewizard.detection.windowing.HammingWindow;

import javax.sound.sampled.LineUnavailableException;
import java.util.LinkedList;
import java.util.Queue;

public class AudioProcessorHandlerFFT implements AudioProcessorHandler {
    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 2048;
    private static final int QUEUE_SIZE = 10;
    private Queue<Double> pitchQueue = new LinkedList<>();
    private AudioInput audioInput;
    private final HammingWindow hammingWindow = new HammingWindow();

    private boolean processing = false;
    private final PitchListener pitchListener;

    public AudioProcessorHandlerFFT(PitchListener pitchListener) {
        this.pitchListener = pitchListener;
    }

    private void processAudioInput() {
        double[] audioData = audioInput.readAudioData();
        hammingWindow.applyWindow(audioData);
        double pitch = Fft.detectPitch(audioData);
        if (pitch > 20 && pitch < 20000) {
            if (pitchQueue.size() == QUEUE_SIZE) {
                pitchQueue.poll();
            }
            pitchQueue.add(pitch);

            double sum = 0;
            for (double p : pitchQueue) {
                sum += p;
            }
            double averagePitch = sum / pitchQueue.size();
            System.out.println(pitch);
            pitchListener.onPitchDetected(averagePitch);
        }
    }

    @Override
    public void startProcessing() {
        try {
            processing = true;
            audioInput = new AudioInput(BUFFER_SIZE, SAMPLE_RATE);
            new Thread(() -> {
                while (processing) {
                    processAudioInput();
                }
            }, "Audio Processor").start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopProcessing() {
        processing = false;
        if (audioInput != null) {
            audioInput.close();
        }
    }
}
