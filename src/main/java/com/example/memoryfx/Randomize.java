package com.example.memoryfx;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;

public class Randomize {
    private final int rows;
    private final int cols;
    private boolean roundActive = true;
    private int lives = 50;
    private final GameGrid grid;
    private int score;
    private int roundTotalCorrect;
    private int roundCorrectClicked;
    public final ArrayList<ArrayList<Integer>> pattern;
    private OnRoundComplete onRoundComplete;
    private OnLivesChange onLivesChange;

    public Randomize(GameGrid grid, int rows, int cols, int startingScore, int existingLives) {
        this.rows = rows;
        this.cols = cols;
        this.grid = grid;
        this.lives = existingLives;
        this.score = startingScore;
        this.pattern = new ArrayList<>();
        generatePattern();
    }
    public void setOnRoundComplete(OnRoundComplete callback) {
        this.onRoundComplete = callback;
    }

    public int getScore() {
        return score;
    }

    public int getLives(){
        return lives;
    }

    public boolean getRoundActive(){
        return roundActive;
    }

    public void setOnLivesChange(OnLivesChange callback) {
        this.onLivesChange = callback;
    }

    private void wrongClicked(){
        if(lives > 0) {
            lives--;
        }
        if(onLivesChange != null) {
            onLivesChange.onChange(lives);
        }

        if(lives <= 0){
            promptForNameAndSaveScore(score);
        }
    }

    public interface OnLivesChange{
        void onChange(int newLives);
    }

    //This is my simple randomizer that creates the pattern for each grid
    //It uses an arraylist to add the remaining ones that were found in the algorithm
    //and then add them to the pattern to then create the green squares.
    public void generatePattern() {
        pattern.clear();
        roundTotalCorrect = 0;
        roundCorrectClicked = 0;


        for (int i = 0; i < rows; i++) {
            ArrayList<Integer> rowList = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                int value = (int) (Math.random() * 2);
                //Adds the ones to rowList
                if (value == 1) roundTotalCorrect++;
                rowList.add(value);
            }
            pattern.add(rowList);

        }

    }

    public int getValue(int row, int col) {
        return pattern.get(row).get(col);
    }

    public void revealPattern(Duration revealTime) {
        revealPattern(revealTime, null);
    }

    //This is the working lambdas of the game itself that checks for each square clicked and resets on certain criteria.
    // Notice I use OnRoundComplete to ensure sync across files
    public void revealPattern(Duration revealTime, Runnable onFinished) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (getValue(i, j) == 1) {
                    Button btn = grid.getButton(i, j);
                    btn.setStyle("-fx-background-color: green;" +
                            "-fx-min-width: 75; -fx-min-height: 75;" +
                            "-fx-border-color: black; -fx-border-width: 2;");
                    fadeButton(btn);
                }
            }
        }

        PauseTransition pause = new PauseTransition(revealTime);
        pause.setOnFinished(event -> {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Button btn = grid.getButton(i, j);
                    btn.setStyle("-fx-background-color: lightgray;" +
                            "-fx-min-width: 75; -fx-min-height: 75;" +
                            "-fx-border-color: black; -fx-border-width: 2;");

                    final int r = i;
                    final int c = j;
                    btn.setOnMouseClicked(e -> {
                        if(!roundActive) {
                            return;
                        }

                        if (getValue(r, c) == 1 && !btn.getStyle().contains("green")) {
                            btn.setStyle("-fx-background-color: green;" +
                                    "-fx-min-width: 75; -fx-min-height: 75;" +
                                    "-fx-border-color: black; -fx-border-width: 2;");
                            score++;
                            roundCorrectClicked++;
                            onRoundComplete.onComplete(score);
                        } else if (getValue(r, c) == 0) {
                            wrongClicked();

                            if(lives <= 0){
                                promptForNameAndSaveScore(score);
                            }

                            btn.setStyle("-fx-background-color: lightgray;" +
                                    "-fx-min-width: 75; -fx-min-height: 75;" +
                                    "-fx-border-color: black; -fx-border-width: 2;");
                        }
                        if(allCorrectClicked()){
                            roundActive = false;
                            onRoundComplete.onComplete(score);
                        }

                    });
                }
            }
            if (onFinished != null) onFinished.run();
        });
        pause.play();
    }


    //The first part of my animation of the grid.
    public void fadeButton(Button btn) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.0), btn);
        fade.setFromValue(1.0);
        fade.setToValue(0.4);
        fade.setCycleCount(1);
        fade.setAutoReverse(true);
        fade.setOnFinished(event -> btn.setOpacity(1.0));
        fade.play();
    }


    //My universal function reset whenever I need to reset the board
    public void resetBoard() {
        roundActive = true;
        generatePattern();
        roundCorrectClicked = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Button btn = grid.getButton(i, j);
                btn.setStyle("-fx-background-color: lightgray;" +
                        "-fx-min-width: 75; -fx-min-height: 75;" +
                        "-fx-border-color: black; -fx-border-width: 2;");
                btn.setOnMouseClicked(null);
            }
        }
        revealPattern(Duration.seconds(1.5));
    }

    public boolean allCorrectClicked(){
        return roundCorrectClicked == roundTotalCorrect;
    }

    //This function is called anytime I want the game to end. This is essentially my Game Over screen.
    public void promptForNameAndSaveScore(int finalScore){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Game Over");
        dialog.setHeaderText("Your final score: " + finalScore);
        dialog.setContentText("Enter your name (No special characters): ");

        dialog.showAndWait().ifPresent(name -> {
            Leaderboard.saveScore(name, finalScore);
        });
        System.exit(0);
    }

}