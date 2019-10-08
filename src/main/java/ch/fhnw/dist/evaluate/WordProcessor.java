package ch.fhnw.dist.evaluate;


import ch.fhnw.dist.filter.SpamFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class WordProcessor {

    private Logger logger = LogManager.getLogger(WordProcessor.class);

    private String content;
    private SpamFilter.SpamOrHam spamOrHam;

    /**
     * set the content for the WordProcessor and the type of Data
     *
     * @param content   Content from Mail as String
     * @param spamOrHam Type of Content SPAM or HAM from Enum SpamOrHam
     */
    public void setContent(String content, SpamFilter.SpamOrHam spamOrHam) {
        this.content = content;
        this.spamOrHam = spamOrHam;
    }

    /**
     * Process the content of a mail
     *
     * @return get a Map of key (the word) as <String> and Word Object <Word>
     */
    public Map<String, Word> processContent() {

        Map<String, Word> wordlist = new HashMap<>();

        String[] splittedContent = this.content.split(" ");

        for (String word : splittedContent) {

            if (wordlist.containsKey(word)) {
                Word fromList = wordlist.get(word);
                if (this.spamOrHam == SpamFilter.SpamOrHam.HAM) {
                    fromList.setHam(fromList.getHam() + 1);
                } else {
                    fromList.setSpam(fromList.getSpam() + 1);
                }
            } else {
                if (this.spamOrHam == SpamFilter.SpamOrHam.HAM) {
                    Word listW = new Word(1, 0, 0.0, 0.0);
                    wordlist.put(word, listW);
                } else {
                    Word listW = new Word(0, 1, 0.0, 0.0);
                    wordlist.put(word, listW);
                }
            }


        }

        return wordlist;
    }
}
