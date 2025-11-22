package Benchmark;

import DOMParser.DOMParserDTDImages;
import DOMParser.DOMParserXSDImages;
import SAXParser.SAXParserDTDImages;
import SAXParser.SAXParserXSDImages;
import Utils.ProjectConfig;

import java.util.ArrayList;
import java.util.List;

import static CsvConverterToXml.Orchestre.convertCSVtoXML;

public class Benchmarks {


    // Mesure mémoire
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }



    public static String[] run(String titre, Runnable parser) {


        System.out.println("\n===== " + titre + " =====");

        if (ProjectConfig.getBool("benchmark.gc.enabled")) {
            System.gc();
        }

        try {
            Thread.sleep(ProjectConfig.getInt("benchmark.sleep.ms"));
        } catch (Exception _) {}

        long startTime = System.currentTimeMillis();
        long memBefore = getUsedMemory();

        parser.run();

        long memAfter = getUsedMemory();
        long endTime = System.currentTimeMillis();

        double timeSeconds = (endTime - startTime) / 1000.0;
        long memUsed = (memAfter - memBefore) / (1024*1024);
        long memAfterMb = (memAfter) / (1024*1024);

        System.out.println("[Timer] " + timeSeconds + " sec");
        System.out.println("[Mémoire] Avant : " + (memBefore / (1024*1024)) + " MB");
        System.out.println("[Mémoire] Après  : " + memAfterMb + " MB");
        System.out.println("[Mémoire] Utilisée : " + memUsed + " MB");

        return new String[]{
                titre,
                String.format("%.3f", timeSeconds),
                String.valueOf(memAfterMb),
                String.valueOf(memUsed)
        };
    }


    // === Lancer tous les tests ===
    public static void runAll() {

        // Tableau interne de stockage des résultats
        List<String[]> results = new ArrayList<>();

        results.add(run("SAX + DTD", SAXParserDTDImages::main));
        results.add(run("SAX + XSD", SAXParserXSDImages::main));
        results.add(run("DOM + DTD", DOMParserDTDImages::main));
        results.add(run("DOM + XSD", DOMParserXSDImages::main));

        // Affichage propre
        System.out.println("\n============================================");
        System.out.println("              TABLEAU COMPARATIF            ");
        System.out.println("============================================");
        System.out.printf("%-15s | %-12s | %-14s | %-15s%n",
                "Parser", "Temps (s)", "Mémoire (MB)", "Utilisée (MB)");
        System.out.println("--------------------------------------------");

        for (String[] r : results) {
            System.out.printf("%-15s | %-12s | %-14s | %-15s%n",
                    r[0], r[1], r[2], r[3]);
        }


        System.out.println("============================================");
    }







    // === Lancer le convertisseur seul ===
    public static void runConverterOnly() {

        int iterations = ProjectConfig.getInt("benchmark.iterations");;
        double totalSeconds = 0;

        for (int i = 0; i < iterations; i++) {

            long start = System.currentTimeMillis();

            convertCSVtoXML();   // relance la conversion complète

            long end = System.currentTimeMillis();
            double seconds = (end - start) / 1000.0;

            totalSeconds += seconds;

            System.out.println("Run " + (i + 1) + " terminé en " + seconds + " sec");
        }

        double average = totalSeconds / iterations;
        System.out.println("\n=== Moyenne sur " + iterations + " runs : " + average + " secondes ===");
    }






}
