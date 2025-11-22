package DOMParser;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.validation.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DOMParserXSDImages {

    static String xmlFile = "src/main/resources/PADCHEST.xml";
    static String xsdFile = "src/main/resources/validator.xsd";
    static boolean ignoreWhiteSpaces = true;

    public static void main() {

        try {
            System.out.println("→ Validation XML + XSD : " + xmlFile);

            // ==== CONFIG FACTORY ====
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);      // pas DTD
            factory.setNamespaceAware(true);   // obligatoire pour XSD
            factory.setIgnoringElementContentWhitespace(ignoreWhiteSpaces);

            // ==== AJOUT SCHEMA XSD ====
            SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = schemaFactory.newSchema(new File(xsdFile));
            factory.setSchema(schema);

            // ==== BUILDER ====
            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException e) { System.out.println("⚠ WARNING : " + e.getMessage()); }
                public void error(SAXParseException e) throws SAXException {
                    System.out.println("ERROR : " + e.getMessage());
                    throw e;
                }
                public void fatalError(SAXParseException e) throws SAXException {
                    System.out.println("FATAL : " + e.getMessage());
                    throw e;
                }
            });

            // ==== PARSE + VALIDATION ====
            Document xml = builder.parse(new File(xmlFile));
            Element root = xml.getDocumentElement();

            System.out.println("==== Fichier XML valide contre XSD ====");
            System.out.println("Racine : " + root.getNodeName() + "\n");

            NodeList listImages = root.getElementsByTagName("image");

            int countLocRight = 0;
            Map<String, Integer> labelFreq = new HashMap<>();

            // ==== PARCOURS DES IMAGES ====
            for (int i = 0; i < listImages.getLength(); i++) {
                Element img = (Element) listImages.item(i);



                // === LOCALIZATIONS ===
                NodeList locs = img.getElementsByTagName("Localization");
                for (int j = 0; j < locs.getLength(); j++) {
                    String loc = locs.item(j).getTextContent().trim();
                    if (loc.equalsIgnoreCase("loc right")) {
                        countLocRight++;
                    }
                }

                // === LABELS ===
                NodeList labels = img.getElementsByTagName("Label");
                for (int j = 0; j < labels.getLength(); j++) {
                    String lbl = labels.item(j).getTextContent().trim();
                    if (!lbl.isEmpty()) {
                        labelFreq.put(lbl, labelFreq.getOrDefault(lbl, 0) + 1);
                    }
                }


            }

            // ==== AFFICHAGE DES STATS ====
            System.out.println("\n=== Statistiques XML/XSD ===");

            System.out.println("Images contenant 'loc right' : " + countLocRight);

            System.out.println("\nTop 10 labels les plus fréquents :");
            labelFreq.entrySet().stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(10)
                    .forEach(e -> System.out.println("  - " + e.getKey() + " : " + e.getValue()));

        }
        catch (SAXException | IOException | ParserConfigurationException e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }
}