package inc.elevati.imycity.main;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
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
    int PAGE_STARRED = 3;
    int PAGE_COMPLETED = 4;

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

        String getCurrentUserName();

        NewReportPresenter getNewReportPresenter();

        ReportListPresenter getAllReportsPresenter();

        ReportListPresenter getCompletedReportsPresenter();

        ReportListPresenter getMyReportsPresenter();

        ReportListPresenter getStarredReportsPresenter();
    }

    interface NewReportPresenter extends MvpContracts.MvpPresenter {

        void sendButtonClicked(String title, String description, Context appContext, Uri imageUri);

        void sendReportData(Report report);

        void onSendTaskComplete(int resultCode);
    }

    interface ReportListPresenter extends MvpContracts.MvpPresenter, Serializable {

        void loadReports();

        void onLoadReportsTaskComplete(QuerySnapshot results);

        void onUpdateTaskComplete();

        void showReport(Report report);

        void starsButtonClicked(Report report);

        void onStarOperationComplete();
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
