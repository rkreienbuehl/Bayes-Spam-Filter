package ch.fhnw.dist.evaluate;

import ch.fhnw.dist.filter.SpamFilter;

public class Word {

    private String word;
    private int ham = 0;
    private int spam = 0;
    private double minimumValue = 0.1;
    private double hamProbability;
    private double spamProbability;

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
        this.hamProbability = this.ham / ((double) this.ham + (double) this.spam);
        this.spamProbability = (this.spam == 0 ? this.minimumValue : (this.spam / ((double) this.ham + (double) this.spam)));
    }

    public int getSpam() {
        return spam;
    }

    public void addSpam(int count) {
        this.spam += count;
        this.hamProbability = (this.ham == 0 ? this.minimumValue : (this.ham / ((double) this.ham + (double) this.spam)));
        this.spamProbability = this.spam / ((double) this.ham + (double) this.spam);
    }

    public double getHamProbability() {
        return hamProbability;
    }

    public double getSpamProbability() {
        return spamProbability;
    }
}
