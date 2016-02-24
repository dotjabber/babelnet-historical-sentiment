package pl.dotjabber.sentiment.historical.service.wiki;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.wikipedia.miner.model.Wikipedia;
import org.wikipedia.miner.util.WikipediaConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.File;
import java.io.IOException;

@Service
public class WikiExtractorService {
    private WikiExtractor enWikiExtractor;

    @Value("${wiki.config.path}")
    private String wikiConfigPath;

    @PostConstruct
    private void init() throws Exception {
        WikipediaConfiguration enWikiConfig = new WikipediaConfiguration(new File(wikiConfigPath));
        Wikipedia enWikipedia = new Wikipedia(enWikiConfig, true);
        this.enWikiExtractor = new WikiExtractor(enWikipedia);

        //System.out.println(getArticleForTerm("Adolf Hitler", true));
    }

    private void cleanLockFile(String configFilePath) throws IOException {
        File file = new File(configFilePath.replace("wikipedia-config.xml", "db/je.lck"));
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
    }
    
    public String getArticleForTerm(String term, boolean strict) {
        return enWikiExtractor.getArticleForTerm(term.replace("_", " "), strict);
    }

    @PreDestroy
    public void close() throws IOException {
        if(enWikiExtractor != null) enWikiExtractor.close();
    }
}
