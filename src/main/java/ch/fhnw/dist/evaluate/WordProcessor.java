package ch.fhnw.dist.evaluate;


import ch.fhnw.dist.filter.SpamFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The Wordprocessor receives the Content of a Mail an split the content into Words. Each word is then put into a List.
 */
public class WordProcessor {

    /**
     * Class Logger
     */
    private Logger logger = LogManager.getLogger(WordProcessor.class);

    /**
     * The Content of a Mail
     */
    private String content;

    /**
     * Type of Content (is determined from which folder it was read)
     */
    private SpamFilter.SpamOrHam spamOrHam;


    /**
     * set the content for the WordProcessor
     *
     * @param content   Content from Mail as String
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * set the content for the WordProcessor and the type of Data
     *
     * @param content   Content from Mail as String
     * @param spamOrHam Type of Content SPAM or HAM from Enum SpamOrHam
     */
    public void setContentWithIdentification(String content, SpamFilter.SpamOrHam spamOrHam) {
        this.content = content;
        this.spamOrHam = spamOrHam;
    }

    /**
     * Process the content of a mail
     *
     * @return get a Map of key (the word) as <String> and Word Object <Word>
     */
    public Map<String, Word> processContentWithIdentification() {

        Map<String, Word> wordlist = new HashMap<>();

        String[] splittedContent = this.content.toLowerCase().split(" ");

        for (String word : splittedContent) {

            if (!wordlist.containsKey(word)) {
                if (this.spamOrHam == SpamFilter.SpamOrHam.HAM) {
                    Word listW = new Word(word, SpamFilter.SpamOrHam.HAM);
                    wordlist.put(word, listW);
                } else {
                    Word listW = new Word(word, SpamFilter.SpamOrHam.SPAM);
                    wordlist.put(word, listW);
                }
            }
        }
        return wordlist;
    }

    /**
     * Split the content and returns a List of words.
     * @return List of Words
     */
    public List<String> processContent() {
        List<String> wordlist = new LinkedList<>();

        String[] splittedContent = this.content.toLowerCase().split(" ");

        for (String word : splittedContent) {
            if (!wordlist.contains(word)) {
                wordlist.add(word);
            }
        }

        return wordlist;
    }
}
