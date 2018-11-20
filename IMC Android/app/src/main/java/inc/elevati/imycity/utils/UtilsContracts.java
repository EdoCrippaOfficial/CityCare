package inc.elevati.imycity.utils;

import android.graphics.Bitmap;

/**
 * Interface that contains interfaces to utils package
 */
public interface UtilsContracts {

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
