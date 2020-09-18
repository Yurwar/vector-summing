package com.yurwar;


import java.util.Arrays;

public class App {
    private final ArrayGenerator generator;

    public App() {
        generator = new ArrayGenerator();
    }

    public static void main(String[] args) {
        App app = new App();

        int numberOfJobs = 2;
        int vectorsSize = 10000000;
        int iterations = 10;

        long sequentialDuration = 0;
        long parallelDuration = 0;

        int[] v1 = app.getGenerator().getIntArray(vectorsSize);
        int[] v2 = app.getGenerator().getIntArray(vectorsSize);

        System.out.println("Vectors initialized");

        for (int i = 0; i < iterations; i++) {
            CalculationResult sequentialCalcResult = app.sumVectors(v1, v2);
            CalculationResult parallelCalcResult = app.sumVectorsParallel(v1, v2, numberOfJobs);

            sequentialDuration += sequentialCalcResult.getCalculationDuration();
            parallelDuration += parallelCalcResult.getCalculationDuration();
        }

        System.out.println("Calculation finished with " + iterations + " iterations");

        double averageSequentialDuration = (double) sequentialDuration / iterations;
        double averageParallelDuration = (double) parallelDuration / iterations;

        double acceleration = averageSequentialDuration / averageParallelDuration;

        System.out.println("Number of jobs: " + numberOfJobs);
        System.out.println("Vectors size: " + vectorsSize);
        System.out.println("Acceleration of parallel algorithm: " + acceleration);
        System.out.println("Efficiency of parallel algorithm: " + acceleration / numberOfJobs);
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
