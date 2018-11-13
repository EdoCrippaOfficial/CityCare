package inc.elevati.imycity.main;

import android.graphics.Bitmap;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import inc.elevati.imycity.utils.Report;

/**
 * This is the generic interface that contains the single
 * interfaces that are required to build MVP architecture
 */
public interface MainContracts {

    /**
     *  This enum lists all ic_menu pages and it's used in
     *  communication within View and NewReportPresenter
     */
    enum MenuPages {

        PAGE_ALL(0), PAGE_NEW(1);

        int position;

        MenuPages(int position) {
            this.position = position;
        }
    }


    interface NewReportPresenter {

        /**
         * Starts the report sending transaction
         * @param image the report image
         * @param title the report title
         * @param description the report description
         */
        void handleSendReport(Bitmap image, String title, String description);

        /**
         * Called after image has been sent to storage correctly,
         * it sends the report data to Firestore
         * @param report the report to be sent
         */
        void sendReportData(Report report);

        void dismissViewDialog(boolean error);
    }

    interface AllReportsPresenter {

        void loadAllReports();

        void displayAllReports(QuerySnapshot results);

        void resetViewRefreshing();

        void showReport(Report report);
    }

    interface NewReportView {

        void dismissProgressDialog(boolean error);
    }

    interface AllReportsView {


        /**
         * Replaces the displayed reports with the ones in the list
         * @param reports the list of reports to be displayed
         */
        void updateReports(List<Report> reports);

        void resetRefreshing();

        void showReportDialog(Report report);
    }
}
