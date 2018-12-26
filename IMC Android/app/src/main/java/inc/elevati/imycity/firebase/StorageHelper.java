package inc.elevati.imycity.firebase;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.Compressor;
import inc.elevati.imycity.utils.Report;

public class StorageHelper {

    /**
     * Method called to send the report image to storage
     * @param report     the report associated to the image
     * @param appContext context needed by Glide to load the image from Uri
     * @param imageUri   the image Uri
     */
    public static void sendImage(final Report report, Context appContext, Uri imageUri, final MainContracts.NewReportPresenter presenter) {

        // Compressor listener
        Compressor.CompressorListener listener = new Compressor.CompressorListener() {
            @Override
            public void onCompressed(byte[] fullData, final byte[] thumbData) {

                // Images storage sending
                final StorageReference imageReference = report.getImageReference(Report.IMAGE_FULL);
                imageReference.putBytes(fullData)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                report.getImageReference(Report.IMAGE_THUMBNAIL).putBytes(thumbData)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                // Proceed with data sending
                                                presenter.sendReportData(report);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                // The thumbnail upload has failed, so remove the full image too
                                                imageReference.delete();
                                                presenter.onSendTaskComplete(MainContracts.RESULT_SEND_ERROR_DB);
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // No file has been uploaded, cancel the operation
                                presenter.onSendTaskComplete(MainContracts.RESULT_SEND_ERROR_DB);
                            }
                        });
            }

            @Override
            public void onCompressError() {
                presenter.onSendTaskComplete(MainContracts.RESULT_SEND_ERROR_IMAGE);
            }
        };

        // Image compressing, normal and thumbnail
        Compressor.startCompressing(listener, appContext, imageUri);
    }

    /**
     * Called when sending report data to database has failed
     * @param report the report image to be removed from the storage
     */
    static void deleteImage(Report report) {
        report.getImageReference(Report.IMAGE_FULL).delete();
        report.getImageReference(Report.IMAGE_THUMBNAIL).delete();
    }
}
