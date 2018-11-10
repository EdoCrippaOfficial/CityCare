package inc.elevati.imycity.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import inc.elevati.imycity.main.MainContracts;

public class StorageSender implements UtilsInterface.StorageSender {

    private MainContracts.NewReportPresenter presenter;

    public StorageSender(MainContracts.NewReportPresenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void send(Bitmap image, final String fileName) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        Compressor compressor = new Compressor(image);
        byte[] imageData = compressor.getCompressedByteData(1280);
        byte[] thumbData = compressor.getCompressedByteData(160);
        StorageReference ref = storageReference.child("images/" + fileName);
        ref.child("img").putBytes(imageData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        presenter.dismissViewDialog();
                        Log.d("DEBUG", "Uploaded!\tname: " + fileName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        presenter.dismissViewDialog();
                        Log.d("DEBUG", "Error uploading!");
                    }
                });
        ref.child("thumb").putBytes(thumbData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //presenter.dismissViewDialog();
                        Log.d("DEBUG", "Uploaded!\tname: " + fileName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //presenter.dismissViewDialog();
                        Log.d("DEBUG", "Error uploading!");
                    }
                });
    }
}
