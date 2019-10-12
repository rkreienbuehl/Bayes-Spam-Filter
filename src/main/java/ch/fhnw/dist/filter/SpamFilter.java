package ch.fhnw.dist.filter;

import ch.fhnw.dist.evaluate.Word;
import ch.fhnw.dist.evaluate.WordProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
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

    private double minimumValue = 0.00001;

    public SpamFilter() {
    }

    /**
     * Learn all words in *-anlern.zip files
     *
     * @param mail      String from mail aka the content
     * @param spamOrHam type of mail
     */
    public void learn(String mail, SpamOrHam spamOrHam) {
        wp.setContent(mail, spamOrHam);
        Map<String, Word> learnWords = wp.processContent();

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

    public SpamOrHam checkMail(String mail) {
        String[] mailWords = mail.split(" ");

        double spamIndex = 1.0;
        double hamIndex = 1.0;


        for (String word : mailWords ) {
            if (words.containsKey(word)) {
                Word w = words.get(word);
                spamIndex = spamIndex * ((w.getSpam() != 0) ? ((double) w.getSpam() / (double) this.spamCount) : this.minimumValue);
                hamIndex = hamIndex * ((w.getHam() != 0) ? ((double) w.getHam() / (double) this.hamCount) : this.minimumValue);
            }
        }

        double spamProbability = spamIndex / (spamIndex + hamIndex);

        SpamOrHam isSpam = spamProbability >= 0.5 ? SpamOrHam.SPAM : SpamOrHam.HAM;

        logger.debug(" Probability of mail beeing spam: " + spamProbability + " => " + isSpam.toString());

        return isSpam;
    }
}
