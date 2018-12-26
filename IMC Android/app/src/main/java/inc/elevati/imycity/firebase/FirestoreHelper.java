package inc.elevati.imycity.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.Report;

public class FirestoreHelper {

    public static void deleteReport(String reportId, final onDeleteReportListener listener) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports").document(reportId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onReportDeleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onReportDeleteFailed();
                    }
                });
    }

    public static void sendReport(final Report report, final MainContracts.NewReportPresenter presenter) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("id", report.getId());
        map.put("title", report.getTitle());
        map.put("description", report.getDescription());
        map.put("timestamp", report.getTimestamp());
        map.put("user_id", report.getUserId());
        map.put("user_name", report.getUserName());
        map.put("operator_id", "");
        map.put("n_stars", 0);
        map.put("reply", "");
        map.put("status", Report.STATUS_WAITING);
        map.put("position", report.getLatitude() + "," + report.getLongitude());
        map.put("users_starred", new ArrayList<String>());
        dbRef.collection("reports").document(report.getId())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        presenter.onSendTaskComplete(MainContracts.RESULT_SEND_OK);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Delete the image uploaded previously on storage
                        StorageHelper.deleteImage(report);
                        presenter.onSendTaskComplete(MainContracts.RESULT_SEND_ERROR_DB);
                    }
                });
    }

    public static void starReport(final Report report, final String userId, final MainContracts.ReportListPresenter presenter) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .document(report.getId())
                .update("users_starred", FieldValue.arrayUnion(userId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        presenter.onStarOperationComplete();
                    }
        });
    }

    public static void unstarReport(final Report report, final String userId, final MainContracts.ReportListPresenter presenter) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .document(report.getId())
                .update("users_starred", FieldValue.arrayRemove(userId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        presenter.onStarOperationComplete();
                    }
                });
    }

    public static void readAllReports(final MainContracts.ReportListPresenter presenter) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) presenter.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        presenter.onUpdateTaskComplete();
                    }
                });
    }

    public static void readMyReports(final MainContracts.ReportListPresenter presenter, String userId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) presenter.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        presenter.onUpdateTaskComplete();
                    }
                });
    }

    public static void readStarredReports(final MainContracts.ReportListPresenter presenter, String userId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .whereArrayContains("users_starred", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) presenter.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        presenter.onUpdateTaskComplete();
                    }
                });
    }

    public static void readCompletedReports(final MainContracts.ReportListPresenter presenter) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .whereEqualTo("status", Report.STATUS_COMPLETED)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) presenter.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        presenter.onUpdateTaskComplete();
                    }
                });
    }

    public interface onDeleteReportListener {

        void onReportDeleted();

        void onReportDeleteFailed();
    }
}
