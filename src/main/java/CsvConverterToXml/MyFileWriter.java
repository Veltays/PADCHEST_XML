package CsvConverterToXml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MyFileWriter {

    private final String filePath;
    private FileWriter fileWriter;
    public static String AllHeader[];
    public static int depth =0;



    /**********************************************************************/
    /* Constructeur et Acesseur                                            */
    /***********************************************************************/


    public MyFileWriter(String filePath)
    {
        this.filePath = filePath;
    }

    public void setAllHeader(String[] allheader)
    {
        AllHeader = allheader;
    }

    public void openFile()
    {
        try
        {
            this.fileWriter = new FileWriter(filePath);
        }
        catch (IOException e)
        {
            System.err.println("[MyFileWriter] Erreur lors de l'ouverture du fichier : " + e.getMessage());
        }
    }

    public void closeFile()
    {
        try
        {
            if (fileWriter != null)
            {
                fileWriter.close();
            }
        }
        catch (IOException e)
        {
            System.err.println("[MyFileWriter] Erreur lors de la fermeture du fichier : " + e.getMessage());
        }
    }


    // fonction qui écrira une ligne dans le fichier
    public void WriteLine(String line)
    {
        try {
            fileWriter.write(GetIndentLevel() + line + "\n");

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }






    /************************************************************************/
    /* Fonction d'écriture d'entête                                        */
    /***********************************************************************/


    public String WriteStartingHeader(String header)
    {
        return  "<" + header + ">" ;
    }

    public String WriteEndingHeader(String header)
    {
        return  "</" + header + ">";
    }

    public void WriteFirstBalise(String header)
    {
        WriteLine("<" + header + ">");
        depth++;
    }

    public void WriteLastBalise(String header)
    {
        depth--;
        WriteLine("</" + header + ">");
    }

    public void WriteImageStart(String identifiant) {
        WriteLine("<image Identifiant=\"" + identifiant + "\">");
        depth++;
    }

    public void WriteImageEnd() {
        depth--;
        WriteLine("</image>");
    }

    /************************************************************************/
    /* Stratégie d'écriture                                               */
    /***********************************************************************/
    public void WriteXMLLine(String[] line) {

        if (line == null || line.length == 0) {
            System.err.println(" Ligne vide ignorée !");
            return; // skip
        }

        WriteImageStart("image_" +line[0]);

        for (int i = 1; i < line.length; i++) {

            if (line[i].contains("[["))
                WriteForAdvancedTab(line[i], AllHeader[i]);
            else if (line[i].contains("["))
                WriteForSimpleTab(line[i], AllHeader[i]);
            else
                WriteSimpleXML(line[i], AllHeader[i]);
        }

        WriteImageEnd();
    }


    public void WriteSimpleXML(String line, String header)
    {
        if (line == null || line.trim().isEmpty() || line.trim().equalsIgnoreCase("nan")) {
            WriteLine("<" + header + "/>");
            return;
        }

        WriteLine("<" + header + ">" + line + "</" + header + ">");
    }

    public void WriteForSimpleTab(String line, String header)
    {
        WriteLine("<" + header + ">");
        depth++;

        String[] values = line.split(",");
        CleanLine(values);

        String tag = getSubHeader(header);

        for (String value : values) {
            value = value.trim();

            // 🔥 Traitement spécial pour NAN
            if (value.equalsIgnoreCase("nan")) {
                WriteLine("<" + tag + "/>");
                continue;
            }

            if (!header.equals("LabelsLocalizationsBySentence")) {
                if (value.isEmpty()) {
                    WriteLine("<" + tag + "/>");
                } else {
                    WriteLine("<" + tag + ">" + value + "</" + tag + ">");
                }
            } else {
                WriteLine("<Sentence>");
                depth++;

                if (!value.isEmpty()) {
                    WriteLine("<LabelSentence>" + value + "</LabelSentence>");
                }

                depth--;
                WriteLine("</Sentence>");
            }
        }

        depth--;
        WriteLine("</" + header + ">");
    }


    public void WriteForAdvancedTab(String line, String header)
    {
        WriteLine(WriteStartingHeader(header));
        depth++;

        line = line.substring(1, line.length() - 1);

        String[] AllSimpleTab = getAllTabOfString(line);

        CleanLine(AllSimpleTab);

        for (String string : AllSimpleTab) {
            string = string.trim();
            if (string.isEmpty()) continue;

            WriteLine(WriteStartingHeader(getSubHeader(header)));
            depth++;

            String[] AllOfComplexTab = string.split(",");
            for (String s : AllOfComplexTab) {
                if (s == null || s.trim().isEmpty()) {
                    WriteLine("<LabelSentence/>");
                } else {
                    WriteLine("<LabelSentence>" + s.trim() + "</LabelSentence>");
                }
            }
            depth--;
            WriteLine(WriteEndingHeader(getSubHeader(header)));
        }

        depth--;
        WriteLine(WriteEndingHeader(header));
    }


    /***********************************************************************/
    /* Helper                                                              */
    /***********************************************************************/

    public String[] getAllTabOfString(String line)
    {
        char c;
        int i = 0;
        StringBuilder Tab1 = new StringBuilder();
        ArrayList<String> TableauListe = new ArrayList<>();

        while(i < line.length() )
        {
            while(((c = line.charAt(i)) != ']'))
            {
                i++;
                Tab1.append(c);
            }
            Tab1.append(c);
            i++;
            TableauListe.add(Tab1.toString());
            Tab1 = new StringBuilder();
        }


        for (i = 0; i < TableauListe.size(); i++) {
            String cleaned = removeOuterCommas(TableauListe.get(i));
            TableauListe.set(i, cleaned);
        }


        return TableauListe.toArray(new String[0]);
    }

    public String getSubHeader(String header)
    {
        return switch (header) {
            case "Labels" -> "Label";
            case "Localizations" -> "Localization";
            case "LabelsLocalizationsBySentence" -> "Sentence";
            case "labelCUIS" -> "labelCUI";
            case "LocalizationsCUIS" -> "LocalizationsCUI";
            default ->  // fallback
                    header.endsWith("s") ? header.substring(0, header.length() - 1) : "Item";
        };
    }
    public String GetIndentLevel()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++)
        {
            sb.append('\t');
        }
        return sb.toString();
    }

    public void CleanLine(String[] line)
    {
        for(int i = 0; i < line.length; i++)
        {
            line[i] = line[i]
                    .replace("[", "")
                    .replace("]", "")
                    .replace("\"", "")
                    .replace("'", "")
                    .trim()
                    .replaceAll("\\s+", " ");}
    }

    public static String removeOuterCommas(String line) {
        StringBuilder result = new StringBuilder();
        int depth = 0;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '[') depth++;
            if (c == ']') depth--;

            // On garde tout sauf les virgules qui sont à l’extérieur des crochets
            if (c != ',' || depth > 0) {
                result.append(c);
            }
        }

        return result.toString().trim();
    }

}