package inc.elevati.imycity.utils.firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import inc.elevati.imycity.main.ReportDialog;

public class FirestoreDeleter {

    public static void deleteReport(String reportId, final ReportDialog dialog){
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports").document(reportId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.onReportDeleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DEBUG", "Error deleting document", e);
                    }
                });
    }
}
