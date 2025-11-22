package SAXParser;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import Utils.ProjectConfig;
import org.xml.sax.*;

public class SAXParserDTDImages {

    public static void main() {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser sp = factory.newSAXParser();
            XMLReader reader = sp.getXMLReader();

            // Handler de statistiques
            ImageSAXHandler handler = new ImageSAXHandler();
            reader.setContentHandler(handler);

            // Gestion des erreurs
            reader.setErrorHandler(new ErrorHandler() {
                private String fmt(SAXParseException e) {
                    return String.format("%s\n Ligne %d, Colonne %d", e.getMessage(), e.getLineNumber(), e.getColumnNumber());
                }
                public void warning(SAXParseException e) { System.out.println("WARNING\n" + fmt(e)); }
                public void error(SAXParseException e) throws SAXException { System.out.println("ERROR\n" + fmt(e)); throw e; }
                public void fatalError(SAXParseException e) throws SAXException { System.out.println("FATAL\n" + fmt(e)); throw e; }
            });

            System.out.println("Validation SAX + DTD en cours...");
            reader.parse(ProjectConfig.get("xml.output"));

            System.out.println("XML VALIDE selon le DTD !\n");

            // Affichage des statistiques
            System.out.println("=== Statistiques SAX/DTD ===");
            System.out.println("Nombre d’images contenant 'loc right' : " + handler.getLocRightCount());

            System.out.println("\nTop 10 des labels les plus fréquents :");
            handler.getLabelCount()
                    .entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(10)
                    .forEach(e -> System.out.println(" - " + e.getKey() + " : " + e.getValue()));

        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Erreur SAX : " + e.getMessage());
        }
    }
}