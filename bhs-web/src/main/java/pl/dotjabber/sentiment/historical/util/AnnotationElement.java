package pl.dotjabber.sentiment.historical.util;

public class AnnotationElement {
    private static final String SEPARATOR = "<sep />";

    private String synsetId;
    private String phrase;

    public AnnotationElement(String line) {
        if(line.contains(SEPARATOR)) {
            String elements[] = line.split(SEPARATOR);

            phrase = (elements.length > 0) ? elements[0] : null;
            synsetId = (elements.length > 1) ? elements[1] : null;
        }
    }

    public AnnotationElement(String phrase, String synsetId) {
        this.synsetId = synsetId;
        this.phrase = phrase;

    }

    public String getPhrase() {
        return phrase;
    }

    public String getSynsetId() {
        return synsetId;
    }

    @Override
    public String toString() {
        return phrase + SEPARATOR + synsetId + "\n";
    }
}
