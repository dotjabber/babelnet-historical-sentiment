package pl.dotjabber.sentiment.historical.service.tagger;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CorpusTokenizer extends StringTokenizer {
	private List<String> tokens;
	private int windowSize = 1;
	private int maxWindowSize = 1;
	private int windowPos = 0;

	public CorpusTokenizer(String str) {
		super(str);
		
		tokens = new ArrayList<String>();

		// tokenizer
		StringTokenizer tokenizer = new StringTokenizer(str);
		while (tokenizer.hasMoreElements()) {
			tokens.add(tokenizer.nextToken());
		}

		if(tokens.size() == 0) {
			tokens.add(str);
		}
	}
	
	public CorpusTokenizer(String str, int size) {
		this(str);
		maxWindowSize = size;
	}

	public boolean hasMoreTokens() {
		if(maxWindowSize == -1) {
			return tokens.size() >= windowSize;
			
		} else {
			return maxWindowSize >= windowSize && tokens.size() >= windowSize;
		}
	}

	public String nextToken() {
		String result = null;

		for(int j = 0; j < windowSize; j++) {
			if(result == null) {
				result = tokens.get(windowPos + j);
				
			} else {
				result = result + " " + tokens.get(windowPos + j);
			}
		}
		
		windowPos++;
		
		if(windowPos + windowSize > tokens.size()) {
			windowPos = 0;
			windowSize++;
		}
		
		return result;
	}
}
