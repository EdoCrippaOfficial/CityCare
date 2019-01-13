package inc.elevati.imycity.main.newreport;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import inc.elevati.imycity.firebase.FirebaseContracts;
import inc.elevati.imycity.firebase.FirestoreHelper;
import inc.elevati.imycity.firebase.StorageHelper;
import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.EspressoIdlingResource;
import inc.elevati.imycity.utils.MvpContracts;
import inc.elevati.imycity.firebase.FirebaseAuthHelper;
import inc.elevati.imycity.utils.Report;

/** Presenter associated to {@link NewReportFragment} */
public class NewReportPresenter implements MainContracts.NewReportPresenter {

    /** The view associated to this presenter */
    private MainContracts.NewReportView view;

    /** This flag is set when a task had to be executed when no view was attached to this presenter */
    private boolean pendingTask;

    /** Used only if pendingTask flag is set, if not null indicates that onSendTaskComplete has to be executed */
    private MainContracts.SendTaskResult result;

    /**{@inheritDoc}*/
    @Override
    public void attachView(MvpContracts.MvpView view) {
        this.view = (MainContracts.NewReportView) view;

        // If there were pending tasks, execute them now
        if (pendingTask) {

            // If resultCode is not 0, then onSendTaskComplete has to be executed
            if (result != null) {
                onSendTaskComplete(result);
                result = null;
            }
            pendingTask = false;
        }
    }

    /**{@inheritDoc}*/
    @Override
    public void detachView() {
        this.view = null;
    }

    /**{@inheritDoc}*/
    @Override
    public void sendButtonClicked(String title, String description, Context appContext, Uri imageUri, LatLng position) {
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
            Report report = new Report(uuid, title, description, System.currentTimeMillis(), userId, userName, position);

            // Store image (normal and thumbnail) in Firebase Storage
            FirebaseContracts.StorageWriter writer = new StorageHelper(this);
            writer.sendImage(report, appContext, imageUri);
        }
    }

    /**{@inheritDoc}*/
    @Override
    public void sendReportData(Report report) {
        FirebaseContracts.DatabaseWriter writer = new FirestoreHelper(this);
        writer.sendReport(report);
    }

    /**{@inheritDoc}*/
    @Override
    public void onSendTaskComplete(MainContracts.SendTaskResult result) {

        // If view is detached, set the pendingTask flag
        if (view == null) {
            pendingTask = true;
            this.result = result;
            return;
        }
        EspressoIdlingResource.decrement();
        view.dismissProgressDialog();
        view.notifySendTaskCompleted(result);
    }
}
