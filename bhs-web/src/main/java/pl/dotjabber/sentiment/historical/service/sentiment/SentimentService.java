package pl.dotjabber.sentiment.historical.service.sentiment;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.dotjabber.sentiment.historical.service.tagger.Word;

import javax.annotation.PostConstruct;

@Service
public class SentimentService {

	@Value("${sentiment.lexicon.positives.path}")
	private String lexiconPosPath;

	@Value("${sentiment.lexicon.negatives.path}")
	private String lexiconNegPath;

	@Value("${sentiment.sentiwordnet.path}")
	private String sentiPath;
	
	private Map<String, Double> positives;
	private Map<String, Double> negatives;

	@PostConstruct
	public void init() throws IOException {
		positives = new HashMap<>();
		negatives = new HashMap<>();

		// lexicon
		List<String> posDict = FileUtils.readLines(new File(lexiconPosPath), "utf-8");
		List<String> negList = FileUtils.readLines(new File(lexiconNegPath), "utf-8");

		posDict.stream().filter(posEl -> !posEl.contains(";")).forEach(posEl -> positives.put(posEl, 1.0));
		negList.stream().filter(negEl -> !negEl.contains(";")).forEach(negEl -> negatives.put(negEl, 1.0));

		// sentiwordnet
		List<String> elemDict = FileUtils.readLines(new File(sentiPath), "utf-8");

		elemDict.stream().filter(elem -> !elem.startsWith("#")).forEach(elem -> {
			String[] cols = elem.split("\t");

			String word = cols[4].replaceAll("#[0-9]+", "");
			if (!word.contains("_") && !word.contains(" ")) {
				Double posScore = Double.parseDouble(cols[2]);
				Double negScore = Double.parseDouble(cols[3]);

				positives.put(word, posScore);
				negatives.put(word, negScore);
			}
		});
	}
	
	public Map.Entry<Double, Double> getSentiment(Word word) {
		double posScore = positives.containsKey(word.getContent()) ? positives.get(word.getContent()) : 0;
		double negScore = negatives.containsKey(word.getContent()) ? negatives.get(word.getContent()) : 0;
		
		return new AbstractMap.SimpleEntry<>(posScore, negScore);
	}
}