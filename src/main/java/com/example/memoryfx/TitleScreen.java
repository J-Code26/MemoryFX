package com.example.memoryfx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class TitleScreen {
    private final BorderPane root;

    //This constructor creates the game from the button on the title screen and ensures the formatting.
    public TitleScreen(Stage stage) {
        root = new BorderPane();
        Label titleLabel = new Label("Grid Memory Game");
        titleLabel.setStyle("-fx-font-size: 70px; -fx-font-weight: bold;");
        Button startBtn = new Button("Start Game");
        startBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10 20;");
        startBtn.setOnAction(event -> {
            GameScreen game = new GameScreen(stage, 3, 3, 0);
            stage.getScene().setRoot(game.getRoot());
        });

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(event -> System.exit(0));

        VBox center = new VBox(30, titleLabel, startBtn, exitBtn);
        center.setAlignment(Pos.CENTER);
        root.setCenter(center);

        VBox leaderboard = getLeaderBoardBox();
        leaderboard.setSpacing(10);
        leaderboard.setStyle("-fx-padding: 20;");
        root.setTop(leaderboard);
    }

    public BorderPane getRoot() {
        return root;
    }

    //This is the function that makes the leaderboard visable in the title screen.
    public VBox getLeaderBoardBox() {
        VBox box = new VBox(5);
        Label leaderboard = new Label("<===Leaderboard===>");
        leaderboard.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
        box.getChildren().add(leaderboard);

        List<String> scores = Leaderboard.getScores();
        for (int i = 0; i < Math.min(scores.size(), 10); i++) {
            String s = scores.get(i);
            String[] parts = s.split(",");
            if (parts.length < 2) {
                continue;
            }
            if (parts.length == 2) {
                box.getChildren().add(new Label(parts[0] + " - " + parts[1]));
            }
        }

        box.setAlignment(Pos.TOP_CENTER);
        return box;
    }
}