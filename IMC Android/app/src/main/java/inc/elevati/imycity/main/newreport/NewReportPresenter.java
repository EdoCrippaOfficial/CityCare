package inc.elevati.imycity.main.newreport;

import android.content.Context;
import android.net.Uri;

import java.util.UUID;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.EspressoIdlingResource;
import inc.elevati.imycity.utils.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.utils.firebase.FirestoreSender;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.firebase.StorageWriter;

/** Presenter class used by NewReportFragment to interact with the app kernel */
public class NewReportPresenter implements MainContracts.NewReportPresenter {

    /** The view instance */
    private MainContracts.NewReportView view;

    /**
     * Public constructor
     * @param view The view instance that interacts with this presenter
     */
    public NewReportPresenter(MainContracts.NewReportView view) {
        this.view = view;
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
            Report report = new Report(uuid, title, description, System.currentTimeMillis(), userId, 0, 0);

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
     * @param resultCode integer representing the operation result
     */
    @Override
    public void onSendTaskComplete(int resultCode) {
        EspressoIdlingResource.decrement();
        view.dismissProgressDialog();
        view.notifySendTaskCompleted(resultCode);
    }

}
