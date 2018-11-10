package inc.elevati.imycity.main;

import android.graphics.Bitmap;

import java.util.UUID;

import inc.elevati.imycity.utils.FirestoreSender;
import inc.elevati.imycity.utils.StorageSender;

public class NewReportPresenter implements MainContracts.NewReportPresenter {

    private MainContracts.NewReportView view;

    NewReportPresenter(MainContracts.NewReportView view) {
        this.view = view;
    }

    @Override
    public void handleSendReport(Bitmap image, String title, String description) {
        String uuid = UUID.randomUUID().toString();

        // Store image (normal and thumbnail) in Firebase Storage
        StorageSender fss = new StorageSender(this);
        fss.send(image, uuid);

        // Store report data (included image name) in Firebase Firestore
        FirestoreSender fds = new FirestoreSender();
        fds.send(title, description, uuid);

    }

    public void dismissViewDialog() {
        view.dismissProgressDialog();
    }

}
