package com.yurwar;

import java.util.stream.IntStream;

public class VectorAdderThread extends Thread {
    private final int[] v1;
    private final int[] v2;
    private int[] resV;

    public VectorAdderThread(int[] v1, int[] v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public void run() {
        resV = IntStream.range(0, v1.length)
                .map(index -> v1[index] + v2[index])
                .toArray();
    }

    public int[] getResult() {
        return resV;
    }
}
