package inc.elevati.imycity.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import inc.elevati.imycity.main.MainContracts;

public class FirebaseStorageSender implements UtilsInterface.StorageSender {

    MainContracts.NewReportPresenter pres;

    public FirebaseStorageSender(MainContracts.NewReportPresenter pres){
        this.pres = pres;
    }

    @Override
    public void send(Bitmap image, final String fileName, StorageReference storageReference) {
        Compressor compressor = new Compressor(image);
        byte[] bitmapData = compressor.getCompressedByteData();
        StorageReference ref = storageReference.child("images/" + fileName);
        ref.putBytes(bitmapData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pres.dismissViewDialog();
                        Log.d("DEBUG", "Uploaded!\tname: " + fileName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pres.dismissViewDialog();
                        Log.d("DEBUG", "Error uploading!");
                    }
                });
    }
}
