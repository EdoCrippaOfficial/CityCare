package inc.elevati.imycity.utils.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.UtilsInterface;

public class FirestoreReader implements UtilsInterface.DatabaseReader {

    private MainContracts.AllReportsPresenter presenter;

    public FirestoreReader(MainContracts.AllReportsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void readAllReports() {
        FirebaseFirestore dbRef = FirebaseFirestore.getInstance();
        dbRef.collection("reports")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            presenter.displayAllReports(task.getResult());
                        else
                            task.getException().printStackTrace();

                        presenter.resetViewRefreshing();
                    }
                });
    }
}
