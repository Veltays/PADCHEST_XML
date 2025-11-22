package CsvConverterToXml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class CsvParser {

    public final static Set<Integer> INCLUDED_ITEMS = Set.of(
            0,  // ID
            1,  // ImageID
            2,  // ImageDir
            4,  // StudyID
            5,  // PatientID
            6,  // PatientBirth
            9,  // Projection
            10, // MethodProjection
            11, // Pediatric
            28, // ReportID
            29, // Report
            30, // MethodLabel
            31, // Labels
            32, // Localizations
            33, // LabelsLocalizationsBySentence
            34, // labelCUIS
            35  // LocalizationsCUIS
    );

    public static String[] GetAllHeaders(String headerLine) {
        String[] AllHeaders;
        String[] FilteredHeaders;
        AllHeaders = Spliter(headerLine);
        FilteredHeaders = DeleteExcludedData(AllHeaders);
        System.out.println("Headers: " + Arrays.toString(FilteredHeaders));

        return FilteredHeaders;
    }

    public static String[] ParseLine(String line) {
        line = normalizePythonArray(line);

        String[] all = Spliter(line);

        return DeleteExcludedData(all);
    }

    public static String[] DeleteExcludedData(String[] dataLine) {
        ArrayList<String> filteredData = new ArrayList<>();

        for (int i = 0; i < dataLine.length; i++) {
            if (INCLUDED_ITEMS.contains(i)) {
                filteredData.add(dataLine[i]);
            }
        }

        return filteredData.toArray(new String[0]);
    }

    public static String[] Spliter(String line) {
        ArrayList<String> mots = new ArrayList<>();
        StringBuilder mot = new StringBuilder();

        int tabLevel = 0;
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            switch (c) {
                case '"':
                    inQuotes = !inQuotes;
                    break;

                case '[':
                    tabLevel++;
                    mot.append(c);
                    break;

                case ']':
                    tabLevel = Math.max(0, tabLevel - 1);
                    mot.append(c);
                    break;

                case ',':
                    if (!inQuotes && tabLevel == 0) {
                        mots.add(mot.toString().trim());
                        mot.setLength(0);
                    } else {
                        mot.append(c);
                    }
                    break;

                default:
                    mot.append(c);
                    break;
            }
        }

        if (mot.length() > 0) {
            mots.add(mot.toString().trim());
        }

        return mots.toArray(new String[0]);
    }

    private static String normalizePythonArray(String line) {
        // Corrige les retours à la ligne internes
        line = line.replace("\n", " ").replace("\r", " ");

        // Convertit format Python: ['A' 'B' 'C'] → ['A','B','C']
        if (line.contains("['") && line.contains("' '")) {
            line = line.replace("' '", "', '");
        }

        return line;
    }

}
