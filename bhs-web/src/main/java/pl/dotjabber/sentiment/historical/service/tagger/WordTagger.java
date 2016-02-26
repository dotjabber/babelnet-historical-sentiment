package pl.dotjabber.sentiment.historical.service.tagger;

import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordTagger {
	private static final String NGRAM_CONTENT_REGEX = ".*[0-9]+.*";
	private static final String[] NGRAM_CONTENT_REMOVE = {
		"<x/>", "&apos;", ".", ",", "`", 
		"\"", "?", "!", ")",  "(", "'", 
		";",  ":", "\"", "]", "[", "“", 
		"\n", "\r", "-", "/", "\\", "<", 
		">", "|"
	};

	private static WordTagger tagger;
	
	public static WordTagger getTagger() throws IOException {
		if(tagger == null) {
			tagger = new WordTagger();
		}
		
		return tagger;
	}
	
	public static String getClean(String word) {
		word = word.toLowerCase();
		
		for(String removeToken : NGRAM_CONTENT_REMOVE) {
			word = word.replace(removeToken, "");
		}
		
    	return word.trim();
	}

	private DictionaryLookup lookup;

	public WordTagger() throws IOException {
		lookup = new DictionaryLookup(Dictionary.getForLanguage("en"));
	}

	public DictionaryLookup getLookup() {
		return lookup;
	}
	
    public List<Word> getTagged(String content) {
    	Map<String, Word> wordMap = new HashMap<>();
    	
    	content = getClean(content);
    	if(!content.matches(NGRAM_CONTENT_REGEX)) {
    		
	        for(WordData wordData : getLookup().lookup(content)) {
	        	String wordContent = wordData.getStem().toString();
	        	
	        	Word word = wordMap.get(wordContent);
	        	if(word == null) {
	        		word = new Word(wordContent);
	        		wordMap.put(wordContent, word);
	        	}
	        	
	        	word.addTag(wordData.getTag().toString());
	        }
    	}
        
        return new ArrayList<>(wordMap.values());
    }
}
