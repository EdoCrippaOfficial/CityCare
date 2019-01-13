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

import static inc.elevati.imycity.main.MainContracts.DeleteReportTaskResult.RESULT_FAILED;
import static inc.elevati.imycity.main.MainContracts.DeleteReportTaskResult.RESULT_OK;
import static inc.elevati.imycity.main.MainContracts.SendTaskResult.RESULT_SEND_ERROR_DB;
import static inc.elevati.imycity.main.MainContracts.SendTaskResult.RESULT_SEND_OK;

/** This class is an helper that handles with Firebase Firestore related tasks */
public class FirestoreHelper implements FirebaseContracts.DatabaseReader, FirebaseContracts.DatabaseWriter {

    /** The listener that gets notified about report handling tasks */
    private MainContracts.ReportListPresenter reportListListener;

    /** The listener that gets notified about report creating tasks */
    private MainContracts.NewReportPresenter newReportListener;

    /**
     * Constructor used when report handling tasks are needed
     * @param reportListListener the presenter that is requesting services
     */
    public FirestoreHelper(MainContracts.ReportListPresenter reportListListener) {
        this.reportListListener = reportListListener;
    }

    /**
     * Constructor used when report creating tasks are needed
     * @param newReportListener the presenter that is requesting services
     */
    public FirestoreHelper(MainContracts.NewReportPresenter newReportListener) {
        this.newReportListener = newReportListener;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteReport(String reportId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports").document(reportId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reportListListener.onDeleteReportTaskComplete(RESULT_OK);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        reportListListener.onDeleteReportTaskComplete(RESULT_FAILED);
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public void sendReport(final Report report) {
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
        map.put("status", Report.Status.STATUS_WAITING.value);
        map.put("position", report.getPosition());
        map.put("users_starred", new ArrayList<String>());
        dbRef.collection("reports").document(report.getId())
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        newReportListener.onSendTaskComplete(RESULT_SEND_OK);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Delete the image uploaded previously on storage
                        StorageHelper.deleteImage(report);
                        newReportListener.onSendTaskComplete(RESULT_SEND_ERROR_DB);
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public void starReport(final Report report, final String userId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .document(report.getId())
                .update("users_starred", FieldValue.arrayUnion(userId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reportListListener.onStarOperationComplete();
                    }
        });
    }

    /** {@inheritDoc} */
    @Override
    public void unstarReport(final Report report, final String userId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .document(report.getId())
                .update("users_starred", FieldValue.arrayRemove(userId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reportListListener.onStarOperationComplete();
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public void readAllReports() {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) reportListListener.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        reportListListener.onUpdateTaskComplete();
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public void readMyReports(String userId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) reportListListener.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        reportListListener.onUpdateTaskComplete();
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public void readStarredReports(String userId) {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .whereArrayContains("users_starred", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) reportListListener.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        reportListListener.onUpdateTaskComplete();
                    }
                });
    }

    /** {@inheritDoc} */
    @Override
    public void readCompletedReports() {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .whereEqualTo("status", Report.Status.STATUS_COMPLETED.value)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) reportListListener.onLoadReportsTaskComplete(task.getResult());

                        // Hide refresh image
                        reportListListener.onUpdateTaskComplete();
                    }
                });
    }
}
