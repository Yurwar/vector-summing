package com.yurwar;

public class VectorAdderRunnable implements Runnable {
    private final int[] v1;
    private final int[] v2;
    private final int[] resV;
    private final int chunkSize;
    private final int startIndex;

    public VectorAdderRunnable(int[] v1, int[] v2, int[] resV, int chunkSize, int startIndex) {
        this.v1 = v1;
        this.v2 = v2;
        this.resV = resV;
        this.chunkSize = chunkSize;
        this.startIndex = startIndex;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < startIndex + chunkSize || i < v1.length; i++) {
            resV[i] = v1[i] + v2[i];
        }
    }
}
