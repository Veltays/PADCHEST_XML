package CsvConverterToXml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MyFileReader {

    private final String filePath;
    private BufferedReader br;

    public MyFileReader(String filePath) {
        this.filePath = filePath;
    }

    public void openFile() {
        try {
            FileReader fileReader = new FileReader(filePath);
            br = new BufferedReader(fileReader);

        } catch (IOException e) {
            System.err.println("[MyFileReader] Erreur lors de l'ouverture du fichier : " + e.getMessage());
        }
    }

    public void closeFile() {
        try {
            if (br != null)
                br.close();

        } catch (IOException e) {
            System.err.println("[MyFileReader] Erreur lors de la fermeture du fichier : " + e.getMessage());
        }
    }

    public String readLine() {
        try {
            if (br != null) {
                return br.readLine();
            } else {
                System.err.println("[MyFileReader] Le fichier n'est pas ouvert.");
            }
        } catch (IOException e) {
            System.err.println("[MyFileReader] Erreur lors de la lecture de la ligne : " + e.getMessage());
        }

        return null;
    }

    // ---------------------------------------------------------
    // lit TOUTES les lignes du fichier
    // ---------------------------------------------------------
    public ArrayList<String> readAllLines() {
        ArrayList<String> lines = new ArrayList<>();
        String line;

        try {
            if (br == null) {
                System.err.println("[MyFileReader] Le fichier n'est pas ouvert.");
                return lines;
            }

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            System.err.println("[MyFileReader] Erreur lors de la lecture complète : " + e.getMessage());
        }

        return lines;
    }
}
