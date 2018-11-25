package inc.elevati.imycity.main.newreport;

import android.content.Context;
import android.net.Uri;

import java.util.UUID;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.firebase.FirestoreSender;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.firebase.StorageWriter;

/**
 * Presenter class used by NewReportFragment to interact with the app kernel
 */
public class NewReportPresenter implements MainContracts.NewReportPresenter {

    /**
     * The view instance
     */
    private MainContracts.NewReportView view;

    public NewReportPresenter(MainContracts.NewReportView view) {
        this.view = view;
    }

    /**
     * Method called to handle the report sending logic
     * @param imageStream the image inputStream
     * @param title the report title
     * @param description the report description
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
            view.showProgressDialog();
            String uuid = UUID.randomUUID().toString();
            Report report = new Report(uuid, title, description, System.currentTimeMillis(), "", 0, 0);

            // Store image (normal and thumbnail) in Firebase Storage
            StorageWriter storageWriter = new StorageWriter(this);
            storageWriter.send(report, appContext, imageUri);
        }
    }

    /**
     * Called by the app kernel to notify that storage sending completed
     * successfully, it proceeds with database sending
     * @param report the report to be sent
     */
    @Override
    public void sendReportData(Report report) {
        // Store report data (included image name) in Firebase Firestore
        FirestoreSender firestoreSender = new FirestoreSender(this);
        firestoreSender.send(report);
    }

    /**
     * Called by the app kernel to notify that report sending has completed
     * @param error false if the operation has complete successfully, true otherwise
     */
    @Override
    public void dismissViewDialog(boolean error) {
        view.dismissProgressDialog(error);
    }

}
