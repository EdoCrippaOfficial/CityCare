package inc.elevati.imycity.main.newreport;

import android.content.Context;
import android.net.Uri;

import java.util.UUID;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.EspressoIdlingResource;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.utils.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.utils.firebase.FirestoreSender;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.firebase.StorageWriter;

/** Presenter class used by NewReportFragment to interact with the app kernel */
public class NewReportPresenter implements MainContracts.NewReportPresenter {

    /** The view instance */
    private MainContracts.NewReportView view;

    /** Indicates that a task is completed while View was detached */
    private boolean pendingTask;

    /** Variable set when onSendTaskComplete is called while View was detached */
    private int resultCode;

    @Override
    public void attachView(MvpContracts.MvpView view) {
        this.view = (MainContracts.NewReportView) view;

        // If there were pending tasks, execute them now
        if (pendingTask) {

            // If resultCode is not 0, then onSendTaskComplete has to be executed
            if (resultCode != 0) {
                onSendTaskComplete(resultCode);
                resultCode = 0;
            }
            pendingTask = false;
        }
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    /**
     * Method called to handle the report sending logic
     * @param title the report title
     * @param description the report description
     * @param appContext the context needed by Glide to load image from Uri
     * @param imageUri the Uri of the image
     */
    @Override
    public void sendButtonClicked(String title, String description, Context appContext, Uri imageUri) {
        if (imageUri == null) {
            view.notifyInvalidImage();
        } else if (title.equals("")) {
            view.notifyInvalidTitle();
        } else if (description.equals("")) {
            view.notifyInvalidDescription();
        } else {
            EspressoIdlingResource.increment();
            view.showProgressDialog();
            String uuid = UUID.randomUUID().toString();
            String userId = FirebaseAuthHelper.getUserId();
            String userName = FirebaseAuthHelper.getUserName();
            Report report = new Report(uuid, title, description, System.currentTimeMillis(), userId, userName, 0, 0);

            // Store image (normal and thumbnail) in Firebase Storage
            StorageWriter storageWriter = new StorageWriter(this);
            storageWriter.send(report, appContext, imageUri);
        }
    }

    /**
     * Called by the app kernel to notify that cloud storage sending completed
     * successfully, here we proceed with database sending
     * @param report the report to be sent
     */
    @Override
    public void sendReportData(Report report) {
        FirestoreSender firestoreSender = new FirestoreSender(this);
        firestoreSender.send(report);
    }

    /**
     * Called by the app kernel to notify that report sending has completed
     * If called when View is detached, resultCode is saved and the method
     * is called again when view is re-attached
     * @param resultCode integer representing the operation result
     */
    @Override
    public void onSendTaskComplete(int resultCode) {

        // If view is detached, set the pendingTask flag
        if (view == null) {
            pendingTask = true;
            this.resultCode = resultCode;
            return;
        }
        EspressoIdlingResource.decrement();
        view.dismissProgressDialog();
        view.notifySendTaskCompleted(resultCode);
    }
}
