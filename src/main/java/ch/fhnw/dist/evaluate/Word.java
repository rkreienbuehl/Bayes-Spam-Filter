package ch.fhnw.dist.evaluate;

import ch.fhnw.dist.filter.SpamFilter;

public class Word {

    private String word;
    private int ham = 0;
    private int spam = 0;

    public Word(String word, SpamFilter.SpamOrHam spam){
        this.word = word;

        if (spam == SpamFilter.SpamOrHam.SPAM) {
            this.addSpam(1);
        } else {
            this.addHam(1);
        }
    }

    public String getWord() {
        return word;
    }

    public int getHam() {
        return ham;
    }

    public void addHam(int count) {
        this.ham += count;
    }

    public int getSpam() {
        return spam;
    }

    public void addSpam(int count) {
        this.spam += count;
    }
}
