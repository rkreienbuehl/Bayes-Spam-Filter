package ch.fhnw.dist.unzip;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Unzip Files from a Folder. Output will be saved to /data in System.getproperty("user.dir").
 * This is the Current destination where the Java Code is executed. See
 * <a href="https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html" target="_blank">Java Sysprop</a>.
 * Source for the <a href="https://www.journaldev.com/960/java-unzip-file-example" target="_blank">doUnzip()</a>.
 * Had to be enhanced for Spam-Filter Functions.
 */
public class Unzip {
    /**
     * The logger
     */
    private final Logger logger = LogManager.getLogger(Unzip.class);

    /**
     * Path to Zip File
     */
    private String zipPath;

    /**
     * The User dir
     */
    private String userHome;

    /**
     * Input Stream for the zipped Files
     */
    private FileInputStream fis;

    /**
     * Buffer to extract files
     */
    private byte[] readbuffer = new byte[1024];

    /**
     * Directory where all Data gets stored
     */
    private File dataDir;

    /**
     * Destionation of extracted files in @dataDir
     */
    private File destDir;

    public Unzip(String path) {
        zipPath = path;
        userHome = System.getProperty("user.dir");
        dataDir = new File(System.getProperty("user.dir") + File.separator + "spam-data");
        destDir = new File(dataDir + File.separator + getUnzipDirName());
        process();
    }

    /**
     * Do a cleanup and then process all Files in the ZIP Folder
     */
    private void process() {
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            doUnzip();
        } else {
            doUnzip();
        }

    }


    /**
     * Unzip the folder and add all files to /data directory in user.dir
     */
    private void doUnzip() {
        logger.info("Starting unzip of " + zipPath + " to " + destDir);

        if (!destDir.exists()) destDir.mkdirs();

        try {
            fis = new FileInputStream(zipPath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipFileEntry = zis.getNextEntry();

            while (zipFileEntry != null) {
                String fileName = zipFileEntry.getName();
                File unzipedFile = new File(destDir + File.separator + fileName);

                logger.info("Unzipping to " + unzipedFile.getAbsolutePath());

                new File(unzipedFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(unzipedFile);
                int len;
                while ((len = zis.read(readbuffer)) > 0) {
                    //logger.debug();
                    fos.write(readbuffer, 0, len);
                }
                fos.close();

                zis.closeEntry();
                zipFileEntry = zis.getNextEntry();


            }
            zis.closeEntry();
            zis.close();
            fis.close();

            logger.info("finished unzipping of " + zipPath);

        } catch (IOException e) {
            logger.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }


    }

    /**
     * Determine the name for the unzipped Folder
     * @return Name of the Folder
     */
    private String getUnzipDirName() {
        String[] pathParts = zipPath.split(File.separator);
        String last = pathParts[pathParts.length - 1];
        String[] directory = last.split("\\.");
        logger.info("directory will be " + directory[0]);
        return directory[0];
    }
}
