package com.example.notewizard.detection.windowing;

/*
This code will modify the values in the audio buffer by multiplying them with the corresponding Hamming window coefficients.
The Hamming window function is defined by the equation: w(n) = 0.54 - 0.46 * cos(2πn / (N - 1)),
where n is the index of the sample and N is the length of the buffer.

By applying the Hamming window, you can reduce spectral leakage, which can improve the accuracy of frequency
analysis
 */
public class HammingWindow {
    public void applyWindow(double[] buffer) {
        int length = buffer.length;
        for (int i = 0; i < length; i++) {
            buffer[i] *= hamming(i, length);
        }
    }

    private double hamming(int index, int length) {
        return 0.54f - 0.46f * Math.cos(2 * Math.PI * index / (length - 1));
    }
}
