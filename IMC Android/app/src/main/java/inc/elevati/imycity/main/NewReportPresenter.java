package inc.elevati.imycity.main;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import inc.elevati.imycity.utils.Compressor;
import inc.elevati.imycity.utils.FirebaseDatabaseSender;
import inc.elevati.imycity.utils.FirebaseStorageSender;

public class NewReportPresenter implements MainContracts.NewReportPresenter {

    //Firebase
    private MainContracts.NewReportView view;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    String fileName;

    NewReportPresenter(MainContracts.NewReportView view) {
        this.view = view;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    @Override
    public void handleSendReport(Bitmap image, String title, String description) {

        //Invio della foto allo storage fatta!

        //TODO
        //Salvare nome nel database


        fileName = UUID.randomUUID().toString();

        FirebaseStorageSender fss = new FirebaseStorageSender(this);
        fss.send(image, fileName, storageReference);

        FirebaseDatabaseSender fds = new FirebaseDatabaseSender();
        fds.send(title, description, fileName, databaseReference);

    }

    public void dismissViewDialog(){
        view.dismissProgressDialog();
    }

}
