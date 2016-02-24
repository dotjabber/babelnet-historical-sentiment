package pl.dotjabber.sentiment.historical.service;

import java.util.HashSet;
import java.util.Set;

public class SearchResponse {
    private String imageUrl;
    private double positiveScore = 0;
    private double negativeScore = 0;
    private Set<String> glosses;

    public SearchResponse() {
        glosses = new HashSet<>();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPositiveScore() {
        return positiveScore;
    }

    public void setPositiveScore(double positiveScore) {
        this.positiveScore = positiveScore;
    }

    public double getNegativeScore() {
        return negativeScore;
    }

    public void setNegativeScore(double negativeScore) {
        this.negativeScore = negativeScore;
    }

    public Set<String> getGlosses() {
        return glosses;
    }

    public void addGloss(String gloss) {
        glosses.add(gloss);
    }
}
