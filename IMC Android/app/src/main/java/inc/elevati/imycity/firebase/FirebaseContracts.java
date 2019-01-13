package inc.elevati.imycity.firebase;

import android.content.Context;
import android.net.Uri;

import inc.elevati.imycity.utils.Report;

import static inc.elevati.imycity.utils.Report.*;


/** Generic interface that defines contracts for firebase package */
public interface FirebaseContracts {

    /**
     * Implemented by class that handles Firebase Storage tasks
     */
    interface StorageWriter {

        /**
         * Sends a new image to the storage
         * @param report the report related to the image
         * @param appContext the application context, needed to load and compress the image
         * @param imageUri the image Uri path
         */
        void sendImage(final Report report, Context appContext, Uri imageUri);
    }

    /**
     * Implemented by class that handles Firebase Firestore reading tasks
     */
    interface DatabaseReader {

        /**
         * Read all the reports in database
         */
        void readAllReports();

        /**
         * Reads the reports created by the specified user
         * @param userId the user id
         */
        void readMyReports(String userId);

        /**
         * Reads the reports starred by the specified user
         * @param userId the user id
         */
        void readStarredReports(String userId);

        /**
         * Reads the reports with status {@link Status#STATUS_COMPLETED}
         */
        void readCompletedReports();
    }

    /**
     * Implemented by class that handles Firebase Firestore writing tasks
     */
    interface DatabaseWriter {

        /**
         * Deletes the report with the specified id
         * @param reportId the id of the report to delete
         */
        void deleteReport(String reportId);

        /**
         * Creates a new report record in database
         * @param report the report to send
         */
        void sendReport(final Report report);

        /**
         * Adds the specified user to the list that starred the specified report
         * @param report the report to star
         * @param userId the id of the user who starred the report
         */
        void starReport(final Report report, final String userId);

        /**
         * Removes the specified user to the list that starred the specified report
         * @param report the report to un-star
         * @param userId the id of the user who un-starred the report
         */
        void unstarReport(final Report report, final String userId);
    }

    /**
     * Implemented by class that handles Firebase Auth tasks
     */
    interface AuthHelper {

        /**
         * Registers a new user in the Firebase Auth system
         * @param name the user displayed name
         * @param email the user email
         * @param password the user password
         */
        void register(final String name, String email, String password);

        /**
         * Makes a sign-in request to Firebase Auth system with specified data
         * @param email the user email
         * @param password the user password
         */
        void signIn(String email, String password);
    }
}
