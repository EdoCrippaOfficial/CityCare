package inc.elevati.imycity.main.all_report_fragment;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.firebase.FirestoreReader;
import inc.elevati.imycity.utils.Report;

/**
 * Presenter class used by AllReportsFragment to interact with the app kernel
 */
class AllReportsPresenter implements MainContracts.AllReportsPresenter {

    /**
     * The view instance
     */
    private MainContracts.AllReportsView view;

    AllReportsPresenter(MainContracts.AllReportsView view) {
        this.view = view;
    }


    /**
     * Method called to retrieve all reports from database
     */
    @Override
    public void loadAllReports() {
        FirestoreReader reader = new FirestoreReader(this);
        reader.readAllReports();
    }

    /**
     * Method called to forward data retrieved from database to View
     * @param results the data retrieved
     */
    @Override
    public void displayAllReports(QuerySnapshot results) {
        List<Report> reports = new ArrayList<>();
        for (QueryDocumentSnapshot snap: results) {
            String id = snap.getString("id");
            String title = snap.getString("title");
            String description = snap.getString("description");
            String image = snap.getString("image");
            String userId = snap.getString("user_id");
            String reply = snap.getString("reply");
            String operatorId = snap.getString("operator_id");
            long nStars = snap.getLong("n_stars");
            long status = snap.getLong("status");
            String position = snap.getString("position");
            String[] positionData = position.split(",");
            long timestamp = snap.getLong("timestamp");
            Report report = new Report(id, userId, title, description, reply, operatorId, image, timestamp, (int) nStars,
                    Long.valueOf(positionData[0]), Long.valueOf(positionData[1]), (int) status);

            reports.add(report);
        }
        view.updateReports(reports);
    }

    /**
     * Method called by app kernel that tells View to hide the refreshing image
     */
    @Override
    public void resetViewRefreshing() {
        view.resetRefreshing();
    }

    /**
     * Method called when user click on a report in the list
     * @param report the report to be shown
     */
    @Override
    public void showReport(Report report) {
        view.showReportDialog(report);
    }
}
