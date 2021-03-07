package gr.ste.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public List<String[]> read(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader csvReader = new BufferedReader(fileReader);
        List<String[]> contents = new ArrayList<>();

        String row;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            contents.add(data);
        }
        csvReader.close();

        return contents;
    }
}
