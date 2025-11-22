package DOMParser;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class DOMParserDTDImages {
    static String xmlFile = "src/main/resources/PADCHEST.xml";
    static boolean ignoreWhiteSpaces = true;

    public static void main() {

        try {
            // ==== SETUP DES OPTIONS DU BUILDER ====
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);                                // DTD validation
            factory.setIgnoringElementContentWhitespace(ignoreWhiteSpaces);

            DocumentBuilder builder = factory.newDocumentBuilder();

            // ==== HANDLER DES ERREURS ====
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

            // ==== CHARGER ET VALIDER LE XML ====
            Document xml = builder.parse(new File(xmlFile));
            Element root = xml.getDocumentElement();

            System.out.println("==== Fichier valide ! Racine : " + root.getNodeName() + " ====\n");

            NodeList listImages = root.getElementsByTagName("image");

            int countLocRight = 0;
            Map<String, Integer> labelFreq = new HashMap<>();

            // ==== PARCOURIR CHAQUE IMAGE ====
            for (int i = 0; i < listImages.getLength(); i++) {
                Element img = (Element) listImages.item(i);

                String id = img.getAttribute("Identifiant");

                // ==== LOCALIZATIONS ====
                NodeList locs = img.getElementsByTagName("Localization");
                for (int j = 0; j < locs.getLength(); j++) {
                    String loc = locs.item(j).getTextContent().trim();
                    if (loc.equalsIgnoreCase("loc right")) {
                        countLocRight++;
                    }
                }

                // ==== LABELS ====
                NodeList labels = img.getElementsByTagName("Label");
                for (int j = 0; j < labels.getLength(); j++) {
                    String lbl = labels.item(j).getTextContent().trim();
                    if (!lbl.isEmpty()) {
                        labelFreq.put(lbl, labelFreq.getOrDefault(lbl, 0) + 1);
                    }
                }

            }

            // ==== AFFICHAGE STATISTIQUES ====
            System.out.println("\n === Statistiques demandées ===");

            System.out.println("Nombre d’images contenant 'loc right' : " + countLocRight);

            System.out.println("\n Top 10 des labels les plus fréquents :");

            labelFreq.entrySet().stream()
                    .sorted((a,b) -> b.getValue() - a.getValue())
                    .limit(10)
                    .forEach(e -> System.out.println("  - " + e.getKey() + " : " + e.getValue() + " occurrences"));

        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }
}
