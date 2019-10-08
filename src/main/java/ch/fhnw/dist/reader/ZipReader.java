package ch.fhnw.dist.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Unzip Files from a Folder and write Content to an Array of Strings
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
     * Unzip the folder and add all files to /data directory in user.dir
     */
    public String[] doRead() {
        logger.info("Starting unzip");


        try {
            fis = new FileInputStream(zipPath);
            ZipInputStream zis = new ZipInputStream(fis, StandardCharsets.UTF_8);
            ZipEntry zipFileEntry = zis.getNextEntry();

            int mailcount = 0;
            String[] mails = new String[(int) zipFileEntry.getSize()];

            while (zipFileEntry != null) {


                logger.info("Unzipping " + zipFileEntry.getName());

                int len;

                StringBuilder sb = new StringBuilder();


                while ((len = zis.read(readbuffer)) > 0) {
                    sb.append(new String(readbuffer, 0, len));
                }

                logger.debug(sb.toString());

                mails[mailcount] = sb.toString();
                mailcount++;

                zis.closeEntry();
                zipFileEntry = zis.getNextEntry();

            }
            zis.closeEntry();
            zis.close();
            fis.close();

            logger.info("finished unzipping of " + zipPath);

            return mails;

        } catch (IOException e) {
            logger.error(e.getMessage() + Arrays.toString(e.getStackTrace()));
        }

        return new String[0];

    }
}
