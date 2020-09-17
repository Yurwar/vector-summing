package com.yurwar;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AppTest {
    private App testedInstance = new App();

    @Test
    public void shouldReturnSumOfTwoVectorsWhenCalculateSequential() {
        int[] v1 = {1, -2, 5, 6, 8, 10};
        int[] v2 = {0, 3, 7, 2, 4, 11};

        CalculationResult calculationResult = testedInstance.sumVectors(v1, v2);

        int[] actual = calculationResult.getResV();
        int[] expected = {1, 1, 12, 8, 12, 21};

        assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnSumOfTwoVectorsWhenCalculateParallel() {
        int[] v1 = {1, -2, 5, 6, 8, 10, 1, -2, 5, 6, 8, 10};
        int[] v2 = {0, 3, 7, 2, 4, 11, 0, 3, 7, 2, 4, 11};

        int numberOfJobs = 4;
        CalculationResult calculationResult = testedInstance.sumVectorsParallel(v1, v2, numberOfJobs);

        int[] actual = calculationResult.getResV();
        int[] expected = {1, 1, 12, 8, 12, 21, 1, 1, 12, 8, 12, 21};

        assertArrayEquals(expected, actual);
    }
}