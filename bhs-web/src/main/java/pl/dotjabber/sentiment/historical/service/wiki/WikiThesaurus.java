package pl.dotjabber.sentiment.historical.service.wiki;

import org.wikipedia.miner.model.Article;
import org.wikipedia.miner.model.Wikipedia;

public class WikiThesaurus {
    protected Wikipedia wikipedia;

    public WikiThesaurus(Wikipedia wiki) {
        wikipedia = wiki;
    }
    
    public String getArticleContent(String term, boolean strict) {
    	Article article = wikipedia.getArticleByTitle(term);
    	
    	if (article == null && !strict) {
            article = wikipedia.getMostLikelyArticle(term, null);
        }
    	
    	return article != null && article.getMarkup() != null ? cleanRaw(article.getMarkup()) : "";
    }
    
    public String cleanRaw(String input){
        //Next three lines attempt to get rid of references.
        input= input.replaceAll("<ref>.*?</ref>","");
        input= input.replaceAll("<ref .*?</ref>","");
        input= input.replaceAll("<ref .*?/>","");

        input= input.replaceAll("==[^=]*==", "");
        //I found that anything between curly braces is not needed.

        while (input.contains("{{")){
            int prevLength= input.length();
            input= input.replaceAll("\\{\\{[^{}]*\\}\\}", "");
            if (prevLength == input.length()){
                break;
            }
        }
        //Next line gets rid of links to other Wikipedia pages.
        input= input.replaceAll("\\[\\[([^]]*[|])?([^]]*?)\\]\\]", "$2");
        input= input.replaceAll("<!--.*?-->","");

        return input;
    }
}
