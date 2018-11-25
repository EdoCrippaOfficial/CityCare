package inc.elevati.imycity.utils;

import android.content.Context;
import android.net.Uri;

/**
 * Interface that contains interfaces to utils package
 */
public interface UtilsContracts {

    interface compressorListener {

        /**
         * Method called when compress process has ended
         * @param fullImage the byte data of compressed full image
         * @param thumbImage the byte data of compressed thumbnail image
         */
        void onCompressed(byte[] fullImage, byte[] thumbImage);

        void onErrorOccurred();
    }

    interface StorageSender {

        /**
         * Method called to send image to storage
         * @param image the image to be sent
         * @param report the report that owns the image
         */
        void send(Report report, Context appContext, Uri imageUri);
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
