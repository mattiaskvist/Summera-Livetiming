package src;

import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws IOException {
        String url = "https://livetiming.se/results.php?cid=7052&session=0&all=1";
        JsoupWebReaderWriter.writeRawResults(url);
        ResultFileAnalyzer.cleanUp();
        ResultFileWriter.writeResultFile();
    }
}
