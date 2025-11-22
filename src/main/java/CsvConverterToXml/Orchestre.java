package CsvConverterToXml;

import java.util.ArrayList;

public class Orchestre {

    public static void main() {
        convertCSVtoXML();
    }

    // ---------------------------------------------------------------------
    // === Conversion CSV → XML ===
    // ---------------------------------------------------------------------
    public static void convertCSVtoXML() {

        long startTime = System.currentTimeMillis();

        try {
            // 1 - Lire CSV via MyFileReader
            MyFileReader reader = new MyFileReader("src/main/resources/PADCHEST_chest_x_ray_images_labels_160K_01.02.19.csv");
            reader.openFile();
            ArrayList<String> rawLines = reader.readAllLines();
            reader.closeFile();

            // 2 - Fix des lignes cassées
            ArrayList<String> fixedLines = CsvParser.fixBrokenLines(rawLines);

            // 3 - Écrire XML
            MyFileWriter writer = new MyFileWriter("src/main/resources/PADCHEST.xml");
            writer.openFile();

            writer.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.WriteLine("<!DOCTYPE Images SYSTEM \"validator.dtd\">");

            String headerLine = fixedLines.getFirst();
            writer.setAllHeader(CsvParser.GetAllHeaders(headerLine));
            writer.WriteFirstBalise("Images");

            for (int i = 1; i < fixedLines.size(); i++) {
                String line = fixedLines.get(i);
                if (line.trim().isEmpty()) continue;

                String[] parsed = CsvParser.ParseLine(line);
                writer.WriteXMLLine(parsed);
            }

            writer.WriteLastBalise("Images");
            writer.closeFile();

        } catch (Exception e) {
            System.out.println("[Orchestre] Erreur lors de la conversion CSV → XML : " + e.getMessage());
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\n[Timer] Conversion terminée en " + ((endTime - startTime) / 1000.0) + " secondes.");
    }



}
