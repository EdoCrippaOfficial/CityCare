package inc.elevati.imycity.utils;

import android.content.Context;
import android.net.Uri;

/** Interface that contains interfaces to utils package */
public interface UtilsContracts {

    interface CompressorListener {

        /**
         * Method called when compress process has ended
         * @param fullImage the byte data of compressed full image
         * @param thumbImage the byte data of compressed thumbnail image
         */
        void onCompressed(byte[] fullImage, byte[] thumbImage);

        /** Method called to notify the listener that an error is occurred */
        void onErrorOccurred();
    }

    interface StorageSender {

        /**
         * Method called to send image to storage
         * @param report the report associated to the image
         * @param appContext context needed by Glide to load the image from Uri
         * @param imageUri the image Uri
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

        /** Method called to asynchronously retrieve all reports in database */
        void readAllReports();
    }

    interface AuthHelper {

        void register(String name, String ssn, String email, String password);
    }
}
