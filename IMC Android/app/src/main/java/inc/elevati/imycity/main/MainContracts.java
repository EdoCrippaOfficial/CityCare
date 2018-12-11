package inc.elevati.imycity.main;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.utils.Report;

/**
 * This is the generic interface that contains the single
 * interfaces that are required to build MVP architecture
 */
public interface MainContracts {

    int RESULT_SEND_OK = 1;
    int RESULT_SEND_ERROR_IMAGE = 2;
    int RESULT_SEND_ERROR_DB = 3;

    int PAGE_NEW = 0;
    int PAGE_ALL = 1;
    int PAGE_MY = 2;

    int REPORT_SORT_DATE_NEWEST = 1;
    int REPORT_SORT_DATE_OLDEST = 2;
    int REPORT_SORT_STARS_MORE = 3;
    int REPORT_SORT_STARS_LESS = 4;

    interface MainView extends MvpContracts.MvpView {

        void scrollToPage(int page);

        void startLoginActivity();

        void setCheckedMenuItem(int itemId);
    }

    interface MainPresenter extends MvpContracts.MvpPresenter {

        void menuItemClicked(int itemId);

        void pageScrolled(int page);

        String getCurrentUserEmail();

        NewReportPresenter getNewReportPresenter();

        AllReportsPresenter getAllReportsPresenter();

        MyReportsPresenter getMyReportsPresenter();
    }

    interface NewReportPresenter extends MvpContracts.MvpPresenter {

        void sendButtonClicked(String title, String description, Context appContext, Uri imageUri);

        void sendReportData(Report report);

        void onSendTaskComplete(int resultCode);
    }

    interface AllReportsPresenter extends ReportListPresenter {

        void loadAllReports();

        void onLoadAllReportsTaskComplete(QuerySnapshot results);

        void onUpdateTaskComplete();
    }

    interface MyReportsPresenter extends ReportListPresenter {

        void loadMyReports();

        void onLoadMyReportsTaskComplete(QuerySnapshot results);

        void onUpdateTaskComplete();
    }

    interface ReportListPresenter extends MvpContracts.MvpPresenter {

        void showReport(Report report);
    }

    interface NewReportView extends MvpContracts.MvpView {

        void showProgressDialog();

        void notifyInvalidImage();

        void notifyInvalidTitle();

        void notifyInvalidDescription();

        void dismissProgressDialog();

        void notifySendTaskCompleted(int resultCode);
    }

    interface ReportsView extends MvpContracts.MvpView {

        void updateReports(List<Report> reports);

        void resetRefreshing();

        void showReportDialog(Report report);
    }
}
