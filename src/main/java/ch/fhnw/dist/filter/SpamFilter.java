package ch.fhnw.dist.filter;

import ch.fhnw.dist.evaluate.Word;
import ch.fhnw.dist.evaluate.WordProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpamFilter {

    /**
     * Categories for Spam oder Ham
     */
    public enum SpamOrHam {
        SPAM, HAM;
    }

    private Logger logger = LogManager.getLogger(SpamFilter.class);
    private Map<String, Word> words = new HashMap<>();
    private WordProcessor wp = new WordProcessor();

    private int spamCount = 0;
    private int hamCount = 0;

    private double minimumValue = 0.0001;

    /**
     * Threshold for Spam Detection.
     */
    private final BigDecimal THRESHOLD = new BigDecimal(0.8);
    private double globalSpamProbability = 0.5;

    public double getMinimumValue() {
        return minimumValue;
    }

    public double getTHRESHOLD() {
        return THRESHOLD.doubleValue();
    }

    public double getGlobalSpamProbability() {
        return globalSpamProbability;
    }

    public void setGlobalSpamProbability(double globalSpamProbability) {
        this.globalSpamProbability = globalSpamProbability;
    }

    public SpamFilter() {
    }

    /**
     * Learn all words in *-anlern.zip files
     *
     * @param mail      String from mail aka the content
     * @param spamOrHam type of mail
     */
    public void learn(String mail, SpamOrHam spamOrHam) {
        wp.setContentWithIdentification(mail, spamOrHam);
        Map<String, Word> learnWords = wp.processContentWithIdentification();

        if (SpamOrHam.SPAM == spamOrHam) {
            this.spamCount++;
        } else {
            this.hamCount++;
        }

        learnWords.forEach((key, word) -> {
            if (words.containsKey(key)) {
                Word spamWord = words.get(key);
                spamWord.addHam(word.getHam());
                spamWord.addSpam(word.getSpam());
            } else {
                words.put(key, word);
            }
        });
    }

    /**
     * Print the Wordlist from the SpamFilter with classification to the console
     */
    public void printWordlist() {
        logger.debug("printing Words to console");
        logger.debug("Wordlist | Word -> SpamCount / SpamProbability -> HamCount / HamProbability ");
        words.forEach((key, word) -> {
            logger.debug(key + " -> " + word.getSpam() + " -> " + word.getHam());
        });
        logger.debug("Spam Ham Liste contains " + words.size() + " words");
    }

    /**
     * You can also print the words to a file
     */
    public void printWordsToFile() {
        File outfile = new File(System.getProperty("user.dir") + File.separator + "spam-data" + File.separator + "model.csv");

        logger.debug("start to write file to " + outfile.getAbsolutePath());

        try {
            PrintWriter pw = new PrintWriter(outfile);
            pw.println("WORD,SPAMCOUNT,HAMCOUNT;");
            words.forEach((key, word) -> {
                pw.println(String.format("%s,%d,%d", key, word.getSpam(), word.getHam()));
            });
            logger.debug("finished writing file");

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }
    }


    /**
     * Do a Spam classification for each mail in the given set
     * @param mail the mail content
     * @return classification of mail type SPAM or HAM
     */
    public SpamOrHam checkMail(String mail) {

        wp.setContent(mail);

        List<String> mailWords = wp.processContent();

        BigDecimal spamIndex = new BigDecimal(1.0);
        BigDecimal hamIndex = new BigDecimal(1.0);
        BigDecimal globalSpamProbability = new BigDecimal(this.globalSpamProbability);
        BigDecimal globalHamProbability = new BigDecimal(1 - this.globalSpamProbability);



        for (String word : mailWords) {
            if (words.containsKey(word)) {
                Word w = words.get(word);

                double wordSpamIndex = ((w.getSpam() != 0) ? (double) w.getSpam() : this.minimumValue) / (double) this.spamCount;
                double wordHamIndex = ((w.getHam() != 0) ? (double) w.getHam() : this.minimumValue) / (double) this.hamCount;

                spamIndex = spamIndex.multiply(new BigDecimal(wordSpamIndex));
                hamIndex = hamIndex.multiply(new BigDecimal(wordHamIndex));
            }
        }

        BigDecimal spamProbability = spamIndex.multiply(globalSpamProbability).divide(spamIndex.multiply(globalSpamProbability).add(hamIndex.multiply(globalHamProbability)), 2, RoundingMode.DOWN);

        SpamOrHam isSpam = spamProbability.compareTo(THRESHOLD) >= 0 ? SpamOrHam.SPAM : SpamOrHam.HAM;

        return isSpam;
    }
}
