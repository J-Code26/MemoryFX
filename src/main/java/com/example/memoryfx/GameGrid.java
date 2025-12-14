package com.example.memoryfx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class GameGrid {
    private final int rows;
    private final int cols;
    private final Button[][] buttons;
    private final GridPane gridPane;

    public GameGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        buttons = new Button[rows][cols];
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.setAlignment(Pos.CENTER);
        createGrid();
    }

    //This is the function that created the buttons to be randomized using a simple 2D array.
    private void createGrid() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {

                Button btn = new Button();

                btn.setMinSize(75,75);
                btn.setStyle(
                        "-fx-background-color: lightgray;" +
                        "-fx-min-width: 75; " +
                        "-fx-min-height: 75;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 2;"
                );

                buttons[i][j] = btn;
                gridPane.add(btn,j,i);
            }
        }
    }
    public GridPane getGridPane() {
        return gridPane;
    }
    public Button getButton(int i, int j) {
        return buttons[i][j];
    }
    public int getRows() {return rows;}
    public int getCols() {return cols;}
}
