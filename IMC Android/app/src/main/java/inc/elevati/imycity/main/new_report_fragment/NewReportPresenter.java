package inc.elevati.imycity.main.new_report_fragment;

import android.graphics.Bitmap;

import java.util.UUID;

import inc.elevati.imycity.main.MainContracts;
import inc.elevati.imycity.utils.firebase.FirestoreSender;
import inc.elevati.imycity.utils.Report;
import inc.elevati.imycity.utils.firebase.StorageWriter;

public class NewReportPresenter implements MainContracts.NewReportPresenter {

    private MainContracts.NewReportView view;

    NewReportPresenter(MainContracts.NewReportView view) {
        this.view = view;
    }

    @Override
    public void handleSendReport(Bitmap image, String title, String description) {
        String uuid = UUID.randomUUID().toString();
        Report report = new Report(title, description, uuid, System.currentTimeMillis());

        // Store image (normal and thumbnail) in Firebase Storage
        StorageWriter storageWriter = new StorageWriter(this);
        storageWriter.send(image, report);
    }

    @Override
    public void sendReportData(Report report) {
        // Store report data (included image name) in Firebase Firestore
        FirestoreSender firestoreSender = new FirestoreSender(this);
        firestoreSender.send(report);
    }

    @Override
    public void dismissViewDialog(boolean error) {
        view.dismissProgressDialog(error);
    }

}
