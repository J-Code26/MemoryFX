package com.example.memoryfx.model.data;

public class ScoreManager {
    private int score;
    private int lives;
    private int threshold;

    public ScoreManager(int score, int lives, int threshold){
        this.score = score;
        this.lives = lives;
        this.threshold = threshold;
    }

    public void setScore(int score){this.score = score;}
    public void setLives(int lives){this.lives = lives;}
    public void setThreshold(int threshold){this.threshold = threshold;}

    public int getScore(){return this.score;}
    public int getLives(){return this.lives;}
    public int getThreshold(){return this.threshold;}
}
