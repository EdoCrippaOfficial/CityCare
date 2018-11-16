package inc.elevati.imycity.utils.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.UtilsContracts;

/**
 * Class that implements the database reading
 */
public class FirestoreReader implements UtilsContracts.DatabaseReader {

    /**
     * The recipient presenter
     */
    private MainContracts.AllReportsPresenter presenter;

    public FirestoreReader(MainContracts.AllReportsPresenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Method called to asynchronously retrieve all reports in database
     */
    @Override
    public void readAllReports() {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) presenter.displayAllReports(task.getResult());

                        // Hide refresh image
                        presenter.resetViewRefreshing();
                    }
                });
    }
}
