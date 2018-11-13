package inc.elevati.imycity.utils;

import android.graphics.Bitmap;

public interface UtilsInterface {

    /**
     *  Component used to reduce the quality of
     *  the selected image before sending it to
     *  Firebase Storage.
     */
    interface ImageCompressor {
        /**
         * Get the byteStream from the compression
         * @param px the image maximum size
         * @return byte array ready to be sent
         */
        byte[] getCompressedByteData(int px);
    }

    interface StorageSender {
        void send(Bitmap image, Report report);
    }

    interface DatabaseSender {
        void send(Report report);
    }

    interface DatabaseReader {
        void readAllReports();
    }
}
