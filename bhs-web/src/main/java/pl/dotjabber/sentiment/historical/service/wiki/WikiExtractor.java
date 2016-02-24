package pl.dotjabber.sentiment.historical.service.wiki;

import org.apache.commons.lang3.StringUtils;
import org.wikipedia.miner.model.Wikipedia;

import java.io.Closeable;

public class WikiExtractor implements Closeable {
    private Wikipedia wikipedia;
    private WikiThesaurus tezaurus;

    public WikiExtractor(Wikipedia wikipedia) {
        this.wikipedia = wikipedia;
        tezaurus = new WikiThesaurus(this.wikipedia);
    }
    
    public String getArticleForTerm(String term, boolean strictMatching) {
        String shortDesc = tezaurus.getArticleContent(term, strictMatching);

        if (StringUtils.isBlank(shortDesc)) {
            return "";
        } else {
            return shortDesc;
        }
    }

    @Override
    public void close() {
        if(wikipedia != null) wikipedia.close();
    }
}
