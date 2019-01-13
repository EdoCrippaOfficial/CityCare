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

import static inc.elevati.imycity.main.MainContracts.SendTaskResult.RESULT_SEND_ERROR_DB;
import static inc.elevati.imycity.main.MainContracts.SendTaskResult.RESULT_SEND_ERROR_IMAGE;

/** This class is an helper that handles with Firebase Storage related tasks */
public class StorageHelper implements FirebaseContracts.StorageWriter {

    /** The listener that gets notified about storage related tasks */
    private MainContracts.NewReportPresenter listener;

    /**
     * @param listener the listener that is requesting services
     */
    public StorageHelper(MainContracts.NewReportPresenter listener) {
        this.listener = listener;
    }

    /** {@inheritDoc} */
    @Override
    public void sendImage(final Report report, Context appContext, Uri imageUri) {

        // Compressor listener
        final Compressor.CompressorListener compressorListener = new Compressor.CompressorListener() {
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
                                                listener.sendReportData(report);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                // The thumbnail upload has failed, so remove the full image too
                                                imageReference.delete();
                                                listener.onSendTaskComplete(RESULT_SEND_ERROR_DB);
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // No file has been uploaded, cancel the operation
                                listener.onSendTaskComplete(RESULT_SEND_ERROR_DB);
                            }
                        });
            }

            @Override
            public void onCompressError() {
                listener.onSendTaskComplete(RESULT_SEND_ERROR_IMAGE);
            }
        };

        // Image compressing, normal and thumbnail
        Compressor.startCompressing(compressorListener, appContext, imageUri);
    }

    /**
     * Called when sending report data to database has failed
     * @param report the report image to remove from the storage
     */
    static void deleteImage(Report report) {
        report.getImageReference(Report.IMAGE_FULL).delete();
        report.getImageReference(Report.IMAGE_THUMBNAIL).delete();
    }
}
