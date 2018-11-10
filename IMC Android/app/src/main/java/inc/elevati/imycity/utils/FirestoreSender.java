package inc.elevati.imycity.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FirestoreSender implements UtilsInterface.DatabaseSender {

    @Override
    public void send(String title, String description, String imageName) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        Map<String, String> report = new HashMap<>();
        report.put("title", title);
        report.put("description", description);
        report.put("image", imageName);
        dbRef.collection("reports")
                .add(report)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
