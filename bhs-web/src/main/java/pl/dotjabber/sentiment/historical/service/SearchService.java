package pl.dotjabber.sentiment.historical.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dotjabber.sentiment.historical.service.babelnet.BabelNetService;
import pl.dotjabber.sentiment.historical.service.sentiment.SentimentService;
import pl.dotjabber.sentiment.historical.service.tagger.CorpusTokenizer;
import pl.dotjabber.sentiment.historical.service.tagger.Word;
import pl.dotjabber.sentiment.historical.service.tagger.WordTagger;
import pl.dotjabber.sentiment.historical.util.SynsetElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class SearchService {

    @Autowired
    private BabelNetService babelNetService;

    @Autowired
    private SentimentService sentimentService;

    public SearchResponse search(String query) throws IOException {
        SearchResponse response = new SearchResponse();

        List<SynsetElement> elements = babelNetService.getCloud(query);
        List<Word> wordList = new ArrayList<>();

        // zbieranie chmury słów
        for (SynsetElement element : elements) {
            boolean flag = response.getImageUrl() == null && element.getImageUrl().contains("http");

            if(flag) {
                response.setImageUrl(element.getImageUrl());
            }

            for (String babelGloss : element.getGlosses()) {
                response.addGloss(babelGloss);

                CorpusTokenizer tokenizer = new CorpusTokenizer(babelGloss);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();

                    List<Word> words = WordTagger.getTagger().getTagged(token);
                    for (Word word : words) {
                        if (wordList.contains(word)) {
                            wordList.get(wordList.indexOf(word)).incCount();

                        } else {
                            wordList.add(word);
                        }
                    }
                }
            }
        }

        // normalizacja wektorów słów
        double denominator = 0;
        for (Word word : wordList) {
            denominator += word.getCount();
        }

        for (Word word : wordList) {
            double counter = word.getCount();
            word.setNormalization(counter / denominator);
        }

        // na podstawie biblioteki wyliczenie sentymentu
        double positives = 0.0;
        double negatives = 0.0;
        double total = 0.0;

        for (Word word : wordList) {
            Map.Entry<Double, Double> sentiment = sentimentService.getSentiment(word);

            positives += sentiment.getKey() * word.getNormalization();
            negatives += sentiment.getValue() * word.getNormalization();
            total += sentiment.getKey() * word.getNormalization() + sentiment.getValue() * word.getNormalization();
        }

        response.setPositiveScore(positives / total);
        response.setNegativeScore(negatives / total);

        return response;
    }
}
