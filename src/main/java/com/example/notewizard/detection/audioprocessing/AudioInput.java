package com.example.notewizard.detection.audioprocessing;
import javax.sound.sampled.*;

public class AudioInput {
    private final TargetDataLine line;
    private final int bufferSize;

    public AudioInput(int bufferSize, float sampleRate) throws LineUnavailableException {
        this.bufferSize = bufferSize;
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        TargetDataLine.Info info = new TargetDataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Line not supported");
        }
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
    }

    public double[] readAudioData() {
        byte[] buffer = new byte[bufferSize * 2];  // two bytes per audio frame for 16 bit stereo
        double[] audioData = new double[bufferSize];
        if(line.read(buffer, 0, buffer.length) > 0) {
            for (int i = 0, j = 0; i < buffer.length; i+=2, j++) {
                // convert two bytes into a Java double, as the audio frame is 16 bits
                int combinedBytesAsInt = (buffer[i+1] << 8) | buffer[i & 0xFF]; // for converting to range -1, 1
                audioData[j] = combinedBytesAsInt / 32768.0;
            }
        }
        return audioData;
    }

    public void close() {
        line.close();
    }
}