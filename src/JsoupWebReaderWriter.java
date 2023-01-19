package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Safelist;

public class JsoupWebReaderWriter {

    public static String output(String url) throws IOException {
     Document doc = Jsoup.connect(url).maxBodySize(0).timeout(0).get();
     
     if (doc == null)
      return "fel url";
     doc.outputSettings(new Document.OutputSettings().prettyPrint(false));// makes
      //html() preserve linebreaks
      // and spacing
      doc.select("br").append("\\n");
      doc.select("p").prepend("\\n\\n");
      String s = doc.html().replaceAll("\\\\n", "\n");
      //s.replaceAll("\u00a0", " ");
      
      String text = Jsoup.clean(s, "", Safelist.none(), new
      Document.OutputSettings().prettyPrint(false));
      String hej = Parser.unescapeEntities(text, false);
     return hej;
      }

    public static String readResultPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).maxBodySize(0).timeout(6000).get();

        String pageText = "";
        for (Element table : doc.select("table")) {
            for (Element row : table.select("tr")) {
                String text = "";
                for (Element nobr : row.select("td").select("nobr")) {
                    text = text + nobr.text() + " ";

                }
                if (!text.equals("")) {
                    pageText = pageText + text + "\n";
                }
            }
        }
        return pageText;
    }

    public static void writeRawResults(String url) throws IOException {
        String rawResults = readResultPage(url);

        BufferedWriter writer = new BufferedWriter(new FileWriter("rawResults.txt", false));
        writer.write(rawResults);
        writer.close();
    }

    public static void main(String[] args) throws Exception {
        //////System.out.println(doc);

        String urlString = "https://livetiming.se/results.php?cid=7052&session=0&all=1";

        System.out.println(readResultPage(urlString));

    }
}
