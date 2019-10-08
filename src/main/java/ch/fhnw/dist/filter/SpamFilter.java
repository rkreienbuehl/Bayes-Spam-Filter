package ch.fhnw.dist.filter;

import ch.fhnw.dist.evaluate.Word;
import ch.fhnw.dist.evaluate.WordProcessor;

import java.util.HashMap;
import java.util.Map;

public class SpamFilter {
    public enum SpamOrHam {
        SPAM, HAM;
    }

    private Map<String, Word> words = new HashMap<>();
    private WordProcessor wp = new WordProcessor();

    public SpamFilter() {}

    public void learn(String mail, SpamOrHam spam) {
        wp.setContent(mail);
        Map<String, Word> mailWords = wp.processContent();

    }
}
