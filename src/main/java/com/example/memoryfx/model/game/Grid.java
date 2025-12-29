package com.example.memoryfx.model.game;

public class Grid {
    private int size;
    private Tile[][] tiles;

    public Grid(int size){
        this.size = size;
        tiles = new Tile[size][size];
        createTiles();
    }

    public void setSize(int size){
        this.size = size;
    }

    public int getSize(){
        return this.size;
    }

    public void createTiles() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }
}
