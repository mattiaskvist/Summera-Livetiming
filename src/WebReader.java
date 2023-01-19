package src;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class WebReader{
public static void main(String[] args) throws IOException {
    
    String adress = "https://livetiming.se/results.php?cid=7052&session=0&all=1";
    URL pageLocation = new URL(adress);
    Scanner in = new Scanner(pageLocation.openStream());

    while(in.hasNext()) {
        String line = in.next();
        if (line.contains("Mattias")){
            System.out.println(line);
        }
    }
}
}