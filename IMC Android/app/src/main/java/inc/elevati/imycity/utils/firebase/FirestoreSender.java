package inc.elevati.imycity.utils.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.UtilsInterface;

public class FirestoreSender implements UtilsInterface.DatabaseSender {

    private MainContracts.NewReportPresenter presenter;

    public FirestoreSender(MainContracts.NewReportPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void send(final Report report) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("title", report.getTitle());
        map.put("description", report.getDescription());
        map.put("image", report.getImageName());
        map.put("timestamp", report.getTimestamp());
        dbRef.collection("reports")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        presenter.dismissViewDialog(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Delete the image uploaded previously on storage
                        StorageWriter.deleteImage(report.getImageName());
                        presenter.dismissViewDialog(true);
                    }
                });
    }
}
