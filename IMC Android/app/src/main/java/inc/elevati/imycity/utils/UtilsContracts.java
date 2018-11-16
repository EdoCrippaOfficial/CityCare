package inc.elevati.imycity.utils;

import android.graphics.Bitmap;

/**
 * Interface that contains interfaces to utils package
 */
public interface UtilsContracts {

    /**
     *  Component used to reduce the size o the selected
     *  image before sending it to storage.
     */
    interface ImageCompressor {

        /**
         * Called to get the byteStream from the compression
         * @param px the image maximum size
         * @return byte array ready to be sent to storage
         */
        byte[] getCompressedByteData(int px);
    }

    interface StorageSender {

        /**
         * Method called to send image to storage
         * @param image the image to be sent
         * @param report the report that owns the image
         */
        void send(Bitmap image, Report report);
    }

    interface DatabaseSender {

        /**
         * Method called to send a report to database
         * @param report the report to be sent
         */
        void send(Report report);
    }

    interface DatabaseReader {

        /**
         * Method called to asynchronously retrieve all reports in database
         */
        void readAllReports();
    }
}
