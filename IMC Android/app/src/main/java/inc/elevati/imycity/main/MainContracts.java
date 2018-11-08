package inc.elevati.imycity.main;

import android.graphics.Bitmap;

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

    interface NewReportView {

        void dismissProgressDialog();
    }
}
