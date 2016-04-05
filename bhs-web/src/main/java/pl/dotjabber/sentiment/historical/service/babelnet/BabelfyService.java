package pl.dotjabber.sentiment.historical.service.babelnet;

import it.uniroma1.lcl.babelfy.commons.BabelfyConfiguration;
import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;
import it.uniroma1.lcl.babelfy.core.Babelfy;
import it.uniroma1.lcl.jlt.util.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.dotjabber.sentiment.historical.util.AnnotationElement;
import pl.dotjabber.sentiment.historical.util.AnnotationMap;
import pl.dotjabber.sentiment.historical.util.SemanticAnnotationComparator;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class BabelfyService {
    private Babelfy babelfy;
    private AnnotationMap annotations;
    private SemanticAnnotationComparator comparator;

    @Value("${babelfy.cache.path}")
    private String cachePath;

    @Value("${babelfy.config.path}")
    private String configPath;

    @PostConstruct
    private void init() throws IOException {
        annotations = new AnnotationMap(cachePath);
        comparator = new SemanticAnnotationComparator();

        BabelfyConfiguration babelfyConfiguration = BabelfyConfiguration.getInstance();
        babelfyConfiguration.setConfigurationFile(new File(configPath));
        babelfy = new Babelfy();
    }

    public String getMainSynsetId(String phrase) {
        String synsetId = null;

        if(!annotations.containsKey(phrase)) {
            List<SemanticAnnotation> semanticAnnotations = babelfy.babelfy(phrase, Language.EN);

            if(semanticAnnotations.size() > 0) {
                Collections.sort(semanticAnnotations, comparator);
                SemanticAnnotation mostProbable = semanticAnnotations.get(0);

                AnnotationElement element = new AnnotationElement(phrase, mostProbable.getBabelSynsetID());
                annotations.put(phrase, element);

                synsetId = mostProbable.getBabelSynsetID();
            }

        } else {
            synsetId = annotations.get(phrase).getSynsetId();
        }

        return synsetId;
    }
}
