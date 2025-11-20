import java.util.Scanner;

// Imports vers tes classes (à adapter selon leur package réel)
import CsvConverterToXml.Orchestre;
import SAXParser.SAXParserDTDImages;
import SAXParser.SAXParserXSDImages;
// import SAXParser.SAXParserXSDImages;
// import DOMParser.DOMParserDTDImages;
// import DOMParser.DOMParserXSDImages;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {

            System.out.println("\n===== MENU PROJET XML =====");
            System.out.println("1. Lancer Convertisseur CSV → XML");
            System.out.println("2. Lancer SAX (validation DTD)");
            System.out.println("3. Lancer SAX (validation XSD)");
            System.out.println("4. Lancer DOM (validation DTD)");
            System.out.println("5. Lancer DOM (validation XSD)");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");

            choix = sc.nextInt();

            switch (choix) {
                case 1:
                    System.out.println("→ Lancement du convertisseur CSV → XML...");
                    Orchestre.main();   // à adapter selon ton code
                    break;

                case 2:
                    System.out.println("→ Lancement du parser SAX avec DTD...");
                    SAXParserDTDImages.main(null);
                    break;

                case 3:
                    System.out.println("→ Lancement du parser SAX avec XSD...");
                    SAXParserXSDImages.main();
                    System.out.println("Parser XSD pas encore implémenté !");
                    break;

                case 4:
                    System.out.println("Lancement du parser DOM avec DTD...");
                    // DOMParserDTDImages.main(null);
                    System.out.println("Parser DOM DTD pas encore implémenté !");
                    break;

                case 5:
                    System.out.println("Lancement du parser DOM avec XSD...");
                    // DOMParserXSDImages.main(null);
                    System.out.println("Parser DOM XSD pas encore implémenté !");
                    break;

                case 0:
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Choix invalide, réessayez.");
                    break;
            }
        }

        sc.close();
    }
}
