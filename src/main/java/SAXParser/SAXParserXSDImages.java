package SAXParser;

import org.xml.sax.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class SAXParserXSDImages {

    public static void main() {
        System.out.println("→ Lancement du parser SAX avec XSD...");

        try {
            // ====== FICHIERS ======
            File xsdFile = new File("src/main/resources/validator.xsd");
            File xmlFile = new File("src/main/resources/PADCHEST.xml");

            if (!xsdFile.exists() || !xmlFile.exists()) {
                System.err.println("ERREUR : Fichier introuvable !");
                return;
            }

            // ====== CHARGEMENT DU XSD ======
            SchemaFactory schemaFactory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsdFile);

            // ====== CONFIGURATION PARSEUR SAX ======
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setSchema(schema);

            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();

            // ====== HANDLER DES STATISTIQUES ======
            ImageSAXHandler statsHandler = new ImageSAXHandler();
            reader.setContentHandler(statsHandler);

            // ====== HANDLER DES ERREURS ======
            reader.setErrorHandler(new ErrorHandler() {

                private String fmt(SAXParseException e) {
                    return String.format("%s\nLigne %d, Colonne %d",
                            e.getMessage(),
                            e.getLineNumber(),
                            e.getColumnNumber());
                }

                @Override public void warning(SAXParseException e) {
                    System.err.println("WARNING : " + fmt(e));
                }

                @Override public void error(SAXParseException e) throws SAXException {
                    System.err.println("ERROR : " + fmt(e));
                    throw e;
                }

                @Override public void fatalError(SAXParseException e) throws SAXException {
                    System.err.println("FATAL ERROR : " + fmt(e));
                    throw e;
                }
            });

            // ====== PARSING + VALIDATION ======
            reader.parse(xmlFile.toURI().toASCIIString());
            System.out.println(" Validation XSD réussie ! XML conforme.\n");

            // ====== STATS ======
            System.out.println("=== Statistiques SAX/XSD ===");
            System.out.println("Images contenant 'loc right' : " +
                    statsHandler.getLocRightCount());

            System.out.println("\nTop 10 labels :");
            statsHandler.getLabelCount()
                    .entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(10)
                    .forEach(e ->
                            System.out.println(" - " + e.getKey() + " : " + e.getValue())
                    );

        } catch (Exception e) {
            System.err.println("SAXException : " + e.getMessage());
        }
    }
}
