package com.example.memoryfx;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameScreen {
    private final BorderPane root = new BorderPane();
    private GameGrid grid;
    private Randomize randomize;
    private int score;
    private int lives;
    private final Stage stage;
    private final TitleScreen titleScreen;
    private final Label scoreLabel = new Label();
    private final Label lifeLabel = new Label();
    private final Label popupLabel = new Label("Grid Beaten! Size Upgrade!");
    private final Label nextLabel = new Label();
    private int lastThresholdHit = 0;


    //I found that using an array instead of if statements flowed better for how I used the thresholds.
    private final int[] thresholds = {50,150,250,350};


    public GameScreen(Stage stage, int rows, int cols, int startingScore) {
        this.stage = stage;
        this.lives = 50;
        this.titleScreen = new TitleScreen(stage);
        this.score = startingScore;

        initializeGrid(rows, cols, score, this.lives);
        setupUI();
    }

    private void initializeGrid(int rows, int cols, int existingScore, int currentLives) {
        grid = new GameGrid(rows, cols);
        randomize = new Randomize(grid, rows, cols, existingScore, currentLives);

        randomize.setOnLivesChange(newLives -> {
            lifeLabel.setText("Lives: " + newLives);
            this.lives = newLives;
        });

        //This function ensures the score is live to each time the button is pressed.
        // I had a big issue with this once I implemented the scoring system.
        randomize.setOnRoundComplete(newScore -> {
            score = newScore;
            scoreLabel.setText("Score: " + score);


                for (int i = 0; i < thresholds.length; i++) {
                    int t = thresholds[i];
                    if (score == t && lives > 0 && lastThresholdHit != t) {
                        showTemporaryLabel(popupLabel);
                        lastThresholdHit = t;
                        if (i + 1 < thresholds.length) {
                            nextLabel.setText("Threshold: " + thresholds[i + 1]);
                        } else {
                            nextLabel.setText("Threshold: Infinity!");
                        }
                        increaseGridSize();
                        break;
                    }
                }
        });

        root.setCenter(grid.getGridPane());
        randomize.revealPattern(Duration.seconds(1.5));
    }

    //This is what gets the game going using the buttons I have.
    //The reveal button of all of them has been changed and reworked many times.
    public void setupUI() {
        //I did a lot of research using AI to figure out how to format everything the way I did.
        //I did not know how to use the padding properly, and I also was having issues
        //with overlapping buttons due to lack of knowledge.
        Label titleLabel = new Label("Grid Memory Game");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        lifeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        lifeLabel.setText("Lives: " + randomize.getLives());
        popupLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        popupLabel.setVisible(false);
        nextLabel.setText("Next Threshold: " + thresholds[0]);
        nextLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox upperBox = new HBox(40, titleLabel, scoreLabel, lifeLabel, nextLabel);

        upperBox.setAlignment(Pos.CENTER);
        root.setTop(upperBox);

        Button nextBtn = new Button("Next Pattern");
        Button returnBtn = new Button("Return to Home Screen");
        Button exitBtn = new Button("Exit");

        nextBtn.setOnAction(e -> {
            if(!randomize.getRoundActive()) {
                randomize.resetBoard();
                randomize.revealPattern(Duration.seconds(1.5), () -> nextBtn.setDisable(false));
                nextBtn.setDisable(true);
            }
        });

        returnBtn.setOnAction(e -> stage.getScene().setRoot(titleScreen.getRoot()));
        exitBtn.setOnAction(e -> {
            randomize.promptForNameAndSaveScore(score);
            System.exit(0);

        });

        HBox bottomBox = new HBox(40, returnBtn, nextBtn, exitBtn, popupLabel);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20, 0, 20, 0));
        root.setBottom(bottomBox);
    }

    //This function determines the size of the grid.
    public void increaseGridSize() {
        grid.getGridPane().setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        pause.setOnFinished(e -> {
            int newRows = grid.getRows() + 1;
            int newCols = grid.getCols() + 1;
            initializeGrid(newRows, newCols, randomize.getScore(), this.lives);
        });
        pause.play();
    }

    public void showTemporaryLabel(Label tempLabel){
        tempLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> tempLabel.setVisible(false));
        pause.play();
    }

    public BorderPane getRoot() {
        return root;
    }
}