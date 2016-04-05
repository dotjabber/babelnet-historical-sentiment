package pl.dotjabber.sentiment.historical.util;

import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Scanner;

public class AnnotationMap extends HashMap<String, AnnotationElement> {
    private String filePath;

    public AnnotationMap(String path) throws IOException {
        filePath = path;
        File file = new File(path);

        if (file.exists()) {
            Scanner scanner = new Scanner(file);

            while(scanner.hasNext()) {
                AnnotationElement element = new AnnotationElement(scanner.nextLine());
                super.put(element.getPhrase(), element);
            }

            scanner.close();

        } else {
            file.createNewFile();
        }
    }

    @Override
    public AnnotationElement put(String phrase, AnnotationElement annotationElement) {
        try {
            Files.write(Paths.get(filePath), annotationElement.toString().getBytes(), StandardOpenOption.APPEND);
            return super.put(phrase, annotationElement);

        } catch (IOException e) {
            return null;
        }
    }
}
