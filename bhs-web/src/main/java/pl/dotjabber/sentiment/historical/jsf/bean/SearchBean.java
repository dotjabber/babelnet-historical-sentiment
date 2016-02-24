package pl.dotjabber.sentiment.historical.jsf.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.dotjabber.sentiment.historical.service.SearchResponse;
import pl.dotjabber.sentiment.historical.service.SearchService;
import pl.dotjabber.sentiment.historical.service.babelnet.BabelNetService;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import java.io.IOException;

@Component
@Scope("view")
public class SearchBean {
    private String query;
    private String babelnetVersion;
    private boolean useWikipedia;
    private boolean resultVisible;

    private SearchResponse response;

    @Autowired
    private SearchService searchService;

    public void search(ActionEvent actionEvent) throws IOException {
        response = searchService.search(query, useWikipedia);
        setResultVisible(response != null);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isResultVisible() {
        return resultVisible;
    }

    public void setResultVisible(boolean resultVisible) {
        this.resultVisible = resultVisible;
    }

    public String getBabelnetVersion() {
        return babelnetVersion;
    }

    public void setBabelnetVersion(String babelnetVersion) {
        this.babelnetVersion = babelnetVersion;
    }

    public boolean getUseWikipedia() {
        return useWikipedia;
    }

    public void setUseWikipedia(boolean useWikipedia) {
        this.useWikipedia = useWikipedia;
    }

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}
