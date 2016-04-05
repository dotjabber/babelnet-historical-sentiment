package pl.dotjabber.sentiment.historical.util;

import it.uniroma1.lcl.babelfy.commons.annotation.SemanticAnnotation;

import java.util.Comparator;

public class SemanticAnnotationComparator implements Comparator<SemanticAnnotation> {

    @Override
    public int compare(SemanticAnnotation sa, SemanticAnnotation sb) {
        return (int) Math.signum(
                (sb.getCoherenceScore() + sb.getScore() + sb.getGlobalScore()) -
                        (sa.getCoherenceScore() + sa.getScore() + sa.getGlobalScore())
        );
    }
}
