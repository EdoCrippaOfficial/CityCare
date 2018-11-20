package inc.elevati.imycity.utils.firebase;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

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
public class StorageWriter implements UtilsContracts.StorageSender {

    /**
     * The recipient presenter
     */
    private MainContracts.NewReportPresenter presenter;

    public StorageWriter(MainContracts.NewReportPresenter presenter){
        this.presenter = presenter;
    }

    /**
     * Method called to send image to storage
     * @param image the image to be sent
     * @param report the report that owns the image
     */
    @Override
    public void send(Bitmap image, final Report report) {
        // Image compressing, normal and thumbnail
        Compressor compressor = Compressor.getInstance();
        final byte[] imageData = compressor.getCompressedByteData(image, Compressor.TYPE_FULL);
        final byte[] thumbData = compressor.getCompressedByteData(image, Compressor.TYPE_THUMBNAIL);

        // Images storage sending
        final StorageReference imageReference = report.getImageReference(Report.IMAGE_FULL);
        imageReference.putBytes(imageData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        report.getImageReference(Report.IMAGE_THUMBNAIL).putBytes(thumbData)
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
                                        presenter.dismissViewDialog(true);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No file has been uploaded
                        presenter.dismissViewDialog(true);
                    }
                });
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
