package com.example.filemanager;

public enum ViewTpe {

    ROW(0), GRID(1);

    private final int value;

    ViewTpe(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
