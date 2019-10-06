package ch.fhnw.dist;

import ch.fhnw.dist.unzip.Unzip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

public class BayesSpamFilter {

    private static final Logger logger = LogManager.getLogger(BayesSpamFilter.class);

    public static void main(String[] args) {


        logger.info("starting Bayes-Spam-Filter");

        /**
         * Load Files for test from classpath ressources
         * TODO Maybe load files from Java Arguments on start
         */
        File hamFiles = new File(BayesSpamFilter.class.getClassLoader().getResource("Programmieraufgabe1" + File.separator + "ham-anlern.zip").getFile());
        new Unzip(hamFiles.toString());


    }
}
