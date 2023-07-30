package com.example.notewizard.detection.audioprocessing;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import javax.sound.sampled.LineUnavailableException;
import java.util.LinkedList;
import java.util.Queue;

public class AudioProcessorHandlerTarsosDSP implements AudioProcessorHandler {
    private AudioDispatcher audioDispatcher;
    private final PitchDetectionHandler pitchDetectionHandler;
    private Queue<Double> pitchQueue = new LinkedList<>();
    private static final int QUEUE_SIZE = 10;
    private static final int SAMPLE_RATE = 44100; //44.1 kHz
    private static final int BUFFER_SIZE = 4096;

    public AudioProcessorHandlerTarsosDSP(PitchListener pitchListener) {
        pitchDetectionHandler = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                double pitch = pitchDetectionResult.getPitch();
                double probability = pitchDetectionResult.getProbability();
                double threshold = 0.9f;
                if (pitch > 20 && pitch < 20000 && probability > threshold) {
                    if (pitchQueue.size() == QUEUE_SIZE) {
                        pitchQueue.poll();
                    }
                    pitchQueue.add(pitch);

                    double sum = 0;
                    for (double p : pitchQueue) {
                        sum += p;
                    }
                    double averagePitch = sum / pitchQueue.size();
                    pitchListener.onPitchDetected(averagePitch);
                }
            }
        };
    }

    @Override
    public void startProcessing() {
        try {
            audioDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(BUFFER_SIZE, BUFFER_SIZE / 2);
            AudioProcessor pitchProcessor = new PitchProcessor(
                    PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
                    SAMPLE_RATE,
                    BUFFER_SIZE,
                    pitchDetectionHandler
            );
            audioDispatcher.addAudioProcessor(pitchProcessor);
            new Thread(audioDispatcher, "Audio Dispatcher").start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopProcessing() {
        if (audioDispatcher != null) {
            audioDispatcher.stop();
        }
    }
}