package SAXParser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import java.util.Map;

public class ImageSAXHandler extends DefaultHandler {

    private int locRightCount = 0;
    private final Map<String, Integer> labelCount = new HashMap<>();

    private boolean insideLocalization = false;
    private boolean insideLabel = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equalsIgnoreCase("Localization")) {
            insideLocalization = true;
        }

        if (qName.equalsIgnoreCase("Label")) {
            insideLabel = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {

        String content = new String(ch, start, length).trim();

        if (insideLocalization && content.equalsIgnoreCase("loc right")) {
            locRightCount++;
        }

        if (insideLabel && !content.isEmpty()) {
            labelCount.put(content, labelCount.getOrDefault(content, 0) + 1);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (qName.equalsIgnoreCase("Localization")) {
            insideLocalization = false;
        }

        if (qName.equalsIgnoreCase("Label")) {
            insideLabel = false;
        }
    }

    // === Getters ===
    public int getLocRightCount() {
        return locRightCount;
    }

    public Map<String, Integer> getLabelCount() {
        return labelCount;
    }
}