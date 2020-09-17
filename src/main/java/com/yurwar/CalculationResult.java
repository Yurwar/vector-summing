package com.yurwar;

public class CalculationResult {
    private int[] resV;
    private long calculationDuration;

    public CalculationResult(int[] resV, long calculationDuration) {
        this.resV = resV;
        this.calculationDuration = calculationDuration;
    }

    public int[] getResV() {
        return resV;
    }

    public void setResV(int[] resV) {
        this.resV = resV;
    }

    public long getCalculationDuration() {
        return calculationDuration;
    }

    public void setCalculationDuration(long calculationDuration) {
        this.calculationDuration = calculationDuration;
    }
}
