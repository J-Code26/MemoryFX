package com.example.memoryfx;

public interface OnRoundComplete {
    //Ensures the score is synced across files
    void onComplete(int newScore);
}
