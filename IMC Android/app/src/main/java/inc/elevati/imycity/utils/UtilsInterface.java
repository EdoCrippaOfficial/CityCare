package inc.elevati.imycity.utils;

import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public interface UtilsInterface {

    /**
     *  Component used to reduce the quality of
     *  the selected image before sending it to
     *  Firebase Storage.
     */
    interface ImageCompressor{
        /**
         * Get the byteStream from the compression
         * @return byte array ready to be sent
         */
        byte[] getCompressedByteData();
    }

    interface StorageSender{
        void send(Bitmap image, String fileName, StorageReference storageReference);
    }

    interface DatabaseSender{
        void send(String title, String description, String fileName, DatabaseReference databaseReference);
    }
}
