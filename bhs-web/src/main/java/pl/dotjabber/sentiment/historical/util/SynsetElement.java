package pl.dotjabber.sentiment.historical.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SynsetElement {
    private static final String SEPARATOR = "<sep />";

    private String synsetId;
    private String imageUrl;
    private List<String> glosses;

    public SynsetElement(String synsetId, String imageUrl) {
        glosses = new ArrayList<>();
        this.synsetId = synsetId;
        this.imageUrl = imageUrl;
    }

    public SynsetElement(String line) {
        glosses = new ArrayList<>();

        if(line.contains(SEPARATOR)) {
            String elements[] = line.split(SEPARATOR);
            synsetId = elements[0];
            imageUrl = elements[1];
            glosses.addAll(Arrays.asList(elements).subList(2, elements.length));
        }
    }

    public List<String> getGlosses() {
        return glosses;
    }

    public void addGloss(String gloss) {
        glosses.add(gloss);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSynsetId() {
        return synsetId;
    }

    public void setSynsetId(String synsetId) {
        this.synsetId = synsetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SynsetElement that = (SynsetElement) o;
        return synsetId.equals(that.synsetId);
    }

    @Override
    public int hashCode() {
        return synsetId.hashCode();
    }

    @Override
    public String toString() {
        return synsetId + SEPARATOR + imageUrl + SEPARATOR + String.join(SEPARATOR, glosses) + "\n";
    }
}