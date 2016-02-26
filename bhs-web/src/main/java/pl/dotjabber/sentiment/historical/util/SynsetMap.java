package pl.dotjabber.sentiment.historical.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class SynsetMap extends HashMap<String, SynsetElement> {
    private String filePath;

    public SynsetMap(String path) throws IOException {
        filePath = path;
        File file = new File(path);

        if (file.exists()) {
            Scanner scanner = new Scanner(file);

            while(scanner.hasNext()) {
                SynsetElement element = new SynsetElement(scanner.nextLine());
                super.put(element.getSynsetId(), element);
            }

            scanner.close();

        } else {
            file.createNewFile();
        }
    }

    @Override
    public SynsetElement put(String s, SynsetElement synsetElement) {
        try {
            Files.write(Paths.get(filePath), synsetElement.toString().getBytes(), StandardOpenOption.APPEND);
            return super.put(s, synsetElement);

        } catch (IOException e) {
            return null;
        }
    }
}
