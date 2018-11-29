package inc.elevati.imycity.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import inc.elevati.imycity.utils.Report;

/**
 * This is the generic interface that contains the single
 * interfaces that are required to build MVP architecture
 */
public interface MainContracts {

    int RESULT_SEND_OK = 1;
    int RESULT_SEND_ERROR_IMAGE = 2;
    int RESULT_SEND_ERROR_DB = 3;

    interface NewReportPresenter {

        /**
         * Starts the report sending transaction
         * @param image the report image
         * @param title the report title
         * @param description the report description
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
         * @param error false if the operation has complete successfully, true otherwise
         */
        void dismissViewDialog(int resultCode);
    }

    interface AllReportsPresenter {

        /**
         * Method called to retrieve all reports from database
        */
        void loadAllReports();

        /**
         * Method called to forward data retrieved from database to View
         * @param results the data retrieved
         */
        void displayAllReports(QuerySnapshot results);

        /**
         * Method called by app kernel that tells View to hide the refreshing image
         */
        void resetViewRefreshing();

        /**
         * Method called when user click on a report in the list
         * @param report the report to be shown
         */
        void showReport(Report report);
    }

    interface NewReportView {

        void showProgressDialog();

        void notifyInvalidImage();

        void notifyInvalidTitle();

        void notifyInvalidDescription();

        /**
         * Dismisses the progress dialog after a report sending
         * @param error should be true if the operation didn't complete
         *              If false the fragments fields (ImageView and EditText
         *              for title and description) are cleared
         */
        void dismissProgressDialog(int resultCode);
    }

    interface AllReportsView {


        /**
         * Replaces the displayed reports with the ones in the list
         * @param reports the list of reports to be displayed
         */
        void updateReports(List<Report> reports);

        /**
         * Method called to hide the View shown when refreshing
         */
        void resetRefreshing();

        /**
         * Called when user clicks on a report in the list, it opens a
         * fullscreen dialog containing all the report information
         * @param report the clicked report
         */
        void showReportDialog(Report report);
    }
}
