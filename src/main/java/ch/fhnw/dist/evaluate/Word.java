package ch.fhnw.dist.evaluate;

public class Word {

    private String word;
    private int ham;
    private int spam;
    private double hamProbability;
    private double spamProbability;

    public Word(int ham, int spam, double hamProbability, double spamProbability){
        this.ham = ham;
        this.spam = spam;
        this.hamProbability = hamProbability;
        this.spamProbability = spamProbability;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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
        this.ham += count;
    }

    public double getHamProbability() {
        return hamProbability;
    }

    public double getSpamProbability() {
        return spamProbability;
    }
}
