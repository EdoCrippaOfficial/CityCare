package inc.elevati.imycity.main;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import inc.elevati.imycity.login.LoginActivity;
import inc.elevati.imycity.main.allreports.AllReportsFragment;
import inc.elevati.imycity.main.completedreports.CompletedReportsFragment;
import inc.elevati.imycity.main.myreports.MyReportsFragment;
import inc.elevati.imycity.main.starredreports.StarredReportsFragment;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.utils.Report;

/** Generic interface that defines contracts for login package */
public interface MainContracts {

    /** Possible results for report send task */
    enum SendTaskResult {

        /** Send task completed correctly */
        RESULT_SEND_OK,

        /** Send task failed due to image compressing error */
        RESULT_SEND_ERROR_IMAGE,

        /** Send task failed due to database */
        RESULT_SEND_ERROR_DB
    }

    /** Possible results for delete report task */
    enum DeleteReportTaskResult {
        RESULT_OK,
        RESULT_FAILED
    }

    /** Menu pages list */
    int PAGE_NEW = 0;
    int PAGE_ALL = 1;
    int PAGE_MY = 2;
    int PAGE_STARRED = 3;
    int PAGE_COMPLETED = 4;

    /** Possible report sorting criteria */
    int REPORT_SORT_DATE_NEWEST = 1;
    int REPORT_SORT_DATE_OLDEST = 2;
    int REPORT_SORT_STARS_MORE = 3;
    int REPORT_SORT_STARS_LESS = 4;

    /** Implemented by the class that handles general main view (activity) */
    interface MainView extends MvpContracts.MvpView {

        /**
         * Tells view to scroll to the selected page
         * @param page the page to scroll to
         */
        void scrollToPage(int page);

        /**
         * Starts {@link LoginActivity}
         */
        void startLoginActivity();

        /**
         * Sets as checked the provided menu item
         * @param itemId the item id
         */
        void setCheckedMenuItem(int itemId);
    }

    /** Implemented by the class that handles general main presenter */
    interface MainPresenter extends MvpContracts.MvpPresenter {

        /**
         * Notifies this presenter that a menu item was clicked
         * @param itemId the id of the clicked item
         */
        void onMenuItemClicked(int itemId);

        /**
         * Notifies this presenter that a scroll action has occurred
         * @param page the page to scroll to
         */
        void onPageScrolled(int page);

        /**
         * @return the current user email
         */
        String getCurrentUserEmail();

        /**
         * @return the current user display name
         */
        String getCurrentUserName();

        /**
         * @return the child {@link NewReportPresenter} instance
         */
        NewReportPresenter getNewReportPresenter();

        /**
         * @return the child {@link ReportListPresenter} instance associated to {@link AllReportsFragment}
         */
        ReportListPresenter getAllReportsPresenter();

        /**
         * @return the child {@link ReportListPresenter} instance associated to {@link CompletedReportsFragment}
         */
        ReportListPresenter getCompletedReportsPresenter();

        /**
         * @return the child {@link ReportListPresenter} instance associated to {@link MyReportsFragment}
         */
        ReportListPresenter getMyReportsPresenter();

        /**
         * @return the child {@link ReportListPresenter} instance associated to {@link StarredReportsFragment}
         */
        ReportListPresenter getStarredReportsPresenter();
    }

    /** Implemented by the class that handles new report presenter */
    interface NewReportPresenter extends MvpContracts.MvpPresenter {

        /**
         * Notifies this presenter that send button was clicked
         * @param title the title provided
         * @param description the description provided
         * @param appContext the appContext provided
         * @param imageUri the imageUri provided
         * @param position the position provided
         */
        void onSendButtonClicked(String title, String description, Context appContext, Uri imageUri, LatLng position);

        /**
         * Continues the report sending task after the image was sent to storage,
         * it sends the remaining report data to database
         * @param report the report to send
         */
        void sendReportData(Report report);

        /**
         * Notifies this presenter that a send report task has completed
         * @param result the task result
         */
        void onSendTaskComplete(SendTaskResult result);
    }

    /** Implemented by classes that handle presenter associated to views with a report list */
    interface ReportListPresenter extends MvpContracts.MvpPresenter {

        /**
         * Attach a {@link ReportDialogView} to this presenter
         * @param view the {@link ReportDialogView} to attach
         */
        void attachReportDialogView(ReportDialogView view);

        /**
         * Detaches the {@link ReportDialogView} from the presenter, if one was attached
         */
        void detachReportDialogView();

        /**
         * Starts the report retrieving task from database
         */
        void loadReports();

        /**
         * Notifies this presenter that a report retrieving task has completed
         * @param result the query result
         */
        void onLoadReportsTaskComplete(QuerySnapshot result);

        /**
         * Notifies this presenter that a report list update task has completed
         */
        void onUpdateTaskComplete();

        /**
         * Notifies this presenter that a report in the list was clicked
         * @param report the clicked report
         */
        void onReportClicked(Report report);

        /**
         * Notifies this presenter that the star button of a report was clicked
         * @param report the report associated with the star button clicked
         */
        void onStarsButtonClicked(Report report);

        /**
         * Notifies this presenter that a star task has completed
         */
        void onStarTaskComplete();

        /**
         * Notifies this presenter that the delete button of a report
         * @param report the report to delete
         */
        void onDeleteReportButtonClicked(Report report);

        /**
         * Called when a report delete task is completed
         * @param result the task result
         */
        void onDeleteReportTaskComplete(DeleteReportTaskResult result);
    }

    /** Implemented by the class that handles new report view (fragment) */
    interface NewReportView extends MvpContracts.MvpView {

        /**
         * Shows a non-cancelable progress dialog
         */
        void showProgressDialog();

        /**
         * Notifies this view that the image provided is not valid
         */
        void notifyInvalidImage();

        /**
         * Notifies this view that the title provided is not valid
         */
        void notifyInvalidTitle();

        /**
         * Notifies this view that the description provided is not valid
         */
        void notifyInvalidDescription();

        /**
         * Dismisses the progress dialog if shown
         */
        void dismissProgressDialog();

        /**
         * Notifies this view that a report send task has completed
         * @param result the task result
         */
        void notifySendTaskCompleted(SendTaskResult result);
    }

    /** Implemented by classes that handle reports list (fragments) */
    interface ReportListView extends MvpContracts.MvpView {

        /**
         * Updates the report list shown
         * @param reports the report list to show
         */
        void updateReports(List<Report> reports);

        /**
         * Hides the refreshing view
         */
        void resetRefreshing();

        /**
         Shows a full-screen dialog with a report information
         * @param report the report to show
         */
        void showReportDialog(Report report);
    }

    interface ReportDialogView extends MvpContracts.MvpView {

        /**
         * Shows a non-cancelable progress dialog
         */
        void showProgressDialog();

        /**
         * Dismisses the progress dialog if shown
         */
        void dismissProgressDialog();

        /**
         * Dismisses the report dialog
         */
        void dismissDialog();

        /**
         * Notifies this view that an error occurred during report delete task
         */
        void notifyDeleteReportError();
    }
}
