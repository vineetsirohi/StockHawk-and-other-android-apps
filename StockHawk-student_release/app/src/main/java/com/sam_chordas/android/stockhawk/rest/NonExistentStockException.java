package com.sam_chordas.android.stockhawk.rest;

/**
 * Created by vineet on 20-Mar-16.
 */
public class NonExistentStockException extends Exception {

    private String mStockName;

    public NonExistentStockException(String stockName) {
        mStockName = stockName;
    }

    public String getStockName() {
        return mStockName;
    }
}
