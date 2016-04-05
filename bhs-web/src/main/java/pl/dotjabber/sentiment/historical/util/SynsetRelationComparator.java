package pl.dotjabber.sentiment.historical.util;

import it.uniroma1.lcl.babelnet.BabelSynsetIDRelation;

import java.util.Comparator;

public class SynsetRelationComparator implements Comparator<BabelSynsetIDRelation> {

    @Override
    public int compare(BabelSynsetIDRelation sa, BabelSynsetIDRelation sb) {
        return (int) Math.signum((sb.getNormalizedWeight()) - (sa.getNormalizedWeight()));
    }
}
