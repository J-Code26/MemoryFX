package com.example.memoryfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("MemoryFX");
         TitleScreen title = new TitleScreen(stage);
         Scene scene = new Scene(title.getRoot(), 1000, 800);

         stage.setScene(scene);
         stage.show();

    }

    public static void main(String[] args){
        launch();
    }
}
