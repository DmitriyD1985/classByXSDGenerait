package task3;


import static task3.HamletXMLParser.countAmountUniqueWords;
import static task3.HamletXMLParser.countTheXMLTagsDistribution;
import static task3.HamletXMLParser.parserDOM;
import static task3.HamletXMLParser.parserObject;
import static task3.HamletXMLParser.parserSAX;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileMain {
    public static void main(String[] args) {

        parserSAX();
        parserDOM();
        parserObject();
        countAmountUniqueWords();
        countTheXMLTagsDistribution();
    }

    public static void writeResult(String data) {
        String print = '\n' + data;
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File("G:\\tasksFromDaria\\src\\main\\java\\task3\\task3Result.txt"), true);
            os.write(print.getBytes(), 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
