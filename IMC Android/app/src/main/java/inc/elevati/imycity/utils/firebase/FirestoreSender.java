package inc.elevati.imycity.utils.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.UtilsContracts;

/**
 * Class that implements the database writing
 */
public class FirestoreSender implements UtilsContracts.DatabaseSender {

    /**
     * The recipient presenter
     */
    private MainContracts.NewReportPresenter presenter;

    public FirestoreSender(MainContracts.NewReportPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Method called to send a report to database
     * @param report the report to be sent
     */
    @Override
    public void send(final Report report) {
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
                        StorageWriter.deleteImage(report);
                        presenter.onSendTaskComplete(MainContracts.RESULT_SEND_ERROR_DB);
                    }
                });
    }
}
