package src;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ResultFileAnalyzer {

    public static void cleanUp() throws IOException{

        File inputFile = new File("rawResults.txt");
        File tempFile = new File("myTempFile.txt");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        List<String> lineToRemove = Arrays.asList("Resultat", "2022", "Plats", "Bassäng", "Grenen", "resultat", "0m:", "(", "Namn","Sponsor");
        String currentLine;

        while((currentLine = reader.readLine()) != null) {
            // trim newline when comparing with lineToRemove
            String trimmedLine = currentLine.trim();
            boolean print = true;
            for (String string : lineToRemove) {
                if(trimmedLine.contains(string)) print = false;
            }
            if (print) writer.write(currentLine + System.getProperty("line.separator"));

        }
        writer.close(); 
        reader.close(); 
        boolean successful = tempFile.renameTo(inputFile);
        System.out.println(successful);
    }


    public ResultFileAnalyzer(String filename) throws IOException {
       // resultReader = new ResultFileReader(filename);
        cleanUp();

    }

    public static void main(String[] args) throws IOException {
        ResultFileAnalyzer analyzer = new ResultFileAnalyzer("resultat");
    }
}
