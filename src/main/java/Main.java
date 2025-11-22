import java.util.Scanner;

// Imports vers tes classes (à adapter selon leur package réel)
import Benchmark.Benchmarks;
import CsvConverterToXml.Orchestre;
import DOMParser.DOMParserXSDImages;
import SAXParser.SAXParserDTDImages;
import SAXParser.SAXParserXSDImages;
import DOMParser.DOMParserDTDImages;
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
            System.out.println("6. Lancer Test Comparatif (validation DTD/XSD)");
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
                    Benchmarks.run("SAX + DTD", () -> SAXParserDTDImages.main());
                    break;

                case 3:
                    System.out.println("→ Lancement du parser SAX avec XSD...");
                    Benchmarks.run("SAX + XSD", () -> SAXParserXSDImages.main());
                    break;

                case 4:
                    Benchmarks.run("DOM + DTD", () -> DOMParserDTDImages.main());
                    break;

                case 5:
                    Benchmarks.run("DOM + XSD", () -> DOMParserXSDImages.main());
                    break;

                case 6:
                    System.out.println("→ Lancement des benchmarks...");
                    Benchmark.Benchmarks.runAll();
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

    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
