
package molediggers;

import java.io.*;
import java.util.*;

public class Leaderboard {
     private static final String FILE_NAME = "leaderboard.txt"; 
 
    public static void addScore(String name, int score) { 
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) { 
            fw.write(name + "," + score + "\n"); 
        } catch (IOException e) { 
            System.out.println("Error saving score."); 
        } 
    } 
 
    public static List<String> getTopScores() { 
        List<String> scores = new ArrayList<>(); 
 
        try (BufferedReader br = new BufferedReader(new 
            FileReader(FILE_NAME))) { 
            String line; 
            while ((line = br.readLine()) != null) { 
                scores.add(line); 
            } 
        } catch (IOException e) { 
            return scores; 
        } 
 
        // Sort highest score first 
        scores.sort((a, b) -> { 
            int scoreA = Integer.parseInt(a.split(",")[1]); 
            int scoreB = Integer.parseInt(b.split(",")[1]); 
            return scoreB - scoreA; 
        }); 
 
        return scores.size() > 10 ? scores.subList(0, 10) : scores; 
    }
}



