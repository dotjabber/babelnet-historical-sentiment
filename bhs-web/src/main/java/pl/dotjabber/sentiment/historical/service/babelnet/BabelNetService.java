package pl.dotjabber.sentiment.historical.service.babelnet;

import it.uniroma1.lcl.babelnet.*;
import it.uniroma1.lcl.babelnet.data.BabelGloss;
import it.uniroma1.lcl.babelnet.data.BabelPOS;
import it.uniroma1.lcl.babelnet.data.BabelPointer;
import it.uniroma1.lcl.babelnet.data.BabelSenseSource;
import it.uniroma1.lcl.jlt.util.Language;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.dotjabber.sentiment.historical.util.SynsetElement;
import pl.dotjabber.sentiment.historical.util.SynsetMap;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BabelNetService {
    private SynsetMap synsets;
    private BabelNet babelNet;

    @Value("${babelnet.cache.path}")
    private String cachePath;

    @Value("${babelnet.config.path}")
    private String configPath;

    @Autowired
    private BabelfyService babelfyService;

    @PostConstruct
    private void init() throws IOException {
        synsets = new SynsetMap(cachePath);

        BabelNetConfiguration babelNetConfiguration = BabelNetConfiguration.getInstance();
        babelNetConfiguration.setConfigurationFile(new File(configPath));
        babelNet = BabelNet.getInstance();
    }

    public List<SynsetElement> getCloud(String phrase) throws IOException {
        List<SynsetElement> derivedSynsets = new ArrayList<>();

        String mainId = babelfyService.getMainSynsetId(phrase);

        // if babelfy did not handled it, then go raw.
        if(mainId == null) {
            List<BabelSynset> mainSynsets = babelNet.getSynsets(phrase, Language.EN, BabelPOS.NOUN, BabelSenseSource.WIKIDATA);
            BabelSynset mainSynset = mainSynsets.get(0);
            mainId = getId(mainSynset.getId());
        }

        BabelSynset mainSynset = babelNet.getSynset(new BabelSynsetID(mainId));

        if (!synsets.containsKey(mainId)) {
            SynsetElement mainElement = new SynsetElement(mainId, (mainSynset.getImage() != null) ? mainSynset.getImage().getURL() : "");

            for (BabelGloss childGloss : mainSynset.getGlosses()) {
                mainElement.addGloss(childGloss.getGloss());
            }

            synsets.put(mainId, mainElement);
        }

        derivedSynsets.add(synsets.get(mainId));
        derivedSynsets.addAll(getCloud(mainId, 0));

        return derivedSynsets;
    }

    private String getId(BabelSynsetID synsetId) {
        String id = synsetId.getID();
        id = id.replace("EN_r_", "").replace("_0.00000_0.00000", "");
        return id;
    }

    private List<SynsetElement> getCloud(String parentId, int depth) throws IOException {
        List<SynsetElement> children = new ArrayList<>();

        BabelSynset parentSynset = babelNet.getSynset(new BabelSynsetID(parentId));
        List<BabelSynsetIDRelation> edges = parentSynset.getEdges(BabelPointer.SEMANTICALLY_RELATED);

        for(int i = 0; i < ((edges.size() > 50) ? 50 : edges.size()); i++) {
            BabelSynsetIDRelation edge = edges.get(i);
            String childId = getId(edge.getBabelSynsetIDTarget());

            if(!synsets.containsKey(childId)) {
                BabelSynset childSynset = babelNet.getSynset(new BabelSynsetID(childId));

                SynsetElement childElement = new SynsetElement(childId, (childSynset.getImage() != null) ? childSynset.getImage().getURL() : "");
                for(BabelGloss childGloss : childSynset.getGlosses()) {
                    childElement.addGloss(childGloss.getGloss());
                }

                synsets.put(childId, childElement);
            }

            children.add(synsets.get(childId));

            if(depth > 0) {
                children.addAll(getCloud(childId, depth - 1));
            }
        }

        return children;
    }
}
