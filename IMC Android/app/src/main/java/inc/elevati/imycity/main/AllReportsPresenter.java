package inc.elevati.imycity.main;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import inc.elevati.imycity.utils.FirestoreReader;
import inc.elevati.imycity.utils.Report;

class AllReportsPresenter implements MainContracts.AllReportsPresenter {

    private MainContracts.AllReportsView view;

    AllReportsPresenter(MainContracts.AllReportsView view) {
        this.view = view;
    }

    @Override
    public void loadAllReports() {
        FirestoreReader reader = new FirestoreReader(this);
        reader.readAllReports();
    }

    @Override
    public void displayAllReports(QuerySnapshot results) {
        List<Report> reports = new ArrayList<>();
        for (QueryDocumentSnapshot snap: results) {
            reports.add(new Report(snap.getString("title"), snap.getString("description"), snap.getString("image")));
        }
        view.updateReports(reports);
    }

    @Override
    public void resetViewRefreshing() {
        view.resetRefreshing();
    }

    @Override
    public void showReport(Report report) {
        view.showReportDialog(report);
    }

    @Override
    public StorageReference getImageReference(String imageName, String type) {
        return FirebaseStorage.getInstance().getReference("images/" + imageName + "/" + type);
    }
}
