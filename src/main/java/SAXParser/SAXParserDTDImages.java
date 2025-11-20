package SAXParser;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;

public class SAXParserDTDImages {

    public static void main(String[] args) {
        try {
            // -----------------------------------------
            // 1) Factory + Validation
            // -----------------------------------------
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);        // valide avec le DTD
            factory.setNamespaceAware(true);

            // -----------------------------------------
            // 2) Création du parser SAX
            // -----------------------------------------
            SAXParser sp = factory.newSAXParser();
            XMLReader reader = sp.getXMLReader();

            // -----------------------------------------
            // 3) Gestion des erreurs DÉTAILLÉE
            // -----------------------------------------
            reader.setErrorHandler(new ErrorHandler() {

                private String formatError(SAXParseException e) {
                    return String.format(
                            "%s\n➡ Ligne %d, Colonne %d\n➡ Public ID : %s\n➡ System ID : %s",
                            e.getMessage(),
                            e.getLineNumber(),
                            e.getColumnNumber(),
                            e.getPublicId(),
                            e.getSystemId()
                    );
                }

                @Override
                public void warning(SAXParseException e) {
                    System.out.println("⚠️ WARNING\n" + formatError(e));
                }

                @Override
                public void error(SAXParseException e) throws SAXException {
                    System.out.println("❌ ERROR\n" + formatError(e));
                    throw e;
                }

                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    System.out.println("💀 FATAL ERROR\n" + formatError(e));
                    throw e;
                }
            });

            // -----------------------------------------
            // 4) Parsing du fichier XML
            // -----------------------------------------
            System.out.println("🔍 Validation SAX + DTD en cours...");
            reader.parse("src/main/resources/PADCHEST.xml");

            System.out.println("✅ XML VALIDE selon le DTD !");

        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfigurationException : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("SAXException : " + e.getMessage());
        }
    }
}
