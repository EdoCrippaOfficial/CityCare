package inc.elevati.imycity.utils.firebase;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
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
        Compressor compressor = new Compressor(image);
        final byte[] imageData = compressor.getCompressedByteData(1280);
        final byte[] thumbData = compressor.getCompressedByteData(160);

        // Images storage sending
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageReference.child("images/" + report.getImageName());
        ref.child("img").putBytes(imageData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.child("thumb").putBytes(thumbData)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        presenter.sendReportData(report);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Delete the file uploaded
                                        ref.delete();
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
     * @param imageName the image to be removed from the storage
     */
    static void deleteImage(String imageName) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference ref = storageReference.child("images/" + imageName);
        ref.delete();
    }
}
