package inc.elevati.imycity.main;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import inc.elevati.imycity.utils.Report;

/**
 * This is the generic interface that contains the single
 * interfaces that are required to build MVP architecture
 */
public interface MainContracts {

    /** Constant representing the correct result of the task */
    int RESULT_SEND_OK = 1;

    /** Constant representing an error during image compressing/resizing */
    int RESULT_SEND_ERROR_IMAGE = 2;

    /** Constant representing an error during cloud (database or storage) communication */
    int RESULT_SEND_ERROR_DB = 3;

    interface NewReportPresenter {

        /**
         * Method called to handle the report sending logic
         * @param title the report title
         * @param description the report description
         * @param appContext the context needed by Glide to load image from Uri
         * @param imageUri the Uri of the image
         */
        void sendButtonClicked(String title, String description, Context appContext, Uri imageUri);

        /**
         * Called after image has been sent to storage correctly,
         * it sends the report data to database
         * @param report the report to be sent
         */
        void sendReportData(Report report);

        /**
         * Called by the app kernel to notify that report sending has completed
         * @param resultCode integer representing the operation result
         */
        void dismissViewDialog(int resultCode);
    }

    interface AllReportsPresenter {

        /** Method called to retrieve all reports from database */
        void loadAllReports();

        /**
         * Method called to forward data retrieved from database to View
         * @param results the data retrieved
         */
        void displayAllReports(QuerySnapshot results);

        /** Method called by app kernel that tells View to hide the refreshing image */
        void resetViewRefreshing();

        /**
         * Method called when user click on a report in the list
         * @param report the report to be shown
         */
        void showReport(Report report);
    }

    interface NewReportView {

        /** Method called to show a non-cancelable progress dialog during database operations */
        void showProgressDialog();

        /** Method called by presenter that notifies an invalid image (null Uri) */
        void notifyInvalidImage();

        /** Method called by presenter that notifies an invalid title (empty string) */
        void notifyInvalidTitle();

        /** Method called by presenter that notifies an invalid description (empty string) */
        void notifyInvalidDescription();

        /**
         * Dismisses the progress dialog after a report sending
         * @param resultCode integer representing the operation result
         */
        void dismissProgressDialog(int resultCode);
    }

    interface AllReportsView {


        /**
         * Replaces the displayed reports with the ones in the list
         * @param reports the list of reports to be displayed
         */
        void updateReports(List<Report> reports);

        /** Method called to hide the View shown when refreshing */
        void resetRefreshing();

        /**
         * Called when user clicks on a report in the list, it opens a
         * fullscreen dialog containing all the report information
         * @param report the clicked report
         */
        void showReportDialog(Report report);
    }
}
