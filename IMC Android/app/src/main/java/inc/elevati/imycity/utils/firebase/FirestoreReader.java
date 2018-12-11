package inc.elevati.imycity.utils.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import inc.elevati.imycity.main.MainContracts;

/** Class that implements the database reading */
public class FirestoreReader {

    public static void readAllReports(final MainContracts.AllReportsPresenter presenter) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) presenter.onLoadAllReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        presenter.onUpdateTaskComplete();
                    }
                });
    }

    public static void readMyReports(final MainContracts.MyReportsPresenter presenter, String userId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) presenter.onLoadMyReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        presenter.onUpdateTaskComplete();
                    }
                });
    }
}
