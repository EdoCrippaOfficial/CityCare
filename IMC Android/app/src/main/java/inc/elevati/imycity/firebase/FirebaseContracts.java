package inc.elevati.imycity.firebase;

import android.content.Context;
import android.net.Uri;

import inc.elevati.imycity.utils.Report;

public interface FirebaseContracts {

    interface StorageWriter {

        void sendImage(final Report report, Context appContext, Uri imageUri);
    }

    interface DatabaseReader {

        void readAllReports();

        void readMyReports(String userId);

        void readStarredReports(String userId);

        void readCompletedReports();
    }

    interface DatabaseWriter {

        void deleteReport(String reportId);

        void sendReport(final Report report);

        void starReport(final Report report, final String userId);

        void unstarReport(final Report report, final String userId);

        interface onDeleteReportListener {

            void onReportDeleted();

            void onReportDeleteFailed();
        }
    }

    interface AuthHelper {

        void register(final String name, String email, String password);

        void signIn(String email, String password);
    }
}
