package inc.elevati.imycity.utils.firebase;

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
import inc.elevati.imycity.utils.UtilsContracts;

/**
 * Class that implements the storage writing
 */
public class StorageWriter implements UtilsContracts.StorageSender, UtilsContracts.CompressorListener {

    /** The recipient presenter */
    private MainContracts.NewReportPresenter presenter;

    /** The report to forward to presenter when image compressing and sending succeeded */
    private Report report;

    public StorageWriter(MainContracts.NewReportPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Method called to send image to storage
     * @param report the report associated to the image
     * @param appContext context needed by Glide to load the image from Uri
     * @param imageUri the image Uri
     */
    @Override
    public void send(Report report, Context appContext, Uri imageUri) {
        this.report = report;
        // Image compressing, normal and thumbnail
        Compressor.startCompressing(this, appContext, imageUri);
    }

    /**
     * Called when sending report data to database has failed
     * @param report the report image to be removed from the storage
     */
    static void deleteImage(Report report) {
        report.getImageReference(Report.IMAGE_FULL).delete();
        report.getImageReference(Report.IMAGE_THUMBNAIL).delete();
    }

    @Override
    public void onCompressed(byte[] fullImage, final byte[] thumbImage) {

        // Images storage sending
        final StorageReference imageReference = report.getImageReference(Report.IMAGE_FULL);
        imageReference.putBytes(fullImage)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        report.getImageReference(Report.IMAGE_THUMBNAIL).putBytes(thumbImage)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        presenter.sendReportData(report);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // The thumbnail upload has failed, so remove the full image too
                                        imageReference.delete();
                                        presenter.dismissViewDialog(MainContracts.RESULT_SEND_ERROR_DB);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No file has been uploaded
                        presenter.dismissViewDialog(MainContracts.RESULT_SEND_ERROR_DB);
                    }
                });
    }

    @Override
    public void onErrorOccurred() {
        presenter.dismissViewDialog(MainContracts.RESULT_SEND_ERROR_IMAGE);
    }
}
