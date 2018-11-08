package inc.elevati.imycity.main;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import inc.elevati.imycity.utils.Compressor;
import inc.elevati.imycity.utils.FirebaseStorageSender;

public class NewReportPresenter implements MainContracts.NewReportPresenter {

    //Firebase
    private MainContracts.NewReportView view;
    private StorageReference storageReference;

    String fileName;

    NewReportPresenter(MainContracts.NewReportView view) {
        this.view = view;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public void handleSendReport(Bitmap image, String title, String description) {

        //Invio della foto allo storage fatta!

        //TODO
        //Salvare nome nel database


        fileName = UUID.randomUUID().toString();

        FirebaseStorageSender fss = new FirebaseStorageSender(this);
        fss.send(image, title, description, fileName, storageReference);
    }

    public void dismissViewDialog(){
        view.dismissProgressDialog();
    }

}
