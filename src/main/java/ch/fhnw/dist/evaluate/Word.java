package ch.fhnw.dist.evaluate;

import ch.fhnw.dist.filter.SpamFilter;


/**
 * Save a single Word and its properties. Each Word is an instance of a wordobject to process it afterwards
 */
public class Word {

    /**
     * A single word
     */
    private String word;

    /**
     * Ham Counter
     */
    private int ham = 0;

    /**
     * Spam Counter
     */
    private int spam = 0;


    public Word(String word, SpamFilter.SpamOrHam spam) {
        this.word = word;

        if (spam == SpamFilter.SpamOrHam.SPAM) {
            this.addSpam(1);
        } else {
            this.addHam(1);
        }
    }

    /**
     * Get a Word
     *
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * Get the Ham Counter
     *
     * @return Amount of Ham
     */
    public int getHam() {
        return ham;
    }

    /**
     * Increment Ham Counter in case of Ham Word
     *
     * @param count increment value
     */
    public void addHam(int count) {
        this.ham += count;
    }

    /**
     * Get the Spam Counter
     *
     * @return Amount of Spam
     */
    public int getSpam() {
        return spam;
    }

    /**
     * Increment Spam counter in case of Spam Word
     *
     * @param count increment value
     */
    public void addSpam(int count) {
        this.spam += count;
    }
}
