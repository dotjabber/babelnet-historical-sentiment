package pl.dotjabber.sentiment.historical.service.tagger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Word {
	private String content;
	private Set<String> tags;
	private int count;
	private double normalization;

	public Word(String content) {
		this.content = content;
		this.count = 1;
		this.tags = new HashSet<String>();
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void addTag(String o) {
		tags.add(o);
	}
	
	public void addTags(Collection<String> t) {
		tags.addAll(t);
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void incCount() {
		this.count ++;
	}

	public double getNormalization() {
		return normalization;
	}

	public void setNormalization(double normalization) {
		this.normalization = normalization;
	}

	@Override
	public String toString() {
		return content + "(" + normalization + ")";// + ":" + tags.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Word) {
			Word wordObj = (Word) obj;
			return getContent().equals(wordObj.getContent());
			
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return content.hashCode();
	}
}
