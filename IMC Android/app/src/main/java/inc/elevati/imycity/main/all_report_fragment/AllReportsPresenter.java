package inc.elevati.imycity.main.all_report_fragment;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.firebase.FirestoreReader;
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
            String title = snap.getString("title");
            String description = snap.getString("description");
            String image = snap.getString("image");
            long timestamp = snap.getLong("timestamp");
            reports.add(new Report(title, description, image, timestamp));
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
}
