package gr.ste.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CsvReader {
    private final String fileName;

    public CsvReader(String fileName) {
        this.fileName = fileName;
    }

    public String[] read() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            return data;
        }
        csvReader.close();
        return null;
    }
}
