package SAXParser;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

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
            // 📌 Charger le fichier XSD
            File xsdFile = new File("src/main/resources/validator.xsd");
            if (!xsdFile.exists()) {
                System.err.println("❌ ERREUR : validator.xsd introuvable !");
                return;
            }

            // 📌 Charger le fichier XML
            File xmlFile = new File("src/main/resources/PADCHEST.xml");
            if (!xmlFile.exists()) {
                System.err.println("❌ ERREUR : PADCHEST.xml introuvable !");
                return;
            }

            // 📌 Fabrique et chargement du schéma XSD
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsdFile);

            // 📌 Configuration du parser SAX
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setSchema(schema);

            SAXParser parser = factory.newSAXParser();

            // 📌 Handler pour gérer les erreurs
            DefaultHandler handler = new DefaultHandler() {

                @Override
                public void error(SAXParseException e) throws SAXException {
                    System.err.println("❌ ERROR");
                    System.err.println("➡ Ligne : " + e.getLineNumber() + ", Colonne : " + e.getColumnNumber());
                    System.err.println("➡ Message : " + e.getMessage());
                    throw e;
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    System.err.println("🔥 FATAL ERROR");
                    System.err.println("➡ Ligne : " + e.getLineNumber() + ", Colonne : " + e.getColumnNumber());
                    System.err.println("➡ Message : " + e.getMessage());
                    throw e;
                }

                @Override
                public void warning(SAXParseException e) throws SAXException {
                    System.err.println("⚠ WARNING : " + e.getMessage());
                }
            };

            // 📌 Lancer la validation
            parser.parse(xmlFile, handler);

            System.out.println("✅ Validation XSD réussie ! XML conforme.");

        } catch (Exception e) {
            System.err.println("❌ SAXException : " + e.getMessage());
        }
    }
}
