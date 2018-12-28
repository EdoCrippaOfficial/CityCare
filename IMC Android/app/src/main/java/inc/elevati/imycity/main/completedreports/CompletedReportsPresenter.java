package inc.elevati.imycity.main.completedreports;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.EspressoIdlingResource;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.utils.Report;

public class CompletedReportsPresenter implements MainContracts.ReportListPresenter{

    /** The view instance */
    private MainContracts.ReportsView view;

    /** Indicates that a task is completed while View was detached */
    private boolean pendingTask;

    /** Variable set when onLoadAllReportsTaskComplete is called while View was detached */
    private QuerySnapshot results;

    /** Variable set when onUpdateTaskComplete is called while View was detached */
    private boolean update;

    @Override
    public void attachView(MvpContracts.MvpView view) {
        this.view = (MainContracts.ReportsView) view;

        // If there were pending tasks, execute them now
        if (pendingTask) {

            // If results is not null, then onLoadReportsTaskComplete has to be executed
            if (results != null) {
                onLoadReportsTaskComplete(results);
                results = null;
            }

            // If update is true, then onUpdateTaskComplete has to be executed
            if (update) {
                onUpdateTaskComplete();
                update = false;
            }
            pendingTask = false;
        }
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    /** Method called to retrieve all reports from database */
    @Override
    public void loadReports() {
        EspressoIdlingResource.increment();
        FirestoreHelper.readCompletedReports(this);
    }

    /**
     * Method called to forward data retrieved from database to View
     * If called when View is detached, results is saved and the method
     * is called again when view is re-attached
     * @param results the data retrieved
     */
    @Override
    public void onLoadReportsTaskComplete(QuerySnapshot results) {

        // If view is detached, set the pendingTask flag
        if (view == null) {
            pendingTask = true;
            this.results = results;
            return;
        }
        List<Report> reports = new ArrayList<>();
        if (results != null) {
            for (QueryDocumentSnapshot snap: results) {
                String id = snap.getString("id");
                String title = snap.getString("title");
                String description = snap.getString("description");
                long timestamp = snap.getLong("timestamp");
                String userId = snap.getString("user_id");
                String userName = snap.getString("user_name");
                String operatorId = snap.getString("operator_id");
                long nStars = snap.getLong("n_stars");
                String reply = snap.getString("reply");
                long status = snap.getLong("status");
                GeoPoint position = (GeoPoint) snap.get("position");

                ArrayList<String> usersStarred = (ArrayList<String>) snap.get("users_starred");
                boolean starred = usersStarred.contains(FirebaseAuthHelper.getUserId());

                Report report = new Report(id, userId, userName, title, description, reply,
                        operatorId, timestamp, (int) nStars, position, (int) status, starred);

                reports.add(report);
            }
        }
        view.updateReports(reports);
    }

    /**
     * Method called by app kernel that tells View to hide the refreshing View
     * If called when View is detached, a flag is set and the method
     * is called again when view is re-attached
     */
    public void onUpdateTaskComplete() {

        // If view is detached, set the pendingTask flag
        if (view == null) {
            pendingTask = true;
            this.update = true;
            return;
        }
        EspressoIdlingResource.decrement();
        view.resetRefreshing();
    }

    /**
     * Method called by ReportsAdapter when user clicks on a report in the RecyclerView
     * @param report the report to be shown
     */
    @Override
    public void showReport(Report report) {
        view.showReportDialog(report);
    }

    @Override
    public void starsButtonClicked(Report report) {
        if (report.isStarred())
            FirestoreHelper.unstarReport(report, FirebaseAuthHelper.getUserId(), this);
        else
            FirestoreHelper.starReport(report, FirebaseAuthHelper.getUserId(), this);
    }

    @Override
    public void onStarOperationComplete() {
        loadReports();
    }
}
