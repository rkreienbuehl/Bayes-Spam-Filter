package ch.fhnw.dist.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * Read File content from a ZipFolder and write Content to an Array of Strings
 */
public class ZipReader {
    /**
     * The logger
     */
    private final Logger logger = LogManager.getLogger(ZipReader.class);

    /**
     * Path to Zip File
     */
    private String zipPath;

    /**
     * Input Stream for the zipped Files
     */
    private FileInputStream fis;

    /**
     * Buffer to extract files
     */
    private byte[] readbuffer = new byte[1024];


    /**
     * Constructor of ZipReader
     *
     * @param path String
     */
    public ZipReader(String path) {
        zipPath = path;
    }


    /**
     * Read alll Data from Zip Folder and add contents of mails to String Array
     */
    public String[] doRead() {
        String filename = fileNameFromZipPath(zipPath);
        logger.info("Starting to read from zipfile " + filename);


        try {
            fis = new FileInputStream(zipPath);
            ZipInputStream zis = new ZipInputStream(fis, StandardCharsets.UTF_8);
            ZipEntry zipFileEntry = zis.getNextEntry();


            logger.info("Num entries in Zip File " + filename + ": #" + countEntriesInZipFile(zipPath));

            int mailcount = 0;
            String[] mails = new String[countEntriesInZipFile(zipPath)];

            while (zipFileEntry != null) {

                int len;
                StringBuilder sb = new StringBuilder();

                while ((len = zis.read(readbuffer)) > 0) {
                    sb.append(new String(readbuffer, 0, len));
                }

                String unformattedContent = removeFormat(sb.toString());

                mails[mailcount] = unformattedContent;
                mailcount++;

                zis.closeEntry();
                zipFileEntry = zis.getNextEntry();

            }
            zis.closeEntry();
            zis.close();
            fis.close();

            logger.info("finished reading contents of " + filename);

            return mails;

        } catch (IOException e) {
            logger.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }

        return new String[0];

    }

    /**
     * Remove format from String like line break, tabs and replace it with a single whitespace
     * \\n     remove new line
     * \\r     remove carriage return
     * &nbsp;  remove html whitespace
     * \\.     remove all periods
     * ,       remove all commas
     * /       remove all slashes
     * \\(     remove all opening parentheses
     * \\)     remove all closing parentheses
     * <       remove all smaler than
     * >       remove all greater than
     * =       remove all equals
     * \\s{2,} remove all 2 or more white space. This must be at the end to prevent double spaces!
     *
     * @param content input formatted content
     * @return unformatted content
     */
    private String removeFormat(String content) {
        return content
                .replaceAll("\\n", " ")
                .replaceAll("\\r", " ")
                .replaceAll("&nbsp;", " ")
                .replaceAll("\\.", " ")
                .replaceAll(",", " ")
                .replaceAll("/", " ")
                .replaceAll("\\(", " ")
                .replaceAll("\\)", " ")
                .replaceAll("<", " ")
                .replaceAll(">", " ")
                .replaceAll("=", " ")
                .replaceAll("\\s{2,}", " ");
    }

    /**
     * Count files contained in a ZipFile
     *
     * @param zipPath path to zip file as String
     * @return number of Files in ZipFile
     */
    private int countEntriesInZipFile(String zipPath) {
        int mailsInZip = 0;
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            Enumeration entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                entries.nextElement();
                mailsInZip++;
            }
            return mailsInZip;
        } catch (IOException e) {
            logger.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }

        return mailsInZip;
    }

    /**
     * Get ZipFile name from a zipPath
     *
     * @param zipPath the path to zip
     * @return filename
     */
    private String fileNameFromZipPath(String zipPath) {

        String[] splittedZipPath = zipPath.split("/");
        return splittedZipPath[splittedZipPath.length - 1];

    }
}
