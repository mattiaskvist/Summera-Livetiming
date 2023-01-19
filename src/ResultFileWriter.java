package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.io.*;

public class ResultFileWriter {

    File file = new File("rawResults.txt");
    // BufferedWriter writer;
    static HashMap<String, String> resultatMap = new HashMap<String, String>();
    static HashMap<String, Integer> finaMap = new HashMap<String, Integer>();
    static String gren = "";
    static String resultat;
    static String key;
    static String tid = "";
    static String placering = "";
    static String födelseår = "";
    static String klass = "";
    static String namn = "";
    static int fina;

    public static void writeResultFile() {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("rawResults.txt"));
            String line = reader.readLine();
            while (line != null) {
                ArrayList<String> string = new ArrayList<String>(Arrays.asList(line.split("\\s+")));
                mapResult(string);
                line = reader.readLine();
            }
            reader.close();
        } catch (

        IOException e) {
            System.out.println("Something went wrong: failed to open rawResults.txt");
        }

        writeIndividualResults(resultatMap, finaMap);
        writeFinaSummary(finaMap);
    }

    public static void mapResult(ArrayList<String> string) {
        if (string.get(0).equals("Gren")) {
            gren = string.get(2) + " " + string.get(3);
            klass = string.get(4);
            return;
        }


        if (!Pattern.matches("[0-9]+",string.get(0))) return;

            namn = string.get(1) + " " + string.get(2);
            placering = string.get(0);
            födelseår = string.get(3);
            key = namn + " " + klass + " " + födelseår;

            switch (placering) {
                case "1":
                    tid = string.get(string.size() - 1);
                    break;
                case "-":
                    tid = "DSQ/DNS";
                    break;
                default:
                    tid = string.get(string.size() - 2);
            }
            calculateFina(gren, klass, tid);
            resultat = gren + " " + tid + " " + fina + ",";
            if (resultatMap.containsKey(key)) {
                resultat = resultatMap.get(key) + resultat;
                fina = finaMap.get(key) + fina;
            }
            resultatMap.put(key, resultat);
            finaMap.put(key, fina);
        
    }

    public static void writeIndividualResults(HashMap<String, String> timeMap, HashMap<String, Integer> finaMap) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("IndividuellaResultat.txt", false));

            for (Map.Entry<String, String> set : timeMap.entrySet()) {
                String line = set.getValue();
                ArrayList<String> string = new ArrayList<String>(Arrays.asList(line.split(",")));
                writer.write(set.getKey() + ":\n");
                for (String resultat : string) {
                    writer.write("    " + resultat + "\n");
                }
                writer.write(finaMap.get(set.getKey()) + "\n");
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFinaSummary(HashMap<String, Integer> finaMap) {
        ArrayList<Integer> points = new ArrayList<Integer>();
        ArrayList<String> ranking = new ArrayList<>();
        int i = 1;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("FinaTotal.txt", false));

            for (Map.Entry<String, Integer> set : finaMap.entrySet()) {
                points.add(set.getValue());
                ranking.add(set.getValue() + " " + set.getKey());

            }
            Collections.sort(points, Collections.reverseOrder());

            for (Integer score : points) {

                for (String string : ranking) {
                    if (string.startsWith(String.valueOf(score))) {
                        writer.write(i + ". " + string + "\n");
                        ranking.remove(string);
                        break;
                    }
                }
                i++;
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calculateFina(String gren, String klass, String tid) {
        BufferedReader baseTimeReader;
        double time;
        if (tid.equals("DSQ/DNS") || tid.equals("-.--")) {
            fina = 0;
            return fina;
        }
        ArrayList<String> tidString = new ArrayList<String>(Arrays.asList(tid.split(":")));
        if (tidString.size() == 2) {
            time = 60 * Double.parseDouble(tidString.get(0)) + Double.parseDouble(tidString.get(1));
        } else
            time = Double.parseDouble(tid);
        double baseTime = 0;

        try {
            baseTimeReader = new BufferedReader(new FileReader("src/BaseTime25m.txt"));
            String line = baseTimeReader.readLine();
            while (line != null) {
                ArrayList<String> string = new ArrayList<String>(Arrays.asList(line.split("\\s+")));

                if (line.startsWith(gren)) {

                    switch (klass) {
                        case "Damer":
                            baseTime = Double.parseDouble(string.get(3));
                            break;

                        case "Herrar":
                            baseTime = Double.parseDouble(string.get(2));
                            break;
                    }
                    break;
                }
                line = baseTimeReader.readLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to open base time file.");
        }

        fina = (int) (1000 * Math.pow(baseTime / time, 3));
        return fina;
    }

    public static void main(String[] args) {

        /*
         * ResultReader splitter = new ResultReader("resultat.txt");
         * ArrayList<String> hamletWords = splitter.getResult();
         * System.out.println(hamletWords.get(3)); // Get the fourth word in hamlet.txt
         */
        //ResultFileWriter writer = new ResultFileWriter("resultat");
    }
}