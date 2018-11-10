package inc.elevati.imycity.main;

import android.graphics.Bitmap;

import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import inc.elevati.imycity.utils.Report;

/**
 * This is the generic interface that contains the single
 * interfaces that are required to build MVP architecture
 */
public interface MainContracts {

    /**
     *  This enum lists all menu_icon pages and it's used in
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
         * Delegates to NewReportPresenter the report sending logic
         * @param image the report image
         * @param title the report title
         * @param description the report description
         */
        void handleSendReport(Bitmap image, String title, String description);

        void dismissViewDialog();
    }

    interface AllReportsPresenter {

        void loadAllReports();

        void displayAllReports(QuerySnapshot results);

        void resetViewRefreshing();

        void showReport(Report report);

        /**
         * Returns the storage reference to the given image
         * @param imageName the file name
         * @param type the type required ("image" or "thumb)
         * @return the storage reference to the given image
         */
        StorageReference getImageReference(String imageName, String type);
    }

    interface NewReportView {

        void dismissProgressDialog();
    }

    interface AllReportsView {

        /**
         * Adds the reports contained in the list to the displayed reports list
         * @param reports the list of reports to be added
         */
        void addReports(List<Report> reports);

        /**
         * Replaces the displayed reports with the ones in the list
         * @param reports the list of reports to be displayed
         */
        void updateReports(List<Report> reports);

        void resetRefreshing();

        void showReportDialog(Report report);
    }
}
