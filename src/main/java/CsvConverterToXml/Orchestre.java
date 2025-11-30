package CsvConverterToXml;

import Utils.ProjectConfig;
import java.util.ArrayList;

public class Orchestre {


    // ---------------------------------------------------------------------
    // === Conversion CSV → XML ===
    // ---------------------------------------------------------------------
    public static void convertCSVtoXML(int niveauXSLT) {

        long startTime = System.currentTimeMillis();

        try {
            // 1 - Lire CSV via MyFileReader
            String csvPath = ProjectConfig.get("csv.input");
            MyFileReader reader = new MyFileReader(csvPath);
            reader.openFile();
            ArrayList<String> rawLines = reader.readAllLines();
            reader.closeFile();

            // 2 - Fix des lignes cassées
            ArrayList<String> fixedLines = CsvParser.fixBrokenLines(rawLines);

            // 3 - Écrire XML
            String xmlOutputPath = ProjectConfig.get("xml.output");
            MyFileWriter writer = new MyFileWriter(xmlOutputPath);
            writer.openFile();

            writer.WriteLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

            switch(niveauXSLT)
            {
                case 1:
                    writer.WriteLine("<?xml-stylesheet href=\"../java/XSLT/minimum/style.xsl\" type=\"text/xsl\"?>");
                    break;
                case 2:
                    writer.WriteLine("<?xml-stylesheet href=\"../java/XSLT/pros/style.xsl\" type=\"text/xsl\"?>");
                    break;
                case 3:
                    writer.WriteLine("<?xml-stylesheet href=\"../java/XSLT/experts/style.xsl\" type=\"text/xsl\"?>");
                    break;
                default:

                    break;
            }



            // DTD configurable
            String dtd = ProjectConfig.get("dtd.path");
            writer.WriteLine("<!DOCTYPE Images SYSTEM \"" + dtd + "\">");

            // Header XML
            String headerLine = fixedLines.getFirst();
            writer.setAllHeader(CsvParser.GetAllHeaders(headerLine));
            writer.WriteFirstBalise("Images");

            // Process line by line
            for (int i = 1; i < fixedLines.size(); i++) {
                String line = fixedLines.get(i);
                if (line.trim().isEmpty())
                    continue;

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
