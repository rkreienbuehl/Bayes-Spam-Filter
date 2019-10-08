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

        learnWords.forEach((key, word) -> {
            if (words.containsKey(key)) {
                Word spamWord = words.get(key);
                spamWord.setHam(spamWord.getHam() + word.getHam());
                spamWord.setSpam(spamWord.getSpam() + word.getSpam());
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
        logger.debug("Wordlist | Word -> SpamCount -> HamCount ");
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
}
