package CsvConverterToXml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    public String readLine()
    {
        String ligne = null;

        try {
            if (br != null) {
                ligne = br.readLine();
            } else {
                System.err.println("[MyFileReader] Le fichier n'est pas ouvert.");
            }
        } catch (IOException e) {
            System.err.println("[MyFileReader] Erreur lors de la lecture de la ligne : " + e.getMessage());
        }

        return ligne;
    }

}