package ch.fhnw.dist;

import ch.fhnw.dist.evaluate.Word;
import ch.fhnw.dist.evaluate.WordProcessor;
import ch.fhnw.dist.filter.SpamFilter;
import ch.fhnw.dist.reader.ZipReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BayesSpamFilter {

    private static final Logger logger = LogManager.getLogger(BayesSpamFilter.class);
    private static Map<String, Word> words = new HashMap<>();

    public static void main(String[] args) {
        logger.info("starting Bayes-Spam-Filter");

        /**
         * Load Files for test from classpath ressources
         * TODO Maybe load files from Java Arguments on start
         */

        /**
         * Read HamAnlernMails
         */
        File hamFiles = new File(BayesSpamFilter.class.getClassLoader().getResource("Programmieraufgabe1" + File.separator + "ham-anlern.zip").getFile());
        ZipReader hr = new ZipReader(hamFiles.toString());
        String[] hamMails = hr.doRead();

        SpamFilter spamFilter = new SpamFilter();


        for(String mail : hamMails){
            spamFilter.learn(mail, SpamFilter.SpamOrHam.HAM);
        }


        /**
         * Read SpamAnlernMails
         */
        File spamFiles = new File(BayesSpamFilter.class.getClassLoader().getResource("Programmieraufgabe1" + File.separator + "spam-anlern.zip").getFile());
        ZipReader sr = new ZipReader(spamFiles.toString());
        String[] spamMails = sr.doRead();

        for(String mail : spamMails){
            spamFilter.learn(mail, SpamFilter.SpamOrHam.SPAM);
        }



    }
}
