package inc.elevati.imycity.main.completedreports;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.firebase.FirebaseContracts;
import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.EspressoIdlingResource;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.utils.Report;

import static inc.elevati.imycity.main.MainContracts.DeleteReportTaskResult.RESULT_OK;
import static inc.elevati.imycity.utils.Report.Status.STATUS_ACCEPTED;
import static inc.elevati.imycity.utils.Report.Status.STATUS_COMPLETED;
import static inc.elevati.imycity.utils.Report.Status.STATUS_REFUSED;
import static inc.elevati.imycity.utils.Report.Status.STATUS_WAITING;

/** Presenter associated to {@link CompletedReportsFragment} */
public class CompletedReportsPresenter implements MainContracts.ReportListPresenter{

    /** The view associated to this presenter */
    private MainContracts.ReportsView view;

    /** The reportDialogView that may be associated to this presenter */
    private MainContracts.ReportDialogView reportDialogView;

    /** This flag is set when a task had to be executed when no view was attached to this presenter */
    private boolean pendingTask;

    /** Used only if pendingTask flag is set, if not null indicates that
     * {@link CompletedReportsPresenter#onLoadReportsTaskComplete(QuerySnapshot)} has to be executed */
    private QuerySnapshot results;

    /** This flag is set when {@link CompletedReportsPresenter#onUpdateTaskComplete()}
     * is called while View is detached */
    private boolean update;

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void detachView() {
        this.view = null;
    }

    /** {@inheritDoc} */
    @Override
    public void attachReportDialogView(MainContracts.ReportDialogView view) {
        this.reportDialogView = view;
    }

    /** {@inheritDoc} */
    @Override
    public void detachReportDialogView() {
        this.reportDialogView = null;
    }

    /** {@inheritDoc} */
    @Override
    public void loadReports() {
        EspressoIdlingResource.increment();
        FirebaseContracts.DatabaseReader reader = new FirestoreHelper(this);
        reader.readCompletedReports();
    }

    /** {@inheritDoc} */
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
                Report.Status status = STATUS_WAITING;
                switch (snap.getString("status")) {
                    case "1":
                        status = STATUS_ACCEPTED;
                        break;
                    case "2":
                        status = STATUS_REFUSED;
                        break;
                    case "3":
                        status = STATUS_COMPLETED;
                        break;
                    case "4":
                        status = STATUS_WAITING;
                        break;
                }
                GeoPoint position = (GeoPoint) snap.get("position");

                ArrayList<String> usersStarred = (ArrayList<String>) snap.get("users_starred");
                boolean starred = usersStarred.contains(FirebaseAuthHelper.getUserId());

                Report report = new Report(id, userId, userName, title, description, reply,
                        operatorId, timestamp, (int) nStars, position, status, starred);

                reports.add(report);
            }
        }
        view.updateReports(reports);
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public void onReportClicked(Report report) {
        view.showReportDialog(report);
    }

    /** {@inheritDoc} */
    @Override
    public void onStarsButtonClicked(Report report) {
        FirebaseContracts.DatabaseWriter writer = new FirestoreHelper(this);
        if (report.isStarred()) {
            report.setStarred(false);
            report.decreaseStars();
            writer.unstarReport(report, FirebaseAuthHelper.getUserId());
        } else {
            report.setStarred(true);
            report.increaseStars();
            writer.starReport(report, FirebaseAuthHelper.getUserId());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onStarTaskComplete() {
        loadReports();
    }

    /** {@inheritDoc} */
    @Override
    public void onDeleteReportButtonClicked(Report report) {
        EspressoIdlingResource.increment();
        reportDialogView.showProgressDialog();
        FirebaseContracts.DatabaseWriter writer = new FirestoreHelper(this);
        writer.deleteReport(report.getId());
    }

    /** {@inheritDoc} */
    @Override
    public void onDeleteReportTaskComplete(MainContracts.DeleteReportTaskResult result) {
        if (reportDialogView == null) return;

        reportDialogView.dismissProgressDialog();
        if (result.equals(RESULT_OK)) {
            reportDialogView.dismissDialog();
            EspressoIdlingResource.decrement();
            loadReports();
        } else {
            reportDialogView.notifyDeleteReportError();
        }
    }
}
