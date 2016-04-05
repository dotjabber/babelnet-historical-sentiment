package pl.dotjabber.sentiment.historical.jsf.bean;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
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
    private boolean resultVisible;

    private SearchResponse response;

    @Autowired
    private SearchService searchService;

    public void search(ActionEvent actionEvent) throws IOException {
        response = searchService.search(query.replace(" ", "_"));
        setResultVisible(response != null);
    }

    public BarChartModel getChart() {
        BarChartModel model = new BarChartModel();

        if(response != null) {
            ChartSeries series = new ChartSeries();
            series.set("Positive", response.getPositiveScore());
            series.set("Negative", response.getNegativeScore());
            model.addSeries(series);
        }

        return model;
    }

    public void setChart(BarChartModel chart) {
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

    public SearchResponse getResponse() {
        return response;
    }

    public void setResponse(SearchResponse response) {
        this.response = response;
    }
}
