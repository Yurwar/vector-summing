package com.yurwar;


import java.util.Arrays;

public class App {
    private final ArrayGenerator generator;
    private static final int NUMBER_OF_JOBS = 2;
    private static final int VECTORS_SIZE = 10000000;
    private static final int ITERATIONS = 10;

    public App() {
        generator = new ArrayGenerator();
    }

    public static void main(String[] args) {
        App app = new App();

        long sequentialDuration = 0;
        long parallelDuration = 0;

        int[] v1 = app.getGenerator().getIntArray(VECTORS_SIZE);
        int[] v2 = app.getGenerator().getIntArray(VECTORS_SIZE);

        System.out.println("Vectors initialized");

        for (int i = 0; i < ITERATIONS; i++) {
            CalculationResult sequentialCalcResult = app.sumVectors(v1, v2);
            CalculationResult parallelCalcResult = app.sumVectorsParallel(v1, v2, NUMBER_OF_JOBS);

            sequentialDuration += sequentialCalcResult.getCalculationDuration();
            parallelDuration += parallelCalcResult.getCalculationDuration();
        }

        System.out.println("Calculation finished with " + ITERATIONS + " iterations");

        double averageSequentialDuration = (double) sequentialDuration / ITERATIONS;
        double averageParallelDuration = (double) parallelDuration / ITERATIONS;

        double acceleration = averageSequentialDuration / averageParallelDuration;

        System.out.println("Number of jobs: " + NUMBER_OF_JOBS);
        System.out.println("Vectors size: " + VECTORS_SIZE);
        System.out.println("Acceleration of parallel algorithm: " + acceleration);
        System.out.println("Efficiency of parallel algorithm: " + acceleration / NUMBER_OF_JOBS);
        System.out.println();
    }

    public CalculationResult sumVectors(int[] v1, int[] v2) {
        return sumVectorsParallel(v1, v2, 1);
    }

    public CalculationResult sumVectorsParallel(int[] v1, int[] v2, int numberOfJobs) {
        int vectorSize = v1.length;
        int chunkSize = Math.floorDiv(vectorSize, numberOfJobs);

        long startTime = System.currentTimeMillis();

        VectorAdderThread[] adderThreads = new VectorAdderThread[numberOfJobs];
        for (int i = 0; i < numberOfJobs; i++) {
            int startIndex = i * chunkSize;

            int min = Math.min(startIndex + chunkSize, v1.length);

            int[] innerV1 = Arrays.copyOfRange(v1, startIndex, min);
            int[] innerV2 = Arrays.copyOfRange(v2, startIndex, min);

            adderThreads[i] = new VectorAdderThread(innerV1, innerV2);
        }


        for (Thread adderThread : adderThreads) {
            adderThread.start();
        }

        for (Thread adderThread : adderThreads) {
            try {
                adderThread.join();
            } catch (InterruptedException e) {
                System.err.println("Thread interrupted");
            }
        }

        long calculationDuration = System.currentTimeMillis() - startTime;

        int[] resV = Arrays.stream(adderThreads)
                .flatMapToInt(thread -> Arrays.stream(thread.getResult()))
                .toArray();

        return new CalculationResult(resV, calculationDuration);
    }

    public ArrayGenerator getGenerator() {
        return generator;
    }
}
