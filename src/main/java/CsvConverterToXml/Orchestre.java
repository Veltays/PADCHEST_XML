package CsvConverterToXml;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Orchestre {

    public static void main() {

        long startTime = System.currentTimeMillis();

        // Debug paths
        File dtdFile = new File("src/main/resources/validator.dtd");
        System.out.println("DTD exists? " + dtdFile.exists());
        System.out.println("Absolute path = " + dtdFile.getAbsolutePath());

        File xmlFile = new File("src/main/resources/PADCHEST.xml");
        System.out.println("XML exists? " + xmlFile.exists());
        System.out.println("Absolute path = " + xmlFile.getAbsolutePath());

        System.out.println("Working directory = " + new File(".").getAbsolutePath());

        try {

            // 1️⃣ Lire le CSV brut
            Path csvPath = Path.of("src/main/resources/PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv");
            ArrayList<String> rawLines = new ArrayList<>(Files.readAllLines(csvPath));

            // 2️⃣ Réparer les lignes cassées
            ArrayList<String> fixedLines = fixBrokenLines(rawLines);

            // 3️⃣ Ouvrir writer XML
            MyFileWriter writer = new MyFileWriter("src/main/resources/PADCHEST.xml");
            writer.openFile();

            // 4️⃣ En-tête XML
            writer.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.WriteLine("<!DOCTYPE Images SYSTEM \"validator.dtd\">");

            // 5️⃣ Traitement header
            String headerLine = fixedLines.get(0);
            writer.setAllHeader(CsvParser.GetAllHeaders(headerLine));

            // 6️⃣ Écrire la racine
            writer.WriteFirstBalise("Images");

            // 7️⃣ Boucle — chaque ligne = une image
            for (int i = 1; i < fixedLines.size(); i++) {

                String line = fixedLines.get(i);

                if (line.trim().isEmpty()) continue;

                String[] parsed = CsvParser.ParseLine(line);
                writer.WriteXMLLine(parsed);
            }

            // 8️⃣ Fin XML
            writer.WriteLastBalise("Images");
            writer.closeFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\n[Timer] Conversion terminée en " + ((endTime - startTime) / 1000.0) + " secondes.");
    }


    // ---------------------------------------------------------------------
    // 🛠️ FIX DES LIGNES CASSÉES ("broken lines" dans les arrays Python)
    // ---------------------------------------------------------------------

    public static ArrayList<String> fixBrokenLines(ArrayList<String> lines) {

        ArrayList<String> fixed = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String line : lines) {

            // Une vraie ligne commence par un ID numérique suivi d'une virgule
            if (line.matches("^\\d+,.*")) {

                // Si on avait une ligne en cours → on la sauvegarde
                if (current.length() > 0) {
                    fixed.add(current.toString());
                }

                // Commencer une nouvelle ligne propre
                current = new StringBuilder(line);

            } else {
                // Ligne cassée = on la recolle
                current.append(" ").append(line.trim());
            }
        }

        // Ajouter la dernière ligne éventuelle
        if (current.length() > 0) {
            fixed.add(current.toString());
        }

        return fixed;
    }
}
