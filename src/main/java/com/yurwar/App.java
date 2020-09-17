package com.yurwar;


public class App {
    private final ArrayGenerator generator;

    public App() {

        generator = new ArrayGenerator();
    }

    public static void main(String[] args) {
        App app = new App();

        int numberOfJobs = 2;
        int vectorsSize = 10000000;

        for (int i = 0; i < 10; i++) {
            int[] v1 = app.getGenerator().getIntArray(vectorsSize);
            int[] v2 = app.getGenerator().getIntArray(vectorsSize);

            CalculationResult parallelCalcResult = app.sumVectorsParallel(v1, v2, numberOfJobs);
            CalculationResult sequentialCalcResult = app.sumVectors(v1, v2);

            double acceleration = (double) sequentialCalcResult.getCalculationDuration() /
                    parallelCalcResult.getCalculationDuration();

            System.out.println("Acceleration of parallel algorithm: " + acceleration);
            System.out.println("Efficiency of parallel algorithm: " + acceleration / numberOfJobs);
            System.out.println();
        }
    }

    public CalculationResult sumVectors(int[] v1, int[] v2) {
        return sumVectorsParallel(v1, v2, 1);
    }

    public CalculationResult sumVectorsParallel(int[] v1, int[] v2, int numberOfJobs) {
        int vectorSize = v1.length;
        int[] resV = new int[vectorSize];
        int chunkSize = Math.floorDiv(vectorSize, numberOfJobs);

        long startTime = System.currentTimeMillis();

        Thread[] adderThreads = new Thread[numberOfJobs];
        for (int i = 0; i < numberOfJobs; i++) {
            int startIndex = i * chunkSize;
            adderThreads[i] = new Thread(
                    new VectorAdderRunnable(v1, v2, resV, chunkSize, startIndex));
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

        System.out.printf("Adding of vectors with size %d on %d threads finished in %d ms%n",
                vectorSize, numberOfJobs, calculationDuration);

        return new CalculationResult(resV, calculationDuration);
    }

    public ArrayGenerator getGenerator() {
        return generator;
    }
}
