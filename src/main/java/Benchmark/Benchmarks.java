package Benchmark;

import DOMParser.DOMParserDTDImages;
import DOMParser.DOMParserXSDImages;
import SAXParser.SAXParserDTDImages;
import SAXParser.SAXParserXSDImages;

import java.util.ArrayList;
import java.util.List;

public class Benchmarks {

    // Mesure mémoire
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    // === Fonction générique pour lancer un parser et mesurer ===
    public static void run(String titre, Runnable parser) {

        System.out.println("\n===== " + titre + " =====");

        System.gc();
        try { Thread.sleep(200); } catch (Exception e) {}

        long startTime = System.currentTimeMillis();
        long memBefore = getUsedMemory();

        parser.run();

        long memAfter = getUsedMemory();
        long endTime = System.currentTimeMillis();

        System.out.println("[Timer] " + titre + " : " + ((endTime - startTime) / 1000.0) + " sec");
        System.out.println("[Mémoire] Avant : " + (memBefore / (1024*1024)) + " MB");
        System.out.println("[Mémoire] Après  : " + (memAfter  / (1024*1024)) + " MB");
        System.out.println("[Mémoire] Utilisée : " + ((memAfter - memBefore) / (1024*1024)) + " MB");
    }

    // === Lancer tous les tests ===
    public static void runAll() {

        // Tableau interne de stockage des résultats
        List<String[]> results = new ArrayList<>();

        results.add(runWithResult("SAX + DTD", () -> SAXParserDTDImages.main()));
        results.add(runWithResult("SAX + XSD", () -> SAXParserXSDImages.main()));
        results.add(runWithResult("DOM + DTD", () -> DOMParserDTDImages.main()));
        results.add(runWithResult("DOM + XSD", () -> DOMParserXSDImages.main()));

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


    private static String[] runWithResult(String titre, Runnable parser) {

        System.out.println("\n===== " + titre + " =====");

        System.gc();
        try { Thread.sleep(200); } catch (Exception e) {}

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
}
