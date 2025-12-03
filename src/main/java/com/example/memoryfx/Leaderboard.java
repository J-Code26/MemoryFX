package com.example.memoryfx;

import java.io.*;
import java.util.*;

public class Leaderboard {
    private static final String FILE_NAME = "leaderboard.txt";

    //This function writes the variables score and name to my file
    public static void saveScore(String name, int score) {
        try (FileWriter writer = new FileWriter("leaderboard.txt", true)) {
            writer.write(name + "," + score + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This function reads the file I gave it and if there are scores they will appear on the title screen.
    public static List<String> getScores() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 2) continue;
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        lines.sort((a, b) -> {
            try {
                int scoreA = Integer.parseInt(a.split(",")[1]);
                int scoreB = Integer.parseInt(b.split(",")[1]);
                return Integer.compare(scoreB, scoreA);
            } catch (Exception e) {
                return 0;
            }
        });

        return lines;
    }
}
